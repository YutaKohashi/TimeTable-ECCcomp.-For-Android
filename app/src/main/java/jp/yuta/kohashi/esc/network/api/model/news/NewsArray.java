package jp.yuta.kohashi.esc.network.api.model.news;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;

/**
 * Author : yutakohashi
 * Project name : ESC
 * Date : 10 / 04 / 2017
 */
@Getter
public class NewsArray implements Serializable{
    // API正常実行
    @SerializedName("code")
    String code;

    // お知らせArray
    @SerializedName("notices")
    List<NewsItem> newsArray;
}
