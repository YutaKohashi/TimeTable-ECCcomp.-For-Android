package jp.yuta.kohashi.esc.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yuta on 2016/06/15.
 */
@Getter
@Setter
@AllArgsConstructor
public class NewsItem implements Serializable{

    private String title;
    private String date;
    private String uri;

    private String groupTitle; // グループタイトル

    public NewsItem(String title, String date, String uri){
        this.title = title;
        this.date = date;
        this.uri = uri;
        groupTitle = null;
    }

    public NewsItem(String groupTitle){
        this.groupTitle = groupTitle;
    }



    public boolean equals(NewsItem model) {
        if(this.groupTitle != null){
            return true;
        } else {
            return this.title.equals(model.getTitle())
                    && this.date.equals(model.getDate())
                    && this.uri.equals(model.getUri());
        }

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
