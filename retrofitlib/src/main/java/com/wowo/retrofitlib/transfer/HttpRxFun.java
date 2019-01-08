package com.wowo.retrofitlib.transfer;

import com.wowo.retrofitlib.RetrofitManager;
import com.wowo.retrofitlib.exception.HttpResponseException;
import com.wowo.retrofitlib.response.CommonResponse;
import com.wowo.retrofitlib.response.EmptyResponseBean;
import com.wowo.retrofitlib.response.HttpResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 通用转化返回类
 *
 * @author Lei.Jiang
 * @version [1.0.0]
 * @since [1.0.0]
 */
public class HttpRxFun<T> implements Function<HttpResponse<T>,
        ObservableSource<HttpResponse<T>>> {

    @Override
    public ObservableSource<HttpResponse<T>> apply(HttpResponse<T> tCommonHttpResponse) throws Exception {
        String code = tCommonHttpResponse.getStatus();
        String desc = tCommonHttpResponse.getMessage();
        if (!RetrofitManager.sHttpOkCode.equals(code)) {
            throw new HttpResponseException(code, desc);
        }
        return Observable.just(tCommonHttpResponse);
    }
}
