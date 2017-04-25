package jp.yuta.kohashi.esc.network.api.model.timeTable;

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

/**
 * JSONのrootに当たるオブジェクト
 */
@Getter
@Setter
public class RootTimeTable implements Serializable{
    @SerializedName("code")
    String code;
    @SerializedName("timetable")
    List<TimeTable> timeTables;
}
