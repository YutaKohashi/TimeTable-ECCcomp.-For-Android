package jp.yuta.kohashi.esc.network.api.model.timeTable;

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
public class TeacherTimeTable implements Serializable {
    // 先生名字
    @SerializedName("first_name")
    String firstName;
    // 先生名前
    @SerializedName("family_name")
    String familyName;
    // 1: メイン 0: チューター
    @SerializedName("category")
    String  category;

    public TeacherTimeTable(String name){
        this.firstName = name;
        this.familyName = "";
        this.category = "1";
    }
}
