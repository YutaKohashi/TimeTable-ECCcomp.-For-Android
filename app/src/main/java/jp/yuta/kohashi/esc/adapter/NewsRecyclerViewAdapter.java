package jp.yuta.kohashi.esc.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.anprosit.android.promise.Callback;
import com.anprosit.android.promise.NextTask;
import com.anprosit.android.promise.Promise;
import com.anprosit.android.promise.Task;

import java.io.IOException;
import java.net.CookiePolicy;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.activity.MainActivity;
import jp.yuta.kohashi.esc.activity.NewsWebViewActivity;
import jp.yuta.kohashi.esc.http.JavaNetCookieJar;
import jp.yuta.kohashi.esc.object.NewsChildListItem;
import jp.yuta.kohashi.esc.preference.LoadManager;
import jp.yuta.kohashi.esc.preference.SaveManager;
import jp.yuta.kohashi.esc.tools.CustomProgressDialog;
import jp.yuta.kohashi.esc.tools.GetValuesBase;
import okhttp3.CookieJar;
import java.net.CookieManager;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Yuta on 2016/06/15.
 */
public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsRecyclerViewHolder> {
    private List<NewsChildListItem> childItems;
    private Context context;


    static final String URL1 = "http://comp2.ecc.ac.jp/sutinfo/login";          //ログイン画面
    static final String URL2 = "http://comp2.ecc.ac.jp/sutinfo/auth/attempt";   //実ログイン
    static final String URL3 ="http://comp2.ecc.ac.jp/sutinfo/logout";
    static final String PREF_NAME_ID_PASS = "ip";

    final String TAG ="error:::";
    OkHttpClient client;
    static String mLastResponse;
    GetValuesBase getValuesBase;
    CookieManager cookieManager;
    CookieJar cookieJar;

    Request request;
    Response response;

    ProgressDialog progressDialog;
    CustomProgressDialog customDialog;

    List<String> list;

    SaveManager saveManger;

    //コンストラクタ
    public NewsRecyclerViewAdapter(List<NewsChildListItem> childItems, Context context) {
        this.childItems = childItems;
        this.context = context;
    }

    @Override
    public NewsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == -1) {
            //コンテンツごとのレイアウト
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_parent_item, parent, false);
        } else {
            //記事のレイアウト
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_child_item, parent, false);

        }

        return new NewsRecyclerViewHolder(v, viewType);
    }

    //アイテムの設定
    @Override
    public void onBindViewHolder(final NewsRecyclerViewHolder holder,final int position) {


        // view type に応じて処理を分ける。
        // 今回はどちらも TextView なので単にセットする文字列を変えている。
        if (holder.getItemViewType() == -1) {
            //タイトル
            try {
                holder.parentTitle.setText(childItems.get(position).getTitle());
            } catch (NullPointerException ex) {
                holder.parentTitle.setText("");
            }


        } else {
            //記事
            try {
                holder.title.setText(childItems.get(position).getTitle());
                holder.time.setText(childItems.get(position).getTime());
                holder.uri.setText(childItems.get(position).getUri());
            } catch (NullPointerException ex) {
                holder.title.setText("");
                holder.time.setText("");
                holder.uri.setText("");
            }
        }

        LoadManager loadManager = new LoadManager();
        list = loadManager.loadManagerWithPreferenceForString(context,PREF_NAME_ID_PASS,"ip");


        customDialog = new CustomProgressDialog();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                try {
                    String uri = holder.uri.getText().toString();
                    cookieManager = new CookieManager();
                    cookieJar = new JavaNetCookieJar(cookieManager);
                    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
                    getValuesBase = new GetValuesBase();


                    final String userId = list.get(0);
                    final String pass = list.get(1);
                    //View view = getActivity().f

                    client = new OkHttpClient.Builder()
                            //.proxy(p)
                            .cookieJar(cookieJar)
                            .build();


                    Promise.with(this, String.class).then(new Task<String, String>() {
                        @Override
                        public void run(String s, NextTask<String> nextTask) {
                            //  ダイアログを表示
                            if (progressDialog == null) {
                                progressDialog = customDialog.createProgressDialogForLoading(context);
                                progressDialog.show();
                            } else {
                                progressDialog.show();
                            }

                            if(!getValuesBase.ConnectionCheck(context)){
                                progressDialog.dismiss();
                                Snackbar.make(v,"インターネットに接続されていません",Snackbar.LENGTH_LONG).show();
                                return;
                            }
                            nextTask.run(null);

                        }

                    }).thenOnAsyncThread(new Task<String, String>() {
                        @Override
                        public void run(String result, NextTask<String> nextTask) {
                            // ログインページヘ
                            try {
                                request = new Request.Builder()
                                        .url(URL1)
                                        .build();

                                response = client.newCall(request).execute();
                                mLastResponse = response.body().string();
                            } catch (UnknownHostException e) {
                                Toast.makeText(context, "通信に失敗しました", Toast.LENGTH_LONG).show();
                                Log.d("okhttp::::", "通信に失敗しました");
                            } catch (IOException  e) {
                                e.printStackTrace();

                            }
                            nextTask.run(mLastResponse);
                        }

                    }).thenOnAsyncThread(new Task<String, String>() {


                        @Override
                        public void run(String s, NextTask<String> nextTask) {
                            // トークンの抽出処理
                            String token = getValuesBase.GetToken(mLastResponse);
                            Log.d(TAG, token);
                            if ("ERROR".equals(token)) {
                                //例外処理
                                Log.d(TAG, "トークン取得失敗");
                            }
                            nextTask.run(token);
                        }

                    }).thenOnAsyncThread(new Task<String, String>() {

                        @Override
                        public void run(String token, NextTask<String> nextTask) {

                            // ESCへPOST
                            // パラメータ
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("_token", token)
                                    .add("userid", userId)
                                    .add("password", pass)
                                    .build();

                            // リクエストオブジェクトを作成
                            request = new Request.Builder()
                                    .url(URL2)
                                    .post(requestBody)
                                    .addHeader("Referer", "http://comp2.ecc.ac.jp/sutinfo/login")
                                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87")
                                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                                    .build();

                            try {
                                Log.d(TAG, "POST");
                                response = client.newCall(request).execute();
                                mLastResponse = response.body().string();



                            } catch (IOException  e) {
                                Log.d(TAG, "POST失敗");
                                e.printStackTrace();
                                Toast.makeText(context, "通信に失敗しました", Toast.LENGTH_LONG).show();
                            }

                            nextTask.run(mLastResponse);
                        }

                    }).thenOnAsyncThread(new Task<String, String>() {
                        @Override
                        public void run(String result, NextTask<String> nextTask) {

                            //ここで通信各記事へ通信処理をする
                            try {
                                request = new Request.Builder()
                                        .url(childItems.get(position).getUri())
                                        .build();

                                response = client.newCall(request).execute();

                                mLastResponse = response.body().string();
                            } catch (UnknownHostException e) {
                                Toast.makeText(context, "通信に失敗しました", Toast.LENGTH_LONG).show();
                                Log.d("okhttp::::", "通信に失敗しました");
                            } catch (IOException e) {
                                e.printStackTrace();

                            }
                            nextTask.run(mLastResponse);
                        }
                    }).thenOnAsyncThread(new Task<String, String>() {

                        @Override
                        public void run(String result, NextTask<String> nextTask) {

                            // ログアウト処理
                            mLastResponse = result;

                            Request request = new Request.Builder()
                                    .url(URL3)
                                    .build();
                            //Response response;
                            try {
                                // ログアウト
                                client.newCall(request).execute();
                                //mLastResponse = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            nextTask.run(mLastResponse);
                        }
                    }).setCallback(new Callback<String>() {
                        @Override
                        public void onSuccess(String result) {
//                            mLastResponseにソース・ファイルが入っている
                            String html = getValuesBase.NarrowingValues("<p class=\"body clear\">","<h3>添付ファイル</h3>",result,false);


                            Intent intent = new Intent((Activity)context, NewsWebViewActivity.class);
                            intent.putExtra("html",html);
                            intent.putExtra("title",childItems.get(position).getTitle());
                            intent.putExtra("date",childItems.get(position).getTime());
                            context.startActivity(intent);
                            progressDialog.dismiss();
//                            Snackbar.make(v,"更新しました",Snackbar.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Bundle bundle, Exception e) {
                            progressDialog.dismiss();
                            Snackbar.make(v,"更新に失敗しました",Snackbar.LENGTH_SHORT).show();
                        }

                    }).create().execute(null);

                } catch (Exception ex) {

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return childItems.size();
    }


    public static class NewsRecyclerViewHolder extends RecyclerView.ViewHolder {

        //記事
        public TextView title;
        public TextView time;
        public TextView uri;
        public CardView cardView;

        //タイトル
        public TextView parentTitle;

        public NewsRecyclerViewHolder(View v, int viewType) {
            super(v);

            //viewTypeによってレイアウトを分ける
            if (viewType == -1) {
                //タイトル
                parentTitle = (TextView) v.findViewById(R.id.news_parent_title);
            } else {
                //記事
                title = (TextView) v.findViewById(R.id.news_child_title);
                time = (TextView) v.findViewById(R.id.news_child_time);
                uri = (TextView) v.findViewById(R.id.url_hidden_text_view);
                cardView = (CardView) itemView.findViewById(R.id.news_child_item);
            }
        }
    }


    //複数レイアウト
    @Override
    public int getItemViewType(int position) {
        NewsChildListItem childListItem = childItems.get(position);
        if (childListItem.getTime() == "") {
            return -1;
        }
        return position;
    }

}
