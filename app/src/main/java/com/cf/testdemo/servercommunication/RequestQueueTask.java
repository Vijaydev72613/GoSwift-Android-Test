package com.cf.testdemo.servercommunication;

import com.cf.testdemo.events.IRequestQueueTask;
import com.cf.testdemo.model.data.RequestData;
import com.cf.testdemo.model.data.ResponseData;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by arunkt on 20/06/16.
 */
public class RequestQueueTask implements IRequestQueueTask {

    private Hashtable httpRequestsHashtable = null;
    private AsyncHttpTask mAsyncHttpTask = null;

    public RequestQueueTask() {

    }

    @Override
    public void onSuccess(ResponseData response) {
        // TODO Auto-generated method stub
        removeHttprequest(response);
        onCancelThread();
    }

    @Override
    public void onError(ResponseData response) {
        // TODO Auto-generated method stub
        removeHttprequest(response);
        onCancelThread();
    }

    @Override
    public void onNetworkError() {
        // TODO Auto-generated method stub
        onCancelThread();
    }

    @Override
    public void onCancelThread() {
        if (mAsyncHttpTask != null && !mAsyncHttpTask.isCancelled()) {
            mAsyncHttpTask.cancel(true);
            mAsyncHttpTask = null;
        }

        startHttpRequest();
    }

    private Hashtable getHttpRequestsHashtable(){
        if(httpRequestsHashtable == null){
            httpRequestsHashtable = new Hashtable();
        }
        return httpRequestsHashtable;
    }

    private synchronized boolean execute(RequestData requestData){
        if(mAsyncHttpTask == null) {
            mAsyncHttpTask = new AsyncHttpTask();
            mAsyncHttpTask.execute(requestData);
            return true;
        }
        return false;
    }

    public synchronized void addHttpRequest(RequestData requestData){
        if(!getHttpRequestsHashtable().containsKey(requestData.getUNIQUE_ID())) {
            getHttpRequestsHashtable().put(requestData.getUNIQUE_ID(),requestData);
        }
        startHttpRequest();
    }

    private synchronized void startHttpRequest(){
        if(getHttpRequestsHashtable().size()>0){
            Enumeration enu = getHttpRequestsHashtable().keys();
            while (enu.hasMoreElements()) {
                RequestData requestData = (RequestData) getHttpRequestsHashtable().get(enu.nextElement());
                if(execute(requestData)){
                    break;
                }
            }
        }
    }

    private synchronized void removeHttprequest(ResponseData responseData){
        if(getHttpRequestsHashtable().size()>0) {
            if(getHttpRequestsHashtable().containsKey(responseData.getUNIQUE_ID())) {
                getHttpRequestsHashtable().remove(responseData.getUNIQUE_ID());
                startHttpRequest();
            }
        }
    }

    public void clearHttpRequest(){
        if(getHttpRequestsHashtable().size()>0) {
            getHttpRequestsHashtable().clear();
        }
    }

}
