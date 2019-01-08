package com.wowo.android_lib_retrofit;

import com.wowo.retrofitlib.response.HttpResponse;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

/**
 * Description.
 *
 * @author Lei.Jiang
 * @version [1.0.0]
 * @see [相关类]
 * @since [1.0.0]
 */
public interface MyService {

    @POST("barrage/getList")
    Observable<HttpResponse<ArrayList<BarrageBean>>> getList(
            @HeaderMap Map<String, String> headers);
}
