package com.wowo.retrofitlib.exception;

/**
 * 网络请求异常.
 *
 * @author Lei.Jiang
 * @version [1.0.0]
 * @see [HttpResponseException]
 * @since [1.0.0]
 */
public class HttpResponseException extends RuntimeException {

    private String code;

    private String desc;

    public HttpResponseException(String code, String desc) {
        super(desc);
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
