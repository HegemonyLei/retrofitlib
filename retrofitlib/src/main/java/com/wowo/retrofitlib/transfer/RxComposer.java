package com.wowo.retrofitlib.transfer;

import com.wowo.retrofitlib.response.CommonResponse;
import com.wowo.retrofitlib.response.HttpResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 进程切换操作
 *
 * @author Lei.Jiang
 * @version [1.0.0]
 * @since [1.0.0]
 */
public class RxComposer {

    private static final ObservableTransformer ioTransformer = new ObservableTransformer() {

        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static <T> ObservableTransformer<HttpResponse<T>, HttpResponse<T>> applyIoSchedulers() {
        return (ObservableTransformer<HttpResponse<T>, HttpResponse<T>>) ioTransformer;
    }
}
