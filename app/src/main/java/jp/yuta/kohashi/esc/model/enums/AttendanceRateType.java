package jp.yuta.kohashi.esc.model.enums;

/**
 * Created by yutakohashi on 2017/01/24.
 */

public enum AttendanceRateType {
    ZENKI(0),
    KOUKI(1);

    private int id;
    AttendanceRateType(int i) {
        this.id = i;
    }

    public  int getId(){
        return id;
    }
}

