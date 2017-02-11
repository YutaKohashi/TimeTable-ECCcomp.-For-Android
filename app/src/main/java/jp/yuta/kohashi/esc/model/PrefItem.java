package jp.yuta.kohashi.esc.model;

import jp.yuta.kohashi.esc.model.enums.PrefViewType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by yutakohashi on 2017/01/17.
 */

@Setter
@Getter
public class PrefItem {
    private String itemName;
    private String rightText;
    private int resourceId;
    private PrefViewType viewType;
    private boolean bool;
    private int color;
    private int start;
    private int end;
    private Integer tag;

    public PrefItem(PrefViewType viewType) {
        this.viewType = viewType;
    }

    public PrefItem(String itemName, PrefViewType viewType) {
        this(itemName, -1, viewType);
    }

    public PrefItem(String itemName, Integer resourceId, PrefViewType viewType) {
        this.itemName = itemName;
        this.resourceId = resourceId;
        this.viewType = viewType;
    }

    public PrefItem(String itemName, Integer resourceId, PrefViewType viewType, boolean bool) {
        this(itemName, resourceId, viewType);
        this.bool = bool;
    }

    public PrefItem(String itemName, String rightText, Integer resourceId, PrefViewType viewType) {
        this(itemName, resourceId, viewType);
        this.rightText = rightText;
    }

    public PrefItem(String itemName, Integer resourceId, PrefViewType viewType, int color) {
        this(itemName, resourceId, viewType);
        this.color = color;
    }

    public PrefItem(String itemName, Integer resourceId, PrefViewType viewType, int color, Integer tag,int start, int end) {
        this(itemName, resourceId, viewType, color);
        this.start = start;
        this.end = end;
        this.tag = tag;
    }
}
