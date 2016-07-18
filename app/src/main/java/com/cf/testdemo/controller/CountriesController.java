package com.cf.testdemo.controller;

import android.os.Message;

import com.cf.testdemo.constants.ActionEvents;
import com.cf.testdemo.constants.ControllerState;
import com.cf.testdemo.model.CountriesModel;
import com.cf.testdemo.view.CountriesActivity;

/**
 * Created by arunkt on 01/03/16.
 */
public class CountriesController extends AbstractController {

    private String TAG = CountriesController.class.getSimpleName();

    public CountriesController(ControllerState state) {
        super(state);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    public CountriesActivity getActivity() {
        return (CountriesActivity) getCfApplicationController().getCurrentView();
    }

    public CountriesModel getCountriesModel(){
        return getCfApplicationModel().getCountriesModel();
    }

    @Override
    public void handleMessage(Message msg) {
        // TODO Auto-generated method stub
        if(!(getCfApplicationController().getCurrentView() instanceof CountriesActivity) || getActivity().isFinishing()){
            return;
        }

        switch (ActionEvents.values()[msg.what]) {
            case REQUEST_DB:
                if(getCountriesModel().getCountriesDataArrayList().size()<=0) {
                    getCfApplicationController().displayProgressBar("Please wait..");
                    getCountriesModel().executeHTTPConnection(ActionEvents.REQUEST_DB.ordinal());
                }
                break;
            case SUCCESS_DB:
                getCfApplicationController().stopProgressBar();
                getActivity().getCountriesListFragment().updateList();
                break;
            case FAILURE_DB:
                getCfApplicationController().stopProgressBar();
//                getActivity().showToast(getCountriesModel().getErrorMessage());
                break;
            case NETWORK_ISSUES:
                getCfApplicationController().stopProgressBar();
//                getActivity().showToast(R.string.networkerror);
                break;
            case PARAM_NULL:
                break;
            case ON_BACK:
                break;
        }
    }

}