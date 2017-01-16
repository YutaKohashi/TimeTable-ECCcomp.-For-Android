package jp.yuta.kohashi.esc.object;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yuta on 2016/06/15.
 */

public class NewsChildListItem extends  Object {

    @Setter @Getter
    private String title;
    @Setter @Getter
    private String time;
    @Setter @Getter
    private String uri;

    public NewsChildListItem(){
        title ="";
        time = "";
        uri = "";
    }
//    public String getTitle(){
//        return title;
//    }
//
//    public String getTime(){
//        return time;
//    }
//
//    public String getUri(){
//        return uri;
//    }
//
//    public void setTitle(String title){
//        this.title = title;
//    }
//
//    public void setTime(String time){
//        this.time = time;
//    }
//
//    public void setUri(String uri){
//        this.uri = uri;
//    }

}
