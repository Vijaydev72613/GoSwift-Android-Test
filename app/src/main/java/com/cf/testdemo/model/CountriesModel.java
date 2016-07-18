package com.cf.testdemo.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.cf.testdemo.R;
import com.cf.testdemo.constants.ActionEvents;
import com.cf.testdemo.constants.CFMacros;
import com.cf.testdemo.constants.ControllerState;
import com.cf.testdemo.constants.HttpCommandId;
import com.cf.testdemo.controller.CountriesController;
import com.cf.testdemo.dbHandler.tabels.CountriesTableHandler;
import com.cf.testdemo.events.IHttpResponseListener;
import com.cf.testdemo.model.data.CountriesData;
import com.cf.testdemo.model.data.RequestData;
import com.cf.testdemo.model.data.ResponseData;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CountriesModel extends AbstractModel implements
		IHttpResponseListener {

	private String TAG = CountriesModel.class.getSimpleName();
	private int mPresentScreen = ActionEvents.COUNTRIES_LIST_FRAGMENT.ordinal();

	private boolean isTransition = false;
	private boolean onBackPressed = false;
	private String errorMessage = "";
	private ArrayList<CountriesData> countriesDataArrayList = null;
	private CountriesData countriesData = null;

  	public CountriesModel(CfApplicationModel cfApplicationModel) {
		super(cfApplicationModel);
		// TODO Auto-generated constructor stubs
		mPresentScreen = ActionEvents.COUNTRIES_LIST_FRAGMENT.ordinal();
		isTransition = false;
		onBackPressed = false;
    }

	public CountriesData getCountriesData() {
		if(countriesData == null){
			countriesData = new CountriesData();
		}
		return countriesData;
	}

	public void setCountriesData(CountriesData countriesData) {
		this.countriesData = countriesData;
	}

	public ArrayList<CountriesData> getCountriesDataArrayList() {
		if(countriesDataArrayList == null){
			countriesDataArrayList = new ArrayList<>();
		}
		return countriesDataArrayList;
	}

	public void setCountriesDataArrayList(ArrayList<CountriesData> countriesDataArrayList) {
		this.countriesDataArrayList = countriesDataArrayList;
	}

	public String getErrorMessage() {
		if(TextUtils.isEmpty(errorMessage) || errorMessage.equalsIgnoreCase("")){
			errorMessage = getCfApplicationController().getString(R.string.failuer);
		}
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isOnBackPressed() {
		return onBackPressed;
	}

	public void setOnBackPressed(boolean onBackPressed) {
		this.onBackPressed = onBackPressed;
	}

	public boolean isTransition() {
		return isTransition;
	}

	public void setIsTransition(boolean isTransition) {
		this.isTransition = isTransition;
	}


	public int getPresentScreen() {
		return mPresentScreen;
	}

	public void setPresentScreen(int presentScreen) {
		this.mPresentScreen = presentScreen;
	}

	@Override
	public void clearCacheData() {
		mPresentScreen = ActionEvents.COUNTRIES_LIST_FRAGMENT.ordinal();
		isTransition = false;
		onBackPressed = false;
	}

	public CountriesController getLeadsController(){
		return (CountriesController)getCfApplicationController().getController(ControllerState.COUNTRY_STATE);
	}

	public void clearDbData() {

	}

	public void executeHTTPConnection(int position){
		RequestData params = null;

		switch (ActionEvents.values()[position]) {

			case REQUEST_DB:
				params = getDBPostData();
				break;
		}

		if(params != null) {
			getCfApplicationModel().getRequestQueueTask().addHttpRequest(params);
		}else{
			getLeadsController().sendEmptyMessage(ActionEvents.PARAM_NULL.ordinal());
		}
	}

	public CountriesTableHandler getLeadsFormTableHandler(){
		return getCfApplicationModel().getLeadsFormTableHandler();
	}

//	public void saveLeadsData(){
//		String id  = UUID.randomUUID().toString();
//		getSelectedLeadsAddData().setLEADS_UUID(id);
//		String json = new Gson().toJson(getSelectedLeadsAddData());
//		ContentValues contentValues = new ContentValues();
//		contentValues.put(TablesList.LeadsFormBaseColumns.ID, id);
//		contentValues.put(TablesList.LeadsFormBaseColumns.leads_data,json);
//		getLeadsFormTableHandler().insertLeadsFormData(contentValues);
//		contentValues.clear();
//		contentValues = null;
////		executeHTTPConnection(ActionEvents.REQUEST_ADD_LEADS.ordinal());
//	}



	public RequestData getDBPostData(){
		RequestData params = new RequestData();
		params.setIHttpResponseListener(this);
		Uri.Builder builder = null;
		builder = Uri.parse("https://github.com/tknizam/goswiff-mobile-test/blob/master/data/GoSwiff.db?raw=true").buildUpon();
		params.setUrl(builder.build().toString());
		params.setrequestType("DOWNLOAD");
		params.setHttpCommandId(HttpCommandId.DB_HTTP_COMMAND);
		params.setUNIQUE_ID(HttpCommandId.DB_HTTP_COMMAND.toString());
		params.setHeader(CFMacros.HEADER_URL_ENCODER);
		return params;
	}



	@Override
	public void onSuccess(ResponseData response) {
		if(response !=null){
			switch(HttpCommandId.values()[response.mHttpCommandId.ordinal()]) {
				case DB_HTTP_COMMAND:
					parseLeadsAddResponse(response);
					break;
				default:
					getLeadsController().sendEmptyMessage(ActionEvents.NETWORK_ISSUES.ordinal());
					break;
			}
		}
		getCfApplicationModel().getRequestQueueTask().onSuccess(response);
	}

	@Override
	public void onError(ResponseData response) {
		// TODO Auto-generated method stub
		if(response != null){
			switch(HttpCommandId.values()[response.mHttpCommandId.ordinal()]){
				case DB_HTTP_COMMAND:
					getLeadsController().sendEmptyMessage(ActionEvents.NETWORK_ISSUES.ordinal());
					break;
				default:
					getLeadsController().sendEmptyMessage(ActionEvents.NETWORK_ISSUES.ordinal());
					break;
			}
		}
		getCfApplicationModel().getRequestQueueTask().onError(response);
	}

	@Override
	public void onNetworkError() {
		// TODO Auto-generated method stub
		getLeadsController().sendEmptyMessage(ActionEvents.NETWORK_ISSUES.ordinal());
		getCfApplicationModel().getRequestQueueTask().onNetworkError();
	}

	@Override
	public void onCancelThread() {
		getCfApplicationModel().getRequestQueueTask().onCancelThread();
	}

	public void parseLeadsAddResponse(ResponseData response){
		if(response!=null && response.getData()!=null) {
			SQLiteDatabase sqliteDB = SQLiteDatabase.openOrCreateDatabase(response.getData(), null);
			Cursor cursor = sqliteDB.rawQuery("SELECT * FROM countries", null);
			CountriesData countriesData = null;
			if (cursor != null) {
				setCountriesDataArrayList( new ArrayList<CountriesData>());
				for (int i = 0; i < cursor.getCount(); i++) {
					if (cursor.moveToPosition(i)) {
						countriesData = new CountriesData();
						countriesData.setId(cursor.getString(cursor.getColumnIndex("id")));
						countriesData.setCode3L(cursor.getString(cursor.getColumnIndex("code3L")));
						countriesData.setCode2L(cursor.getString(cursor.getColumnIndex("code2L")));
						countriesData.setName(cursor.getString(cursor.getColumnIndex("name")));
						countriesData.setName_official(cursor.getString(cursor.getColumnIndex("name_official")));
						countriesData.setFlag_32(cursor.getString(cursor.getColumnIndex("flag_32")));
						countriesData.setFlag_128(cursor.getString(cursor.getColumnIndex("flag_128")));
						countriesData.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
						countriesData.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
						getCountriesDataArrayList().add(countriesData);
						Log.d("ParseData", "countriesData "+ new Gson().toJson(countriesData,CountriesData.class));
					}
				}
			}
		}

		getLeadsController().sendEmptyMessage(ActionEvents.SUCCESS_DB.ordinal());
	}

}