package jp.yuta.kohashi.esc.network.api.model.news;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Author : yutakohashi
 * Project name : ESC
 * Date : 26 / 04 / 2017
 */
@Getter
@Setter
public class NewsDetail implements Serializable {
    //　お知らせID
    @SerializedName("id")
    Integer id;

    // お知らせのカテゴリ名
    @SerializedName("category")
    String category;

    // お知らせタイトル
    @SerializedName("title")
    String title;

    //お知らせ本文
    @SerializedName("body")
    String body;

    // 最終更新日
    @SerializedName("updated_at")
    String updated_at;

    // お知らせの作者名
    @SerializedName("author")
    String author;

    // 添付ファイルリスト
    @SerializedName("files")
    List<NewsFile> newsFiles;

    //　お知らせ配信先コースリスト
    @SerializedName("courses")
    List<NewsCourse> newsCourses;
}
