package com.cf.testdemo.model;

import com.cf.testdemo.controller.CfApplicationController;
import com.cf.testdemo.dbHandler.framework.OnlyOneDBHandler;
import com.cf.testdemo.dbHandler.tabels.CountriesTableHandler;
import com.cf.testdemo.servercommunication.RequestQueueTask;

public class CfApplicationModel {

	public CfApplicationController mCfApplicationController;
	private CountriesModel countriesModel = null;
	private RequestQueueTask requestQueueTask = null;

	public CfApplicationModel(CfApplicationController cfApplicationController) {
		mCfApplicationController = cfApplicationController;
	}


	public CountriesModel getCountriesModel() {
		if(countriesModel == null){
			countriesModel = new CountriesModel(this);
		}
		return countriesModel;
	}

	public void setCountriesModel(CountriesModel countriesModel) {
		this.countriesModel = countriesModel;
	}

	public RequestQueueTask getRequestQueueTask(){
		if(requestQueueTask == null){
			requestQueueTask = new RequestQueueTask();
		}
		return requestQueueTask;
	}

	//	private Bitmap mRecentBitmap = null;
//	private byte[] mRecentBitmapBytedata = null;

//	public Bitmap getRecentBitmap() {
//		return mRecentBitmap;
//	}
//
//	public void setRecentBitmap(Bitmap mRecentBitmap) {
//		this.mRecentBitmap = mRecentBitmap;
//	}
//
//	public byte[] getRecentBitmapBytedata() {
//		return mRecentBitmapBytedata;
//	}
//
//	public void setRecentBitmapBytedata(byte[] mRecentBitmapBytedata) {
//		this.mRecentBitmapBytedata = mRecentBitmapBytedata;
//	}


	public void readAllSavedData() {
	}

	public CfApplicationController getCfApplicationController() {
		if(mCfApplicationController == null){
			mCfApplicationController = CfApplicationController.getCfApplicationController();
		}
		return mCfApplicationController;
	}



	public void clearAllData() {
		deleteFileSystem();
		clearCacheData();
		clearDbData();
	}

	public OnlyOneDBHandler getOnlyOneDBHandler(){
		return getCfApplicationController().openOnlyOneDBHandler();
	}

	public CountriesTableHandler getLeadsFormTableHandler(){
		return CountriesTableHandler.getInstance(getOnlyOneDBHandler());
	}


	public void deleteFileSystem(){
//		FileExplorer.DeleteFolder(getCfApplicationController().getFileStreamPath(CFMacros.CF_FOLDER).getAbsolutePath()+"/Images");
	}

	public void clearCacheData() {

	}

	public void clearDbData() {

	}

}