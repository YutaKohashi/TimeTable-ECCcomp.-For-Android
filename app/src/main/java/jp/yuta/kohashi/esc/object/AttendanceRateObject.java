package jp.yuta.kohashi.esc.object;

import jp.yuta.kohashi.esc.fragment.AttendanceRateFragment;

/**
 * Created by Yuta on 2016/03/27.
 */
public class AttendanceRateObject {
//    <th style="width:28%">教科名</th>
//    <th style="width:8%">単位</th>
//    <th style="width:8%">出席</th>
//    <th style="width:8%">欠席</th>
//    <th style="width:8%">遅刻</th>
//    <th style="width:8%">公欠１</th>
//    <th style="width:8%">公欠２</th>
//    <th style="width:8%">出席率</th>
//    <th style="width:8%">出席<br />不足単位</th>

    String subjectName;         //教科
    String unit;                //単位
    String attendanceNumber;    //出席数
    String absentNumber;        //欠席数
    String lateNumber;          //遅刻数
    String publicAbsentNumber1; //公欠１
    String publicAbsentNumber2; //公欠２
    String attendanceRate;      //出席率
    String shortageNumber;      //

    public AttendanceRateObject(){
        subjectName = "";
        unit = "";
        attendanceNumber = "";
        absentNumber = "";
        lateNumber = "";
        publicAbsentNumber1 = "";
        publicAbsentNumber2 = "";
        attendanceRate = "";
        shortageNumber = "";
    }

    public AttendanceRateObject(String subjectName){
        this.subjectName = subjectName;
        unit = "";
        attendanceNumber = "";
        absentNumber = "";
        lateNumber = "";
        publicAbsentNumber1 = "";
        publicAbsentNumber2 = "";
        attendanceRate = "";
        shortageNumber = "";
    }

    //セッター
    public void setSubjectName(String name){
        subjectName = name;
    }

    public void setUnit(String name){
        unit = name;
    }

    public void setAttendanceNumber(String num){
        attendanceNumber = num;
    }

    public void setAbsentNumber(String num){
        absentNumber = num;
    }

    public void setLateNumber(String num){
        lateNumber = num;
    }

    public void setPublicAbsentNumber1(String num){
        publicAbsentNumber1 = num;
    }

    public void setPublicAbsentNumber2(String num){
        publicAbsentNumber2 = num;
    }

    public void setAttendanceRate(String rate){
        attendanceRate = rate;
    }

    public void setShortageNumber(String num){
        shortageNumber = num;
    }

    public String getSubjectName(){
        return subjectName;
    }

    public String getUnit(){
        return  unit;
    }

    public String getAttendanceNumber(){
        return attendanceNumber;
    }

    public String getAbsentNumber(){
        return  absentNumber;
    }

    public String getLateNumber(){
        return lateNumber;
    }

    public String getPublicAbsentNumber1(){
        return publicAbsentNumber1;
    }

    public String getPublicAbsentNumber2(){
        return publicAbsentNumber2;
    }

    public String getAttendanceRate(){
        return attendanceRate;
    }

    public String getShortageNumber(){
        return shortageNumber;
    }

}
