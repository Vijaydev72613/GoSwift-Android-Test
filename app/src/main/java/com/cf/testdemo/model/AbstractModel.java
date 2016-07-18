package com.cf.testdemo.model;

import com.cf.testdemo.controller.CfApplicationController;

public abstract class AbstractModel {

	public CfApplicationModel mCfApplicationModel;
	
	public AbstractModel(CfApplicationModel cfApplicationModel) {
		mCfApplicationModel = cfApplicationModel;
	}

	public CfApplicationModel getCfApplicationModel() {
		return mCfApplicationModel;
	}

	public CfApplicationController getCfApplicationController(){
		return mCfApplicationModel.getCfApplicationController();
	}


	public abstract void clearCacheData();
	public abstract void clearDbData();
}