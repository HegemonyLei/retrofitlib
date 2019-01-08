package com.wowo.retrofitlib.intercept;

import com.wowo.loglib.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 网络日志拦截打印.
 *
 * @author Lei.Jiang
 * @version [1.0.0]
 * @since [1.0.0]
 */
public class HttpLogInterceptor implements Interceptor {

    private final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;

        Headers headers = request.headers();
        StringBuilder sBuilder = new StringBuilder("Headers:\n");
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            // Skip headers from the request body as they are explicitly logged above.
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(
                    name)) {
                sBuilder.append(name);
                sBuilder.append(" : ");
                sBuilder.append(headers.value(i));
                sBuilder.append("\n");
            }
        }
        StringBuilder body = bodyToString(request);
        if (hasRequestBody) {
            if (requestBody.contentType() != null) {
                if (isPlaintext(requestBody.contentType())) {
                    Logger.json("--> Start " + request.method() + " " + request.url() + " " + protocol
                            + "\nContent-Type : " + requestBody.contentType()
                            + "\n" + sBuilder.toString()
                            + "Content-Length : " + requestBody.contentLength()
                            + "\n" + body.toString() + "\n--> END " + request.method());
                } else {
                    Logger.json("--> Start " + request.method() + " " + request.url() + " " + protocol
                            + "\nContent-Type : " + requestBody.contentType()
                            + "\n" + sBuilder.toString()
                            + "Content-Length : " + requestBody.contentLength()
                            + "\nbody : maybe [file part] , too large too print , ignored!"
                            + "\n--> END " + request.method());
                }

            } else {
                Logger.json("--> Start " + request.method() + " " + request.url() + " " + protocol
                        + "\n" + sBuilder.toString()
                        + body.toString() + "\n--> END " + request.method());
            }
        } else {
            Logger.json("--> Start " + request.method() + " " + request.url() + " " + protocol +
                    "\n" + sBuilder.toString()
                    + body.toString() + "\n--> END " + request.method());
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            Logger.e("Request failed, throw error = [" + e.getMessage() + "]");
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException e) {
                Logger.e("End request, couldn't decode the response body, charset is likely "
                        + "malformed.");
                return response;
            }
        }
        if (!isPlaintext(buffer)) {
            return response;
        }

        if (contentLength != 0 && charset != null) {
            Logger.json("<-- " + response.code() + " " + request.url() + " [" + tookMs + "ms]\n"
                    + "body : " + buffer.clone().readString(charset) + "\n<-- END");
        }
        return response;
    }

    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small
     * sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private static boolean isPlaintext(MediaType mediaType) {
        if (mediaType == null) return false;
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        String subtype = mediaType.subtype();
        if (subtype != null) {
            subtype = subtype.toLowerCase();
            if (subtype.contains("x-www-form-urlencoded") ||
                    subtype.contains("json") ||
                    subtype.contains("xml") ||
                    subtype.contains("html")) //
            {
                return true;
            }
        }
        return false;
    }

    private StringBuilder bodyToString(Request request) {
        StringBuilder body = new StringBuilder("body:");
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = copy.body().contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            body.append(buffer.readString(charset));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }
}
