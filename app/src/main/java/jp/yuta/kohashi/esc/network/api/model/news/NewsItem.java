package jp.yuta.kohashi.esc.network.api.model.news;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;

/**
 * Author : yutakohashi
 * Project name : ESC
 * Date : 26 / 04 / 2017
 */
// 修正予定。API00004のフォーマットに寄せる
@Getter
public class NewsItem implements Serializable {
    //お知らせのID
    @SerializedName("id")
    Integer id;

    // お知らせのカテゴリ名
    @SerializedName("category")
    String category;

    // お知らせのカテゴリ名
    @SerializedName("title")
    String title;

    // お知らせ本文
    @SerializedName("body")
    String body;

    // 最終更新日
    @SerializedName("updated_date")
    String updated_date;

    // 最終更新時間
    @SerializedName("updated_time")
    String updated_time;

    // お知らせの作者名
    @SerializedName("author")
    String author;


    public boolean equals(NewsItem model) {
//        if(this.groupTitle != null){
//            return true;
//        } else {
//
//        }
        return this.getId() == model.getId();
    }

    /**
     * targetListに入っているか
     * @param item
     * @param targetList
     * @return
     */
    public static boolean containsList(NewsItem item, List<NewsItem> targetList){
        boolean result = false;
        for(NewsItem i:targetList){
            if(item.equals(i)){
                result = true;
                break;
            }
        }
        return result;
    }
}
