package jp.yuta.kohashi.esc.network.api.model.news;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;

/**
 * Author : yutakohashi
 * Project name : ESC
 * Date : 26 / 04 / 2017
 */

/**
 * 添付ファイルを扱うモデル
 */
@Getter
public class NewsFile implements Serializable{
    // 添付ファイルID
    @SerializedName("id")
    Integer id;

    // 添付ファイル名
    @SerializedName("name")
    String fileName;

    @Override
    public String toString() {
        return fileName;
    }
}
