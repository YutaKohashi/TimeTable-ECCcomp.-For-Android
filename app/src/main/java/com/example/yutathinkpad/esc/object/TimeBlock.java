package com.example.yutathinkpad.esc.object;

/**
 * Created by Yuta on 2016/03/21.
 */
public class TimeBlock {
    String classRoom;
    String  subject;

    public TimeBlock(){
        classRoom = "";
        subject = "";
    }

    public String getClassRoom(){
        return classRoom;
    }

    public String getSubject(){
        return subject;
    }

    public void setClassRoom(String name){
        classRoom = name;
    }

    public void setSubject(String name){
        subject = name;
    }
}
