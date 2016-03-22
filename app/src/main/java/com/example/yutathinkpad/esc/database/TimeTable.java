package com.example.yutathinkpad.esc.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by YutaThinkPad on 2016/03/23.
 */

/*
*@column "WeekNo"
*@column "LessonNo"
*@column "Subject"
*@column "ClassRoom"
*/
@Table(name = "TimeTable")
public class TimeTable extends Model {
    @Column(name = "WeekNo")
    public String weekNo;

    @Column(name = "LessonNo")
    public String lessonNo;

    @Column(name = "Subject")
    public String subject;

    @Column(name = "ClassRoom")
    public String classRoom;
}
