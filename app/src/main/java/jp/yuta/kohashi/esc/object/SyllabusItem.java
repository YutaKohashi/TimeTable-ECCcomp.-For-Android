package jp.yuta.kohashi.esc.object;

/**
 * Created by Yuta on 2016/04/06.
 */
public class SyllabusItem {
    String dateText;
    String containtsText;


    public SyllabusItem(){
        dateText = "";
        containtsText = "";
    }

    public void setDateText(String date){
        dateText = date;
    }

    public void setContaintsText(String containts){
        containtsText = containts;
    }

    public String getDateText(){
        return dateText;
    }
    public String getContaintsText(){
        return containtsText;
    }
}
