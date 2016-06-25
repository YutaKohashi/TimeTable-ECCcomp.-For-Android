package jp.yuta.kohashi.esc.object;

import java.util.List;

/**
 * Created by Yuta on 2016/06/15.
 */
public class NewsChildListItem extends  Object {

    private String title;
    private String time;
    private String uri;

    public NewsChildListItem(){
        title ="";
        time = "";
        uri = "";
    }
    public String getTitle(){
        return title;
    }

    public String getTime(){
        return time;
    }

    public String getUri(){
        return uri;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setUri(String uri){
        this.uri = uri;
    }

}
