package jp.yuta.kohashi.esc.http2;

import android.content.Context;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;

import jp.yuta.kohashi.esc.http.JavaNetCookieJar;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;

/**
 * Created by yutakohashi on 2016/11/15.
 */


public class HttpBase {

    private OkHttpClient mClient;
    private CookieManager cookieManager;
    private CookieJar cookieJar;

    //コンストラクタ
    //クライアントを共有
    public HttpBase(){
        cookieManager = new CookieManager();
        cookieJar = new JavaNetCookieJar(cookieManager);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        mClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();
    }

    // Getメソッド（sync）
    public HttpResultClass httpGet(String url, String referer){
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
            e.printStackTrace();
            result.setBool(false);
        } catch (Exception e) {
            e.printStackTrace();
            result.setBool(false);
        }
        return result;
    }

    // Postメソッド (sync)
    public HttpResultClass httpPost(String url, Map<String,String> requestBody, String referer){
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
            e.printStackTrace();
            result.setBool(false);
        } catch (Exception e) {
            e.printStackTrace();
            result.setBool(false);
        }

        return result;
    }

    //コールバック
//    private  boolean _bool;
//    private  String  _html;
//    public interface AccessCallbacks {
//        public void callback(boolean bool,String html);
//    }
////    public void setCallbacks(boolean bool ,String html){
////        _bool = bool;
////        _html = html;
////    }
//    public interface SuccessCallbacks {
//        public void callback(boolean bool);
//    }
//    public void setSuccessCallbacks(boolean bool){
//        _issuccess = bool;
//    }

    // リクエストボディを作成するメソッド
    private RequestBody createRequestBody(Map<String,String> body){
        FormBody.Builder builder = new FormBody.Builder();

        for ( Map.Entry<String, String> entry : body.entrySet() ) {
            builder.add( entry.getKey(), entry.getValue() );
        }

        return  builder.build();
    }

}
