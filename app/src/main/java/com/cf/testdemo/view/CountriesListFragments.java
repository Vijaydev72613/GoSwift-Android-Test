package com.cf.testdemo.view;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cf.testdemo.R;
import com.cf.testdemo.constants.ActionEvents;
import com.cf.testdemo.constants.ControllerState;
import com.cf.testdemo.constants.PermissionEvents;
import com.cf.testdemo.controller.CfApplicationController;
import com.cf.testdemo.controller.CountriesController;
import com.cf.testdemo.events.IFragmentListener;
import com.cf.testdemo.model.CountriesModel;
import com.cf.testdemo.model.data.CountriesData;
import com.cf.testdemo.viewadapter.CountriesListAdapter;

/**
 * Created by arunkt on 22/06/16.
 */
public class CountriesListFragments extends Fragment implements View.OnClickListener , SearchView.OnQueryTextListener{

    private IFragmentListener mIRegisterationListener =null;
    private ListView listView;
    private TextView infoTextView = null;
    private CountriesListAdapter countriesAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.countries_list_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView  = (ListView) view.findViewById(R.id.list);
        infoTextView  = (TextView) view.findViewById(R.id.info_textView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0 && position < countriesAdapter.getCount()) {
                    if(getCfApplicationController().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, PermissionEvents.ACCESS_FINE_LOCATION.ordinal())
                            || getCfApplicationController().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, PermissionEvents.ACCESS_COARSE_LOCATION.ordinal())) {
                        CountriesData agentsData =  countriesAdapter.getItem(position);
                        getCountriesModel().setCountriesData(agentsData);
                        getCountriesActivity().setView(ActionEvents.COUNTRY_VIEW_FRAGMENT.ordinal());
                    }
                }
            }
        });

        updateList();
    }

    @Override
    public void onResume() {
        super.onResume();
        getCountriesController().sendEmptyMessage(ActionEvents.REQUEST_DB.ordinal());
    }

    private CountriesActivity getCountriesActivity(){
        return (CountriesActivity)getActivity();
    }

    public void updateList(){
        countriesAdapter = new CountriesListAdapter(getCountriesActivity(), getCountriesModel().getCountriesDataArrayList());
        listView.setAdapter(countriesAdapter);
        countriesAdapter.notifyDataSetChanged();
    }


    public CfApplicationController getCfApplicationController() {
        return CfApplicationController.getCfApplicationController();
    }

    private CountriesModel getCountriesModel(){
        return getCountriesController().getCountriesModel();
    }

    public CountriesController getCountriesController(){
        return (CountriesController)getCfApplicationController().getController(ControllerState.COUNTRY_STATE);
    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int msgId) {
        Toast.makeText(getActivity(), msgId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mIRegisterationListener = (IFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+ " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.search:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.countries_list_menu, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(countriesAdapter!=null){
            countriesAdapter.getFilter().filter(newText);
        }
        return false;
    }

}
