package dong.lan.palmcure;

import android.app.Application;

import com.blankj.ALog;

import dong.lan.base.ui.base.SPHelper;

/**
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new ALog.Builder(this).setGlobalTag("LSR");
        SPHelper.instance().init(getApplicationContext(),"cure");

    }
}
