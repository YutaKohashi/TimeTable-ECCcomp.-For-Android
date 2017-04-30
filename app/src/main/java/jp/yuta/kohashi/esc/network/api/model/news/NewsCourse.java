package jp.yuta.kohashi.esc.network.api.model.news;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;

/**
 * Author : yutakohashi
 * Project name : ESC
 * Date : 26 / 04 / 2017
 */
@Getter
public class NewsCourse implements Serializable {

    // お知らせ配信先コース名
    @SerializedName("name")
    String courseName;
}
