package com.wowo.retrofitlib.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * description.
 *
 * @author Lei.Jiang
 * @version [1.0.0]
 * @since [1.0.0]
 */
public class ProgressHelper {

    public static OkHttpClient addProgressResponseListener(OkHttpClient client,
                                                           final ProgressResponseListener listener) {
        OkHttpClient.Builder mBuilder = client.newBuilder();
        mBuilder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), listener)).build();
            }
        });
        return mBuilder.build();
    }

}
