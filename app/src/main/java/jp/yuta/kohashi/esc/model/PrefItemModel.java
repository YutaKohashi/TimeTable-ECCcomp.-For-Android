package jp.yuta.kohashi.esc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by yutakohashi on 2017/01/17.
 */

@Setter
@Getter
public class PrefItemModel {
    private String itemName;
    private int resourceId;
    private ViewType viewType;

    public enum ViewType{
        EMPTY,
        ITEM,
        ITEM_CENTER_TXT,
        ITEM_GROUP_TITLE;
    }

    public PrefItemModel(String itemName, Integer resourceId,ViewType viewType){
        this.itemName = itemName;
        this.resourceId = resourceId;
        this.viewType = viewType;
    }

    public PrefItemModel(String itemName, ViewType viewType){
        this.itemName = itemName;
        this.viewType = viewType;
    }

    public PrefItemModel(ViewType viewType){
        this.viewType = viewType;
    }
}
