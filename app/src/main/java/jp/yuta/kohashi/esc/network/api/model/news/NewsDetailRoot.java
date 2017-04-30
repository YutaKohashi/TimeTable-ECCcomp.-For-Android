package jp.yuta.kohashi.esc.network.api.model.news;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;

/**
 * Author : yutakohashi
 * Project name : ESC
 * Date : 27 / 04 / 2017
 */
@Getter
public class NewsDetailRoot implements Serializable {
    @SerializedName("code")
    String code;

    @SerializedName("notice")
    NewsDetail newsDetail;
}
