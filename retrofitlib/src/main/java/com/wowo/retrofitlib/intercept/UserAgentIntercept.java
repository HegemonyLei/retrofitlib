package com.wowo.retrofitlib.intercept;

import com.wowo.retrofitlib.header.DefaultHeader;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * UserAgent拦截.
 *
 * @author Lei.Jiang
 * @version [1.0.0]
 * @since [1.0.0]
 */
public class UserAgentIntercept implements Interceptor {

    private static final String USER_AGENT_HEADER_NAME = "User-Agent";

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originalRequest = chain.request();
        final Request requestWithUserAgent = originalRequest.newBuilder()
                .removeHeader(USER_AGENT_HEADER_NAME)
                .addHeader(USER_AGENT_HEADER_NAME, DefaultHeader.getUserAgent())
                .build();
        return chain.proceed(requestWithUserAgent);
    }
}
