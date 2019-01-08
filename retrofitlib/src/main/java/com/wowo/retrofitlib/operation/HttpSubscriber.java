package com.wowo.retrofitlib.operation;


import com.wowo.loglib.Logger;
import com.wowo.retrofitlib.RetrofitManager;
import com.wowo.retrofitlib.exception.HttpResponseException;
import com.wowo.retrofitlib.response.CommonResponse;
import com.wowo.retrofitlib.response.HttpResponse;
import com.wowo.retrofitlib.util.NetworkUtil;

import io.reactivex.observers.DisposableObserver;

/**
 * Description.
 *
 * @author Lei.Jiang
 * @version [1.0.0]
 * @see [相关类]
 * @since [1.0.0]
 */
public abstract class HttpSubscriber<T> extends DisposableObserver<HttpResponse<T>> {

    private boolean isNetworkUnavailable;

    @Override
    protected void onStart() {
        super.onStart();
        try {
            onPreExecute();
            if (NetworkUtil.isConnected(RetrofitManager.sApplicationContext)) {
                isNetworkUnavailable = false;
            } else {
                isNetworkUnavailable = true;
                onNetworkUnavailable();
                onPostExecute();
            }
        } catch (Exception e) {
            Logger.w("Start request error : [" + e.getMessage() + "]");
        }
    }

    @Override
    public void onComplete() {
        try {
            onPostExecute();
        } catch (Exception e) {
            Logger.w("Complete request error : [" + e.getMessage() + "]");
        }
    }

    @Override
    public void onError(Throwable e) {
        try {
            if (isNetworkUnavailable) {
                return;
            }
            if (e instanceof HttpResponseException) {
                HttpResponseException exception = (HttpResponseException) e;
                onResponseError(exception.getDesc(), exception.getCode());
            } else {
                onNetworkError();
            }
            onPostExecute();
        } catch (Exception error) {
            Logger.w(error.getMessage());
        }
    }

    @Override
    public void onNext(HttpResponse<T> t) {
        try {
            if (isNetworkUnavailable) {
                return;
            }
            onResponseSuccess(t.getData());
        } catch (Exception e) {
            onError(e);
        }
    }

    /**
     * 网络调用前的逻辑处理
     */
    public abstract void onPreExecute();

    /**
     * 无网络回调
     */
    public abstract void onNetworkUnavailable();

    /**
     * 获取返回的数据，并传递
     */
    public abstract void onResponseSuccess(T response);

    /**
     * 服务端返回错误信息
     */
    public abstract void onResponseError(String des, String code);

    /**
     * 网络错误
     */
    public abstract void onNetworkError();

    /**
     * 网络调用后的逻辑处理
     */
    public abstract void onPostExecute();

}
