package jp.yuta.kohashi.esc.model.enums;

/**
 * Created by yutakohashi on 2017/01/24.
 */

public enum AttendanceRateType {
    ALL(0),//　フラグメントで使用
    ZENKI(1), //　デフォルト値
    KOUKI(2);


    private int id;

    AttendanceRateType(int i) {
        this.id = i;
    }

    public int getId() {
        return id;
    }
}

