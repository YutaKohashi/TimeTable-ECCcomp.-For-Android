package jp.yuta.kohashi.esc.network.api.model.schedule;

import com.google.gson.annotations.SerializedName;

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
public class RootSchedule {
    @SerializedName("schedules")
    List<RowSchedule> schedules;
}
