package com.cf.testdemo.model.data;

import com.cf.testdemo.constants.HttpCommandId;

public class ResponseData {
	private String response;
	private boolean status;
	public HttpCommandId mHttpCommandId;
	private  String UNIQUE_ID = "";

	public ResponseData() {
		mHttpCommandId = HttpCommandId.Default;
		UNIQUE_ID = "";
	}

	public String getUNIQUE_ID() {
		return UNIQUE_ID;
	}

	public void setUNIQUE_ID(String UNIQUE_ID) {
		this.UNIQUE_ID = UNIQUE_ID;
	}

	public String getData() {
		return response;
	}

	public void setData(String response) {
		this.response = response;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setHttpCommandId(HttpCommandId httpCommandId) {
		mHttpCommandId = httpCommandId;
	}

	public HttpCommandId getHttpCommandId() {
		return mHttpCommandId;
	}
}
