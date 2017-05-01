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
    private String date;
    @SerializedName("year")
    private Integer year;
    @SerializedName("month")
    private Integer month;
    @SerializedName("day")
    private Integer day;
    @SerializedName("week")
    private Integer week;

    /**
     * 内容
     */
    @SerializedName("body")
    private String body;

    public ScheduleItem(){
        date = "";
        year = 0;
        month = 0;
        day = 0;
        week = 0;
        body = "";
    }
}
