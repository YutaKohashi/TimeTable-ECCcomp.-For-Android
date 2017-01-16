package jp.yuta.kohashi.esc.object;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yuta on 2016/03/21.
 */
@Setter
@Getter
public class TimeBlock {
    String classRoom;
    String  subject;

    //TODO:先生名をTimeBlockオブジェクトに加える
    String teacherName;

    //コンストラクタ
    public TimeBlock(){
        super();
        classRoom = "";
        subject = "";
        teacherName = "";
    }
//
//    public String getClassRoom(){
//        return classRoom;
//    }
//
//    public String getSubject(){
//        return subject;
//    }
//
//    public void setClassRoom(String name){
//        classRoom = name;
//    }
//
//    public void setSubject(String name){
//        subject = name;
//    }
}
