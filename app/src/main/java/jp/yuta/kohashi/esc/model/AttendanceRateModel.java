package jp.yuta.kohashi.esc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yuta on 2016/03/27.
 */

@Setter
@Getter
@AllArgsConstructor
public class AttendanceRateModel {

    String subjectName;         //教科
    String unit;                //単位
    String attendanceNumber;    //出席数
    String absentNumber;        //欠席数
    String lateNumber;          //遅刻数
    String publicAbsentNumber1; //公欠１
    String publicAbsentNumber2; //公欠２
    String attendanceRate;      //出席率
    String shortageseNumber;      //

    public AttendanceRateModel(){
        subjectName = "";
        unit = "";
        attendanceNumber = "";
        absentNumber = "";
        lateNumber = "";
        publicAbsentNumber1 = "";
        publicAbsentNumber2 = "";
        attendanceRate = "";
        shortageseNumber = "";
    }

    public AttendanceRateModel(String subjectName) {
        this.subjectName = subjectName;
        unit = "";
        attendanceNumber = "";
        absentNumber = "";
        lateNumber = "";
        publicAbsentNumber1 = "";
        publicAbsentNumber2 = "";
        attendanceRate = "";
        shortageseNumber = "";
    }
}
