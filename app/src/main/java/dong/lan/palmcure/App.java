package dong.lan.palmcure;

import android.app.Application;

import com.blankj.ALog;

import dong.lan.base.ui.base.SPHelper;

/**
 * Created by 梁桂栋 on 2017/4/21.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new ALog.Builder(this).setGlobalTag("LSR");
        SPHelper.instance().init(getApplicationContext(),"cure");

    }
}
