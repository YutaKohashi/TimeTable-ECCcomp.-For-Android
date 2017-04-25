package jp.yuta.kohashi.esc.network.api.model.timeTable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Author : yutakohashi
 * Project name : ESC
 * Date : 10 / 04 / 2017
 */
@Getter
@Setter
public class TimeTable implements Serializable {
    // 時間割ID。時間割を識別する為の固有番号
    @SerializedName("id")
    String id;
    // 曜日 1:月 2:火 3:水 4:木 5:金 6:土 0: 日
    @SerializedName("week")
    Integer week;
    // 時限
    @SerializedName("term")
    Integer term;
    // コード。年度単位で一意
    @SerializedName("lesson_code")
    String lessonCode;
    // 科目名
    @SerializedName("lesson_name")
    String lessonName;
    // 教室名
    @SerializedName("room")
    String room;
    // 科目コース
    @SerializedName("course")
    String course;
    // 先生（リスト）
    @SerializedName("teachers")
    List<TeacherTImeTable> teachers;

    public boolean equals(TimeTable timeTable){
        if(this.lessonName.equals(timeTable.lessonName)  && this.room.equals(timeTable.room)){
            return true;
        }
        return false;
    }



}
