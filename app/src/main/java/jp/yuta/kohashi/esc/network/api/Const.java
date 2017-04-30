package jp.yuta.kohashi.esc.network.api;

/**
 * Author : yutakohashi
 * Project name : ESC
 * Date : 18 / 04 / 2017
 */
final class Const {
    final static String API_BASE_URL = "http://comp2.ecc.ac.jp/monster/v1/";
    final static int NEWS_LIMIT = 100;


   //****************** エラーコード **************************

    /**
     * トークン認証
     */
    // 認証成功
     static final String SUCCESS_AUTH = "CD00001";
    // 認証失敗
     static final String ERROR_AUTH = "ER00001";


    /**
     * 時間割取得一覧
     */
    // API正常実行

    // トークン期限切れ
     static final String ERROR_EXPIRED_TOKEN = "ER00002";
    // アクセストークンが未指定または不正
     static final String ERROR_INVALID_TOKEN= "ER00003";
    // 時間割の取得に失敗しました
    static final String ERROR_API = "ER00004";


}
