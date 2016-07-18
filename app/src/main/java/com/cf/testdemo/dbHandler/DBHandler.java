package com.cf.testdemo.dbHandler;

import android.content.Context;

import com.cf.testdemo.controller.CfApplicationController;


public class DBHandler {
	private String TAG = DBHandler.class.getSimpleName();
	private Context m_cContext;
	CfApplicationController m_cApplication;

	public DBHandler(Context pUIContext) {
		m_cContext = pUIContext;
		m_cApplication = (CfApplicationController) m_cContext.getApplicationContext();
	}

}
