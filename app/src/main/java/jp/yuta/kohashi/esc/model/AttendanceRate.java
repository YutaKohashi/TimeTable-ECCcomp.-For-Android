package jp.yuta.kohashi.esc.model;

import jp.yuta.kohashi.esc.model.enums.AttendanceRateType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yuta on 2016/03/27.
 */

@Setter
@Getter
@AllArgsConstructor
public class AttendanceRate {

    private String subjectName;         //教科
    private String unit;                //単位
    private String attendanceNumber;    //出席数
    private String absentNumber;        //欠席数
    private String lateNumber;          //遅刻数
    private String publicAbsentNumber1; //公欠１
    private String publicAbsentNumber2; //公欠２
    private String attendanceRate;      //出席率
    private String shortageseNumber;      //

    private AttendanceRateType type;

    public AttendanceRate(){
        subjectName = "";
        unit = "";
        attendanceNumber = "";
        absentNumber = "";
        lateNumber = "";
        publicAbsentNumber1 = "";
        publicAbsentNumber2 = "";
        attendanceRate = "";
        shortageseNumber = "";
        type = AttendanceRateType.ZENKI;
    }
}
