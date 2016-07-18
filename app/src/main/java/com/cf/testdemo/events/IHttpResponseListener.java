package com.cf.testdemo.events;

import com.cf.testdemo.model.data.ResponseData;

public interface IHttpResponseListener
{

	void executeHTTPConnection( int requestPonit);

	void onSuccess(ResponseData response);

	void onError(ResponseData response);
	
	void onNetworkError();

	void onCancelThread();
}
