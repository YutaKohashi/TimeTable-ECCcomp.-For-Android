package jp.yuta.kohashi.esc.network.api.model.schedule;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Author : yutakohashi
 * Project name : ESC
 * Date : 10 / 04 / 2017
 */
@Getter
@Setter
public class ScheduleItem implements Serializable{
    @SerializedName("date")
    String date;
    @SerializedName("year")
    Integer year;
    @SerializedName("month")
    Integer month;
    @SerializedName("day")
    Integer day;
    @SerializedName("week")
    Integer week;

    /**
     * 内容
     */
    @SerializedName("body")
    String body;
}
