package jp.yuta.kohashi.esc.network.api;

import jp.yuta.kohashi.esc.network.api.model.Token;
import jp.yuta.kohashi.esc.network.api.model.timeTable.RootTimeTable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.FormUrlEncoded;

/**
 * Author : yutakohashi
 * Project name : ESC
 * Date : 10 / 04 / 2017
 */
 class EscJsonApi {
    private EscJsonApi.service mService;

     EscJsonApi() {
        mService = ApiClient.getClient().create(EscJsonApi.service.class);
    }

    private EscJsonApi.service getService() {
        return mService;
    }


    /**
     * トークン取得
     */
     void postToken(String userId, String password, Callback<Token> callback) {
        getService().postToken(userId,password).enqueue(callback);
    }

    /**
     * 時間割
     */
     void getTimeTable(String code, String token, Callback<RootTimeTable> callback) {
        getService().getTimetable(code, token).enqueue(callback);
    }

    /**
     * 担任からのお知らせ
     */
     void getTaninNews() {

    }

    /**
     * 学校からのお知らせ
     */
     void getSchoolNews() {
    }


    /**
     * 学校行事
     */
     void getSchoolGyoji() {

    }

     void createToken() {

    }

     interface service {

        /**
         * トークン生成するリクエスト
         * @param userId
         * @param password
         * @return
         */
//        @Headers({
//                "Accept: max-age=640000",
//                "Accept-Encoding",
//                "Accept-Language"
//        })
        @FormUrlEncoded
        @POST("token/create")
        Call<Token> postToken(@Field("username") String userId,
                              @Field("pass") String password);


        @GET("timetable/find_by_code")
        Call<RootTimeTable> getTimetable(@Query("code") String code,
                                         @Query("token") String token);


    }
}
