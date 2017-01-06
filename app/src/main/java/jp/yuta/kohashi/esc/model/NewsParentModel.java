package jp.yuta.kohashi.esc.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yuta on 2016/06/15.
 */
@Getter
@Setter
public class NewsParentModel extends Object {

    private String title;
    private List<NewsChildModel> childItems;
}
