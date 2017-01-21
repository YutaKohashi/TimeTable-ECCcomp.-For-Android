package jp.yuta.kohashi.esc.model;

import jp.yuta.kohashi.esc.model.enums.PrefViewType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by yutakohashi on 2017/01/17.
 */

@Setter
@Getter
public class PrefItemModel {
    private String itemName;
    private String rightText;
    private int resourceId;
    private PrefViewType viewType;
    private boolean bool;
    private int color;

    public PrefItemModel(String itemName,Integer resourceId,PrefViewType viewType,boolean bool){
        this.itemName = itemName;
        this.resourceId = resourceId;
        this.viewType = viewType;
        this.bool = bool;
    }

    public PrefItemModel(String itemName, Integer resourceId,PrefViewType viewType){
        this.itemName = itemName;
        this.resourceId = resourceId;
        this.viewType = viewType;
    }

    public PrefItemModel(String itemName, Integer resourceId,PrefViewType viewType,int color){
        this.itemName = itemName;
        this.resourceId = resourceId;
        this.viewType = viewType;
        this.color = color;
    }

    public PrefItemModel(String itemName,String rightText ,Integer resourceId,PrefViewType viewType){
        this.itemName = itemName;
        this.rightText = rightText;
        this.resourceId = resourceId;
        this.viewType = viewType;
    }

    public PrefItemModel(String itemName, PrefViewType viewType){
        this.itemName = itemName;
        this.viewType = viewType;
    }

    public PrefItemModel(PrefViewType viewType){
        this.viewType = viewType;
    }


}
