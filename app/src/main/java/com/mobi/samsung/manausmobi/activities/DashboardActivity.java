package com.mobi.samsung.manausmobi.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextWatcher;

import com.mobi.samsung.manausmobi.R;
import com.mobi.samsung.manausmobi.controllers.impl.ConcreteControllerFactory;
import com.mobi.samsung.manausmobi.fragments.TransportationFragment;
import com.mobi.samsung.manausmobi.models.SharedPoint;
import com.mobi.samsung.manausmobi.models.Transportation;


import android.app.FragmentTransaction;
import android.widget.ArrayAdapter;

import com.mobi.samsung.manausmobi.controllers.IDashboardController;
import com.mobi.samsung.manausmobi.fragments.BIDashboardFragment;
import com.mobi.samsung.manausmobi.fragments.TrafficWarningDashboardFragment;
import com.mobi.samsung.manausmobi.listeners.OnDashboardListener;
import com.mobi.samsung.manausmobi.models.Shared;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends NavigationActivity implements OnDashboardListener {

    @Override
    int getContentViewId() {
        return R.layout.activity_dashboard;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_dashboard;
    }

    private IDashboardController dashboardController;
    private Fragment fragment;
    private List<Shared> sharedList;
    private List<Transportation> transpList;

    @Override
    void callBackCreate() {
        dashboardController = new ConcreteControllerFactory().createDashboardController(getApplicationContext());
        sharedList = new ArrayList<Shared>();
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if ((netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable())) {
            dashboardController.setList(this);
        } else {
            dashboardController.setListLocal(this);
        }
        final MaterialBetterSpinner spinner = findViewById(R.id.spn_type);
        ArrayList<String> list = new ArrayList<String>() {
            {
                add(getString(R.string.transitoPerigo));
                add(getString(R.string.transporte));
                add(getString(R.string.bi));
            }
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_frame, list);

        spinner.setAdapter(adapter);
        spinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals(getString(R.string.transporte))) {
                    createTransportationFragment();
                    ((TransportationFragment) fragment).setTranspList(transpList);
                } else if (editable.toString().equals(getString(R.string.transitoPerigo))) {
                    createTrafficWarningFragment();
                } else if (editable.toString().equals(getString(R.string.bi))) {
                    createBIFragment();
                }
            }
        });
    }

    @Override
    void callBackDestroy() {

    }


    @Override
    public void getTrafficWarning() {

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if ((netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable())) {
            dashboardController.getTrafficWarning(this);
        } else {
            dashboardController.getTrafficWarningLocal(this);
        }
    }

    @Override
    public void changeLocal() {
        ((TrafficWarningDashboardFragment) fragment).setDashboardList(dashboardController.groupByLocal(sharedList, ((TrafficWarningDashboardFragment) fragment).isSubLocal()));
        ((TrafficWarningDashboardFragment) fragment).refreshDashboard();
    }

    @Override
    public void addShared(List<Shared> sharedList) {
        this.sharedList = sharedList;
    }

    @Override
    public void changeBILocal() {
        BIDashboardFragment biFragment = (BIDashboardFragment) fragment;
        biFragment.refreshLocalDashboard(dashboardController.filterByLocal(biFragment.getDashboardList(), biFragment.getSubOption()));
    }

    @Override
    public void changeBIOption(boolean isHours) {
        BIDashboardFragment biFragment = (BIDashboardFragment) fragment;
        if (isHours) {
            biFragment.setDashboardList(dashboardController.groupByHour(sharedList));
            biFragment.initHoursSpinners();
        } else {
            biFragment.setDashboardList(dashboardController.groupByLocal(sharedList));
            biFragment.initLocalSpinners();
        }
    }

    @Override
    public void changeBIHours() {
        BIDashboardFragment biFragment = (BIDashboardFragment) fragment;
        biFragment.refreshHoursDashboard(dashboardController.filterByHours(biFragment.getDashboardList(), biFragment.getSubOption()));
    }

    @Override
    public void createTranspList(List<Transportation> transpList) {

        this.transpList = transpList;
        dashboardController.addTransportation(transpList);
    }

    private void createTrafficWarningFragment() {
        fragment = new TrafficWarningDashboardFragment();
        createFragment();
    }

    private void createBIFragment() {
        fragment = new BIDashboardFragment();
        createFragment();
    }

    private void createTransportationFragment() {
        fragment = new TransportationFragment();
        createFragment();
    }

    private void createFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container_dashboard, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MobilityActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }
}
