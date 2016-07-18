package com.cf.testdemo.events;


import com.cf.testdemo.model.data.ResponseData;

/**
 * Created by arunkt on 20/06/16.
 */
public interface IRequestQueueTask {
    void onSuccess(ResponseData response);

    void onError(ResponseData response);

    void onNetworkError();

    void onCancelThread();

}
