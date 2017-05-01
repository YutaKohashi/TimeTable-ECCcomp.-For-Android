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
// 修正予定。API00004のフォーマットに寄せる
@Getter
public class NewsItem implements Serializable {
    //お知らせのID
    @SerializedName("id")
    private Integer id;

    // お知らせのカテゴリ名
    @SerializedName("category")
    private String category;

    // お知らせのカテゴリ名
    @SerializedName("title")
    private String title;

    // お知らせ本文
    @SerializedName("body")
    private String body;

    // 最終更新日
    @SerializedName("updated_date")
    private String updated_date;

    // 最終更新時間
    @SerializedName("updated_time")
    private String updated_time;

    // お知らせの作者名
    @SerializedName("author")
    private String author;

    public NewsItem(Integer id, String title, String body, String updated_date){
        this.id = id;
        this.title = title;
        this.body = body;
        this.updated_date = updated_date;
    }

    public boolean equals(NewsItem model) {
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
