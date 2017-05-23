package jp.yuta.kohashi.esc.network.api.model.schedule;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
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
    private List<ScheduleCategory> schedules;

    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    public ScheduleRoot(){
        schedules = new ArrayList<>();
        code = "";
        message  = "";
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        ScheduleRoot scheduleRoot = null;
        try{
            scheduleRoot = new ScheduleRoot();

        } catch (Exception e){

        }
        return scheduleRoot;
    }
}
