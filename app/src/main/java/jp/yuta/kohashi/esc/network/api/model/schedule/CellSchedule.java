package jp.yuta.kohashi.esc.network.api.model.schedule;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Author : yutakohashi
 * Project name : ESC
 * Date : 10 / 04 / 2017
 */
@Getter
@Setter
public class CellSchedule {
    @SerializedName("date")
    String date;
    @SerializedName("year")
    String year;
    @SerializedName("month")
    String month;
    @SerializedName("day")
    String day;
    @SerializedName("week")
    String week;
    @SerializedName("body")
    String body;
}
