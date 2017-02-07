package jp.yuta.kohashi.esc.ui.fragment.base;

import android.support.v4.app.Fragment;
import android.util.Log;

import jp.yuta.kohashi.esc.Const;
import jp.yuta.kohashi.esc.util.Util;
import jp.yuta.kohashi.esc.util.preference.PrefUtil;

/**
 * Created by yutakohashi on 2017/02/03.
 */

public class BaseFragment extends Fragment{

    protected boolean isUpdate(){
        final int THREE_HOUR = Const.SERVICE_INTERVAL_MILLISECONDS;
//        final long TWO_HOUR = 5000;
        boolean result = false;
        long diffTime = Util.getCurrentTimeMillis() - PrefUtil.getLatestUpdateDate();
        Log.d("ディフタイム", "difftime1 : " +  Util.getCurrentTimeMillis() );
        Log.d("ディフタイム", "difftime 2: " + PrefUtil.getLatestUpdateDate() );
        Log.d("ディフタイム", "Util.getCurrentTimeMillis() : " + Util.getCurrentTimeMillis() );
        if(diffTime > THREE_HOUR){
            result = true;
        }
        return result;
    }
}
