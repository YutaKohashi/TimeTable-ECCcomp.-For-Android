package jp.yuta.kohashi.esc.network.api;

import jp.yuta.kohashi.esc.network.api.model.Token;
import jp.yuta.kohashi.esc.network.api.model.news.NewsArray;
import jp.yuta.kohashi.esc.network.api.model.news.NewsDetailRoot;
import jp.yuta.kohashi.esc.network.api.model.schedule.ScheduleRoot;
import jp.yuta.kohashi.esc.network.api.model.timeTable.RootTimeTable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
     * 学校からのお知らせリストを取得するメソッド
     */
    void getSchoolNews(String token ,int limit, Callback<NewsArray> callback) {
        getService().getNews(1,token,limit).enqueue(callback);
    }

    /**
     *　担任からのお知らせを取得するメソッド
     */
    void getTaninNews(String token ,int limit, Callback<NewsArray> callback) {
        getService().getNews(2,token,limit).enqueue(callback);
    }

    /**
     * お知らせ詳細を取得するメソッド
     */
    void getNewsDetail(String token, Integer newsId, Callback<NewsDetailRoot> callback){
        getService().getSchoolNewsDetail(token,newsId).enqueue(callback);

    }

    /**
     * 学校行事
     */
     void getSchedule(String token, Integer year, Integer month, Callback<ScheduleRoot> callback) {
        getService().getSchedule(token,year,month).enqueue(callback);
    }


     interface service {

        /**
         * トークン生成するリクエスト
         * @param userId
         * @param password
         * @return
         */
        @FormUrlEncoded
        @POST("token/create")
        Call<Token> postToken(@Field("username") String userId,
                              @Field("pass") String password);

         /**
          * 時間割を取得
          * @param code
          * @param token
          * @return
          */
        @GET("timetable/find_by_code")
        Call<RootTimeTable> getTimetable(@Query("code") String code,
                                         @Query("token") String token);


         /**
          * お知らせのヘッドラインを取得する為のAPI
          * ヘッドラインの取得件数の最大値。最大100 未設定の場合は10
          * @param type 1：学校 　2：担任
          * @param token
          * @param limit
          * @return
          */
         @GET("notice/headline")
         Call<NewsArray> getNews(@Query("type") int type,@Query("token") String token, @Query("limit") int limit);

         /**
          * お知らせの詳細を取得する為のAPI
          * @param token
          * @param newsId　お知らせのID
          * @return
          */
         @GET("notice/find")
         Call<NewsDetailRoot> getSchoolNewsDetail(@Query("token") String token, @Query("id") Integer newsId);


         /**
          * スケジュールを取得するAPI
          * @param token
          * @param year
          * @param month
          * @return
          */
        @GET("schedule/find")
        Call<ScheduleRoot> getSchedule(@Query("token") String token, @Query("year") Integer year, @Query("month") Integer month);

    }
}
