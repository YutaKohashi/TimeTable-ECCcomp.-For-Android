package jp.yuta.kohashi.esc.network.api.model.schedule;

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
public class ScheduleRoot implements Serializable{
    @SerializedName("schedules")
    List<ScheduleCategory> schedules;

    @SerializedName("code")
    String code;

    @SerializedName("message")
    String message;
}
