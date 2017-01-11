package jp.yuta.kohashi.esc.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yuta on 2016/03/21.
 */
@Setter
@Getter
public class TimeBlockModel{
    private String classRoom;
    private String subject;
    private String teacherName;
    private int rowNum;
    private int colNum;

    //コンストラクタ
    public TimeBlockModel(){
        classRoom = "";
        subject = "";
        teacherName = "";
        rowNum = -1;
        colNum = -1;
    }

}
