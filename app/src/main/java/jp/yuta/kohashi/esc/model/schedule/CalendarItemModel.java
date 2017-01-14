package jp.yuta.kohashi.esc.model.schedule;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yuta on 2016/04/14.
 */
@Setter
@Getter
public class CalendarItemModel {
    @SerializedName("date")
    private String date;
    @SerializedName("title")
    private String title;
}
