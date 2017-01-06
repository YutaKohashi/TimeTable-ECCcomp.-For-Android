package jp.yuta.kohashi.esc.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yuta on 2016/06/20.
 */
@Setter
@Getter
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


}
