package com.example.yutathinkpad.esc.database;

import com.example.yutathinkpad.esc.object.TimeBlock;

import java.util.List;

/**
 * Created by YutaThinkPad on 2016/03/23.
 */
public class SaveManager {

    /**
     *
     * @param timeBlocks    TimeBlockオブジェクト（教室、授業名）
     * @param weekNo        1:月 2;火 3:水　4:木 5:金 6:土
     */
    public void saveManagerWithSqlite(List<TimeBlock> timeBlocks,int weekNo){
        TimeTable timeTable = new TimeTable();

        int lessonNo = 1;
        for(TimeBlock timeBlock:timeBlocks){
            String classRoom = timeBlock.getClassRoom();
            String subject = timeBlock.getSubject();

            /*
            *@column "WeekNo"
            *@column "LessonNo"
            *@column "Subject"
            *@column "ClassRoom"
            */
            timeTable.weekNo = Integer.toString(weekNo);
            timeTable.lessonNo = Integer.toString(lessonNo);
            timeTable.subject = subject;
            timeTable.classRoom = classRoom;

            timeTable.save();

            lessonNo++;
        }
    }
}
