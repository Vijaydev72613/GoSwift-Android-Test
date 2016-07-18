package com.cf.testdemo.servercommunication;

import android.os.AsyncTask;

import com.cf.testdemo.model.data.RequestData;
import com.cf.testdemo.model.data.ResponseData;

/**
 * Created by arunkt on 13/06/16.
 */
public class AsyncHttpTask extends AsyncTask<RequestData, Integer, ResponseData> {

    public AsyncHttpTask() {
        // TODO Auto-generated constructor stub
    }


    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }

    @Override
    protected ResponseData doInBackground(RequestData... params) {
        // TODO Auto-generated method stub
        HTTPTransport httpTransport = new HTTPTransport(params[0]);
        if (params[0].requestType.equalsIgnoreCase("GET")) {
            httpTransport.doHttpGetRequest(params[0]);
        } else if(params[0].requestType.equalsIgnoreCase("POST")){
            httpTransport.doHttpPostRequest(params[0]);
        }else if(params[0].requestType.equalsIgnoreCase("DOWNLOAD")){
            httpTransport.doHttpDownloadRequest(params[0]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResponseData result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

    @Override
    protected void onProgressUpdate(Integer... arg0) {
        // TODO Auto-generated method stub
        super.onProgressUpdate(arg0);
    }

    @Override
    protected void onCancelled() {
        // TODO Auto-generated method stub
        super.onCancelled();
    }

}