package com.wowo.retrofitlib;

import android.content.Context;

import com.wowo.retrofitlib.intercept.HttpLogInterceptor;
import com.wowo.retrofitlib.intercept.UserAgentIntercept;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求管理，控制网络请求参数
 *
 * @author Lei.Jiang
 * @version [1.0.0]
 * @since [1.0.0]
 */
public class RetrofitManager {

    private static final int DEFAULT_TIME_OUT = 20;

    private final static String FLAG_HEADER_TIME = "timestamp";

    private HashMap<String, String> headers = new HashMap<>();

    private OkHttpClient.Builder clientBuilder;
    private OkHttpClient mDefaultClient;
    private OkHttpClient mFixTimeOutClient;

    private final Retrofit.Builder retrofitBuilder;

    public static Context sApplicationContext;

    public static String sBaseUrl;

    public static String sHttpOkCode;

    public static class SingletonHolder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    public static void init(Context applicationContext, String baseUrl, String code) {
        sApplicationContext = applicationContext;
        sBaseUrl = baseUrl;
        sHttpOkCode = code;
    }

    private RetrofitManager() {
        clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        retrofitBuilder = new Retrofit.Builder();
        initLog();
        initUserAgent();
    }

    public static RetrofitManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取默认的Retrofit对象，20s默认的连接超时时间
     *
     * @return 默认的Retrofit对象
     */
    public Retrofit getDefaultRetrofit() {
        if (mDefaultClient == null) {
            clientBuilder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
            mDefaultClient = clientBuilder.build();
        }
        return retrofitBuilder.baseUrl(sBaseUrl)
                .client(mDefaultClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 获取特定超时时间的Retrofit
     *
     * @param timeout 自定义的超时时间
     * @return Retrofit对象
     */
    public Retrofit getFixedTimeoutRetrofit(String baseUrl, int timeout) {
        if (mFixTimeOutClient == null) {
            clientBuilder.connectTimeout(timeout, TimeUnit.SECONDS);
            mFixTimeOutClient = clientBuilder.build();
        }
        return retrofitBuilder.baseUrl(baseUrl)
                .client(mFixTimeOutClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 获取特定URL的Retrofit 超时时间默认为20s
     *
     * @param baseUrl 指定的URL
     * @return Retrofit对象
     */
    public Retrofit getSpecialUrlRetrofit(String baseUrl) {
        if (mDefaultClient == null) {
            clientBuilder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
            mDefaultClient = clientBuilder.build();
        }
        return retrofitBuilder.baseUrl(baseUrl)
                .client(mDefaultClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    /**
     * 添加请求头
     *
     * @param key   添加请求头的key
     * @param value 添加请求头的value
     */
    public void addHeader(String key, String value) {
        if (key == null) {
            return;
        }
        headers.put(key, value);
    }

    /**
     * 设置请求头
     *
     * @param headers 请求头的map
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = (HashMap<String, String>) headers;
    }

    /**
     * 移除某个请求头
     *
     * @param key 请求头的key值
     */
    public void removeHeader(String key) {
        if (key == null) {
            return;
        }
        if (headers.containsKey(key)) {
            headers.remove(key);
        }
    }

    private void syncRequestHeader() {
        if (clientBuilder == null) {
            clientBuilder = new OkHttpClient.Builder();
        }
        if (headers.size() > 0) {
            clientBuilder.addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request.Builder builder = request.newBuilder();
                    builder.headers(Headers.of(headers));
                    request = builder.build();
                    return chain.proceed(request);
                }
            });
        }
    }

    /**
     * 创建头部，返回之前添加的以及当前时间戳
     *
     * @return 头域
     */
    public Map<String, String> createHeaders() {
        headers.put(FLAG_HEADER_TIME, String.valueOf(System.currentTimeMillis()));
        return headers;
    }

    /**
     * @return 头部字段
     */
    public Map<String, String> getCloneHeaders() {
        headers.put(FLAG_HEADER_TIME, String.valueOf(System.currentTimeMillis()));
        return (Map<String, String>) headers.clone();
    }

    /**
     * 获取OkHttpClient
     */
    public OkHttpClient getOkHttpClient() {
        if (mDefaultClient == null) {
            clientBuilder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
            mDefaultClient = clientBuilder.build();
        }
        return mDefaultClient;
    }

    private void initLog() {
        clientBuilder.addInterceptor(new HttpLogInterceptor());
    }

    private void initUserAgent() {
        clientBuilder.addInterceptor(new UserAgentIntercept());
    }
}
