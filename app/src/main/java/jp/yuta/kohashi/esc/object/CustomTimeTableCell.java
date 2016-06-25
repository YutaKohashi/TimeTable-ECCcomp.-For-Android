package jp.yuta.kohashi.esc.object;

import jp.yuta.kohashi.esc.chrome.CustomTabsHelper;

/**
 * Created by Yuta on 2016/06/20.
 */
public class CustomTimeTableCell {
    private int x;
    private int y;

    private String subject;
    private String room;
    private String teacherName;

    //コンストラクタ
    public CustomTimeTableCell(){
        subject = "";
        room = "";
        teacherName = "";
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }
    public void setRoom(String room){
        this.room = room;
    }
    public void setTeacherName(String teacherName){
        this.teacherName= teacherName;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public String getSubject(){
        return subject;
    }
    public String getRoom(){
        return room;
    }
    public String getTeacherName(){
        return teacherName;
    }

}
