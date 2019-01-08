package com.wowo.android_lib_retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.wowo.retrofitlib.RetrofitManager;
import com.wowo.retrofitlib.operation.HttpSubscriber;
import com.wowo.retrofitlib.response.CommonResponse;
import com.wowo.retrofitlib.transfer.HttpRxFun;
import com.wowo.retrofitlib.transfer.RxComposer;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

import static com.wowo.retrofitlib.transfer.RxComposer.applyIoSchedulers;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    private DisposableObserver mDisposableObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.test_txt);
        MyService service = RetrofitManager.getInstance().getDefaultRetrofit().create(MyService
                .class);
        HttpSubscriber<ArrayList<BarrageBean>> subscriber = new
                HttpSubscriber<ArrayList<BarrageBean>>() {

            @Override
            public void onPreExecute() {

            }

            @Override
            public void onNetworkUnavailable() {

            }

            @Override
            public void onResponseSuccess(ArrayList<BarrageBean> response) {
                StringBuilder builder = new StringBuilder();
                for (BarrageBean bean : response) {
                    builder.append(bean.getText())
                            .append("\n");
                }
                mTextView.setText(builder.toString());
            }

            @Override
            public void onResponseError(String des, String code) {

            }

            @Override
            public void onNetworkError() {

            }

            @Override
            public void onPostExecute() {

            }
        };
        mDisposableObserver = service.getList(RetrofitManager.getInstance().createHeaders())
                .flatMap(new HttpRxFun<ArrayList<BarrageBean>>())
                .compose(RxComposer.<ArrayList<BarrageBean>>applyIoSchedulers())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposableObserver != null && !mDisposableObserver.isDisposed()) {
            mDisposableObserver.dispose();
        }
    }
}
