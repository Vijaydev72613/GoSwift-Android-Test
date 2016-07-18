package com.cf.testdemo.controller;

import android.os.Handler;

import com.cf.testdemo.constants.ControllerState;
import com.cf.testdemo.model.CfApplicationModel;

public abstract class AbstractController extends Handler {

	/** state of the Controller */
	private ControllerState mState;

	public AbstractController(ControllerState state) {
		mState = state;
	}

	public ControllerState getState() {
		return mState;
	}

	/** Free all the references and Memory if allocated */
	public abstract void close();

	public CfApplicationController getCfApplicationController(){
		return CfApplicationController.getCfApplicationController();
	}

	public CfApplicationModel getCfApplicationModel(){
		return getCfApplicationController().getCfApplicationModel();
	}
}