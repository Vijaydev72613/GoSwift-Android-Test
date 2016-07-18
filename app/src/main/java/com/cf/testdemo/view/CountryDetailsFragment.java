package com.cf.testdemo.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cf.testdemo.R;
import com.cf.testdemo.constants.ControllerState;
import com.cf.testdemo.controller.CfApplicationController;
import com.cf.testdemo.controller.CountriesController;
import com.cf.testdemo.events.IFragmentListener;
import com.cf.testdemo.model.CountriesModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

/**
 * Created by arunkt on 12/07/16.
 */
public class CountryDetailsFragment extends Fragment {

    private IFragmentListener mIRegisterationListener =null;
    public TextView nameTextView = null;
    public TextView name_officialTextView = null;
    public ImageView flafImageView = null;
    private MapView mapView;
    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.country_details_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameTextView = (TextView)view.findViewById(R.id.name_textView);
        name_officialTextView = (TextView)view.findViewById(R.id.phone_textView);
        flafImageView = (ImageView) view.findViewById(R.id.imageView);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
//        try {
//
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
        MapsInitializer.initialize(this.getActivity());

        // Updates the location and zoom of the MapView
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
//        map.animateCamera(cameraUpdate);


        updateView();
    }

    private CountriesActivity getCountriesActivity(){
        return (CountriesActivity)getActivity();
    }

    public void updateView(){

        nameTextView.setText(getCountriesModel().getCountriesData().getName());
        name_officialTextView.setText(getCountriesModel().getCountriesData().getName_official());

        Picasso.with(getCountriesActivity())
                .load(getCountriesModel().getCountriesData().getFlag_128())
                .resize(128,64)
                .into(flafImageView);

        double lat= Double.parseDouble(getCountriesModel().getCountriesData().getLatitude());
        double lng= Double.parseDouble(getCountriesModel().getCountriesData().getLongitude());
        LatLng sydney = new LatLng(lat, lng);
        map.addMarker(new MarkerOptions().position(sydney).title(getCountriesModel().getCountriesData().getName()));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));

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
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
