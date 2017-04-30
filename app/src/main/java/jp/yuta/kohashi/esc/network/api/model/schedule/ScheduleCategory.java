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
public class ScheduleCategory implements Serializable{
    /**
     * category_id": 1,
     "title": "全体予定"
     */
    @SerializedName("category_id")
    String category_id;
    @SerializedName("title")
    String title;
    @SerializedName("details")
    List<ScheduleItem> details;
}
