package jp.yuta.kohashi.esc.network.api.model;

/**
 * Author : yutakohashi
 * Project name : ESC
 * Date : 25 / 04 / 2017
 */

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * トークンを扱うモデル
 */
@Getter
public class Token {
    // 認証成功CD00001 / 認証失敗ER00001
    @SerializedName("code")
    String code;
    //アクセストークン
    @SerializedName("token")
    String token;
    // トークンの使用期限
    @SerializedName("expire")
    String expire;
}
