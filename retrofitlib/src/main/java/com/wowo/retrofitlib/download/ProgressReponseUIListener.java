package com.wowo.retrofitlib.download;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * description.
 *
 * @author Lei.Jiang
 * @version [1.0.0]
 * @since [1.0.0]
 */
public abstract class ProgressReponseUIListener implements ProgressResponseListener {
    private static final int RESPONSE_UPDATE = 0x02;

    private static class UIHandler extends Handler {
        private final WeakReference<ProgressReponseUIListener> mUIListenerWeakReference;

        public UIHandler(Looper looper, ProgressReponseUIListener progressResponseUIListener) {
            super(looper);
            mUIListenerWeakReference = new WeakReference<ProgressReponseUIListener>(
                    progressResponseUIListener);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RESPONSE_UPDATE:
                    ProgressReponseUIListener uiProgressResponseListener =
                            mUIListenerWeakReference.get();
                    if (uiProgressResponseListener != null) {
                        //获得进度实体类
                        ProgressModel progressModel = (ProgressModel) msg.obj;
                        //回调抽象方法
                        uiProgressResponseListener.onUIResponseProgress(
                                progressModel.getCurrentBytes(), progressModel.getContentLength(),
                                progressModel.isDone());
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }

        }

    }

    private final Handler mHandler = new UIHandler(Looper.myLooper(), this);

    @Override
    public void onResponseProgress(long byteRead, long contentLength, boolean done) {
        Message msg = Message.obtain();
        msg.obj = new ProgressModel(byteRead, contentLength, done);
        msg.what = RESPONSE_UPDATE;
        mHandler.sendMessage(msg);
    }

    public abstract void onUIResponseProgress(long bytesRead, long contentLength, boolean done);

}
