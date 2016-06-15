package jp.yuta.kohashi.esc.object;

import java.util.List;

/**
 * Created by Yuta on 2016/06/15.
 */
public class NewsParentListItem extends Object{

    private String title;
    private List<NewsChildListItem> childItems;


    public void setTitle(String title){
        this.title = title;
    }

    public void setChildItems(List<NewsChildListItem> items){
        this.childItems = items;
    }

    public String getTitle(){
        return title;
    }

    public List<NewsChildListItem> getChildItems(){
        return childItems;
    }
}
