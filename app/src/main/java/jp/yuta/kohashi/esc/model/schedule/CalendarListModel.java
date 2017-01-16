package jp.yuta.kohashi.esc.model.schedule;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by yutakohashi on 2017/01/14.
 */

@Setter
@Getter
public class CalendarListModel {
    @SerializedName("4")
    List<CalendarItemModel> month4;
    @SerializedName("5")
    List<CalendarItemModel> month5;
    @SerializedName("6")
    List<CalendarItemModel> month6;
    @SerializedName("7")
    List<CalendarItemModel> month7;
    @SerializedName("8")
    List<CalendarItemModel> month8;
    @SerializedName("9")
    List<CalendarItemModel> month9;
    @SerializedName("10")
    List<CalendarItemModel> month10;
    @SerializedName("11")
    List<CalendarItemModel> month11;
    @SerializedName("12")
    List<CalendarItemModel> month12;
    @SerializedName("1")
    List<CalendarItemModel> month1;
    @SerializedName("2")
    List<CalendarItemModel> month2;
    @SerializedName("3")
    List<CalendarItemModel> month3;

    /**
     * 指定した月のリストを取得するメソッド
     * @param month
     * @return
     */
    public List<CalendarItemModel> get(int month){
        switch(month){
            case 1:
                return month1;
            case 2:
                return month2;
            case 3:
                return month3;
            case 4:
                return month4;
            case 5:
                return month5;
            case 6:
                return month6;
            case 7:
                return month7;
            case 8:
                return month8;
            case 9:
                return month9;
            case 10:
                return month10;
            case 11:
                return month11;
            case 12:
                return month12;
            default:
                return null;
        }
    }
}
