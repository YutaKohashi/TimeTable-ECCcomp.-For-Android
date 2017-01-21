package jp.yuta.kohashi.esc.model.enums;

/**
 * Created by yutakohashi on 2017/01/17.
 */

/**
 * 設定画面のViewTypeを定義
 */
public enum PrefViewType {
    EMPTY(0),
    ITEM(1),
    ITEM_RIGHT_ARROW(2),
    ITEM_SWITCH(3),
    ITEM_CENTER_TXT(4),
    ITEM_GROUP_TITLE(5),
    ITEM_RIGHT_TXT(6),
    ITEM_COLOR_PICK(7);

    int id;
    PrefViewType(int i) {
        this.id = i;
    }

    public  int getId(){
        return id;
    }
}