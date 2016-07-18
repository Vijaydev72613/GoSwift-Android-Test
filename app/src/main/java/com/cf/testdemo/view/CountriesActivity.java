package com.cf.testdemo.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cf.testdemo.R;
import com.cf.testdemo.constants.ActionEvents;
import com.cf.testdemo.constants.ControllerState;
import com.cf.testdemo.controller.CfApplicationController;
import com.cf.testdemo.controller.CountriesController;
import com.cf.testdemo.events.IFragmentListener;
import com.cf.testdemo.events.IViewListener;
import com.cf.testdemo.model.CountriesModel;

/**
 * Created by arunkt on 16/06/16.
 */
public class CountriesActivity extends AppCompatActivity implements IFragmentListener,
        IViewListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private String TAG = CountriesActivity.class.getSimpleName();

    private CoordinatorLayout mCoordinatorLayout = null;
    private ActionBar actionBar = null;
    private Toolbar toolbar = null;

    private CountriesListFragments countriesListFragment = null;
    private CountryDetailsFragment countryDetailsFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leads_activity);
        initviewcontroller(ControllerState.COUNTRY_STATE);
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.SnackBarCoordinatorLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        actionBar = this.getSupportActionBar();

        actionBar.setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(
                        R.color.ColorPrimary)));
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setTitle("");

        getLeadsModel().setIsTransition(false);
        setView(getLeadsModel().getPresentScreen());
    }


    @Override
    public void setTitle(CharSequence title) {
        actionBar.setTitle("  " + title);
    }


    private CountriesModel getLeadsModel(){
        return getLeadsController().getCountriesModel();
    }

    public CountriesController getLeadsController(){
        return (CountriesController)getCfApplicationController().getController(ControllerState.COUNTRY_STATE);
    }


    public CountriesListFragments getCountriesListFragment(){
        if(countriesListFragment ==null){
            countriesListFragment = new CountriesListFragments();
        }
        return countriesListFragment;
    }

    public CountryDetailsFragment getCountryDetailsFragment(){
//        if(countryDetailsFragment ==null){
            countryDetailsFragment = new CountryDetailsFragment();
//        }
        return countryDetailsFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }



    public void setView(int position) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(getLeadsModel().isTransition()) {
            if (getLeadsModel().isOnBackPressed()) {
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
            } else {
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }else{
            getLeadsModel().setIsTransition(true);
        }

        getLeadsModel().setPresentScreen(position);

        String title = "";
        switch (ActionEvents.values()[position]) {
            case COUNTRIES_LIST_FRAGMENT:
                title ="Countries";
                transaction.replace(R.id.content_frame, getCountriesListFragment()).commit();
                break;
            case COUNTRY_VIEW_FRAGMENT:
                title ="Country Details";
                transaction.replace(R.id.content_frame, getCountryDetailsFragment()).commit();
                break;
            default:
                break;
        }

        setTitle(title);
    }

    @Override
    public void onBackPressed() {
        getLeadsModel().setOnBackPressed(true);
        switch (ActionEvents.values()[getLeadsModel().getPresentScreen()]) {
            case COUNTRIES_LIST_FRAGMENT:
                getLeadsController().sendEmptyMessage(ActionEvents.ON_BACK.ordinal());
                break;
            case COUNTRY_VIEW_FRAGMENT:
                setView(ActionEvents.COUNTRIES_LIST_FRAGMENT.ordinal());
                break;
            default:
                getLeadsController().sendEmptyMessage(ActionEvents.ON_BACK.ordinal());
                break;
        }
        getLeadsModel().setOnBackPressed(false);
    }

    public CfApplicationController getCfApplicationController() {
        return CfApplicationController.getCfApplicationController();
    }

    @Override
    public void initviewcontroller(ControllerState state) {
        // TODO Auto-generated method stub
        getCfApplicationController().initCurrentControllerView(state, this);
    }



    public void showToast(int msgId) {
        Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK == resultCode){

        }
    }

    public void showSnackbar(String msg){

        Snackbar snackbar = Snackbar
                .make(mCoordinatorLayout,msg, Snackbar.LENGTH_LONG)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

//        snackbar.setActionTextColor(Color.RED);
        snackbar.setActionTextColor(Color.parseColor("#1ABFDF"));

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        getCfApplicationController().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
