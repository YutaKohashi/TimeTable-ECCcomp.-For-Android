package jp.yuta.kohashi.esc.model;

import java.io.Serializable;

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
        return title.equals(model.title)
                && date.equals(model.date)
                && uri.equals(model.uri)
                && groupTitle.equals(model.groupTitle);
    }
}
