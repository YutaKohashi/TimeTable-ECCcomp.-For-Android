package jp.yuta.kohashi.esc.network.api.model.news;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import jp.yuta.kohashi.esc.network.api.model.timeTable.TimeTable;
import lombok.Getter;
import lombok.Setter;

/**
 * Author : yutakohashi
 * Project name : ESC
 * Date : 10 / 04 / 2017
 */
@Getter
@Setter
public class RootNews {
    @SerializedName("code")
    String code;
    @SerializedName("notices")
    List<TimeTable> notices;
}
