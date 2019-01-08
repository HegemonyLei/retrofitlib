package com.wowo.retrofitlib.download;

/**
 * description.
 *
 * @author Lei.Jiang
 * @version [1.0.0]
 * @since [1.0.0]
 */
public interface ProgressResponseListener {
    void onResponseProgress(long byteRead, long contentLength, boolean done);
}
