package com.wowo.retrofitlib.response;

/**
 * 网络请求列表返回.
 *
 * @author Lei.Jiang
 * @version [1.0.0]
 * @see [HttpPageResponse]
 * @since [1.0.0]
 */
public class HttpPageResponse<T> {

    /**
     * 接口返回操作码
     */
    String status;

    /**
     * 接口返回描述
     */
    String message;

    /**
     * 接口返回具体数据
     */
    HttpListResponse<T> data;

    /**
     * 接口返回时间
     */
    String timestamp;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpListResponse<T> getData() {
        return data;
    }

    public void setData(HttpListResponse<T> data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
