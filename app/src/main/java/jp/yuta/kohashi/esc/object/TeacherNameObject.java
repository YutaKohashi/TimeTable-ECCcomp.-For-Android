package jp.yuta.kohashi.esc.object;

/**
 * Created by Yuta on 2016/05/09.
 */
public class TeacherNameObject {

    String subject;
    String teacher;

    public TeacherNameObject(){
        subject = "";
        teacher = "";
    }

    public String getSubject() {
        return subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
