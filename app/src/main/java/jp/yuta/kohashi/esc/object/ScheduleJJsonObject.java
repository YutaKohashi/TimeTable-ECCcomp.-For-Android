package jp.yuta.kohashi.esc.object;

/**
 * Created by Yuta on 2016/04/14.
 */
public class ScheduleJJsonObject {
    private String date;
    private String title;
//
//    public ScheduleJJsonObject(String date,String title){
//        this.date = date;
//        this.title = title;
//    }

    public String getDate(){
        return date;
    }

    public String getTitle(){
        return title;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void settTitle(String title){
        this.title = title;

    }
}
