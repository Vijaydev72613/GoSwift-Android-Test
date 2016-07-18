package com.cf.testdemo.model.data;

import org.apache.http.HttpEntity;

import com.cf.testdemo.constants.HttpCommandId;
import com.cf.testdemo.events.IHttpResponseListener;

public class RequestData {

	public String url = "";
	public String token = "";
	public String requestType = "";
	public IHttpResponseListener iHttpResponseListener;
	public HttpCommandId mHttpCommandId;
	public HttpEntity httpEntity;
	public String header = "";
	public boolean isSetHeader = true;
	private  String UNIQUE_ID = "";
	
	public RequestData() {
		mHttpCommandId = HttpCommandId.Default;
		isSetHeader = true;
		UNIQUE_ID = "";
	}

	public String getUNIQUE_ID() {
		return UNIQUE_ID;
	}

	public void setUNIQUE_ID(String UNIQUE_ID) {
		this.UNIQUE_ID = UNIQUE_ID;
	}

	public boolean getIsSetHeader() {
		return isSetHeader;
	}

	public void setIsSetHeader(boolean isSetHeader) {
		this.isSetHeader = isSetHeader;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getHeader() {
		return header;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setrequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getrequestType() {
		return requestType;
	}

	public void setIHttpResponseListener(
			IHttpResponseListener iHttpResponseListener) {
		this.iHttpResponseListener = iHttpResponseListener;
	}

	public IHttpResponseListener getIHttpResponseListener() {
		return iHttpResponseListener;
	}

	public void setHttpCommandId(HttpCommandId httpCommandId) {
		mHttpCommandId = httpCommandId;
	}

	public HttpCommandId getHttpCommandId() {
		return mHttpCommandId;
	}

	public void setHttpEntity(HttpEntity httpEntity) {
		this.httpEntity = httpEntity;
	}

	public HttpEntity getHttpEntity() {
		return this.httpEntity;
	}
}
