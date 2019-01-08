package com.wowo.android_lib_retrofit;

import android.app.Application;

import com.wowo.loglib.Logger;
import com.wowo.retrofitlib.RetrofitManager;

/**
 * Description.
 *
 * @author Lei.Jiang
 * @version [1.0.0]
 * @see [相关类]
 * @since [1.0.0]
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.setIsDebug(true);
        Logger.setTAG("testRetrofit");
        RetrofitManager.init(this, "http://financetest.wowoshenghuo.com/finance-web/", "000000");
    }
}
