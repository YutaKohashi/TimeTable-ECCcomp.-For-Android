package jp.yuta.kohashi.esc.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;

import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yutakohashi on 2016/11/15.
 */


public class HttpBase {

    private final static String TAG = HttpBase.class.getSimpleName();

    private static OkHttpClient mClient;
    private static CookieManager cookieManager;
    private static CookieJar cookieJar;

    public static void init(){
        if(cookieManager ==null )cookieManager = new CookieManager();
        if(cookieJar == null) cookieJar = new JavaNetCookieJar(cookieManager);
        if(cookieManager == null) cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        if(mClient == null)
        mClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();
    }

    /***
     * GET method
     * @param url
     * @param referer
     * @return
     */
    protected static HttpResultClass httpGet(String url, String referer){
        HttpResultClass result = new HttpResultClass();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Referer",referer)
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87")
                .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .build();

        Response response;
        try{
            response = mClient.newCall(request).execute();
            Thread.sleep(100);

            if(response.code() != 200) {
                throw new Exception("Failure Network Connection");
            }

            result.setString(response.body().string());
            result.setBool(true);
            response.body().close();
        }catch(IOException | InterruptedException e){
            Log.d(TAG,e.toString());
            result.setBool(false);
        } catch (Exception e) {
            Log.d(TAG,e.toString());
            result.setBool(false);
        }
        return result;
    }

    /**
     * POST method
     * @param url
     * @param requestBody
     * @param referer
     * @return
     */
    protected static HttpResultClass httpPost(String url, Map<String,String> requestBody, String referer){
        HttpResultClass result = new HttpResultClass();

        Request request = new Request.Builder()
                .url(url)
                .post(createRequestBody(requestBody))
                .header("Referer",referer)
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87")
                .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .build();

        Response response;
        try{
            response = mClient.newCall(request).execute();
            Thread.sleep(100);

            if(response.code() != 200) {
                throw new Exception("Failure Network Connection");
            }

            result.setString(response.body().string());
            result.setBool(true);
            response.body().close();
        }catch(IOException | InterruptedException e){
            Log.d(TAG,e.toString());
            result.setBool(false);
        } catch (Exception e) {
            Log.d(TAG,e.toString());
            result.setBool(false);
        }

        return result;
    }

    /**
     * ネットワーク接続チェック
     * @param context
     * @return
     */
    public static boolean netWorkCheck(Context context){
        ConnectivityManager cm =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if( info != null ){
            return info.isConnected();
        } else {
            return false;
        }
    }


    /***
     * リクエストボディを作成するメソッド
     * @param body
     * @return
     */
    private static  RequestBody createRequestBody(Map<String,String> body){
        FormBody.Builder builder = new FormBody.Builder();

        for ( Map.Entry<String, String> entry : body.entrySet() ) {
            builder.add( entry.getKey(), entry.getValue() );
        }

        return  builder.build();
    }

}
