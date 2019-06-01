package com.mobi.samsung.manausmobi.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mobi.samsung.manausmobi.R;
import com.mobi.samsung.manausmobi.controllers.IPointBusController;
import com.mobi.samsung.manausmobi.controllers.ISharedController;
import com.mobi.samsung.manausmobi.controllers.impl.AbstractControllerFactory;
import com.mobi.samsung.manausmobi.controllers.impl.ConcreteControllerFactory;
import com.mobi.samsung.manausmobi.listeners.OnMapListener;
import com.mobi.samsung.manausmobi.models.PointBus;
import com.mobi.samsung.manausmobi.models.SafetyMessage;
import com.mobi.samsung.manausmobi.models.SecurityMessage;
import com.mobi.samsung.manausmobi.models.Shared;
import com.mobi.samsung.manausmobi.models.SharedPoint;
import com.mobi.samsung.manausmobi.models.SharedType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MobilityActivity extends NavigationActivity implements
        OnMapListener, OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    private ISharedController sharedController;
    private IPointBusController pointBusController;


    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationManager locationManager;

    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private double latitude;
    private double longitude;

    private final Handler handler = new Handler();
    private Runnable runnable;
    private boolean lockCamera;
    boolean isConnect;

    private static final int ALERT_TIME = 300000;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    int getContentViewId() {
        return R.layout.activity_mobility;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_mob;
    }

    @Override
    void callBackCreate() {
        if (!lowStorageCheck()) {
            checkIntro();
            refreshMap();
            internetConfig();
            gpsConfig();
            cleanDatabase();
            externalServices();
        }
    }

    private void checkIntro() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                if (isFirstStart) {
                    final Intent i = new Intent(MobilityActivity.this, IntroActivity.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(i);
                        }
                    });
                    SharedPreferences.Editor e = getPrefs.edit();

                    e.putBoolean("firstStart", false);
                    e.apply();
                }
            }
        });
        t.start();
    }

    private void checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        isConnect = (netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable());
    }

    private boolean gpsOn() {
        ContentResolver contentResolver = this.getContentResolver();
        int mode = Settings.Secure.getInt(contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);
        return (mode != Settings.Secure.LOCATION_MODE_OFF);
    }

    private void initControllers() {
        checkConnection();
        AbstractControllerFactory controllerFactory = new ConcreteControllerFactory();

        sharedController = controllerFactory.createSharedController(getApplicationContext());
        sharedController.setList(this, isConnect);

        pointBusController = controllerFactory.createPointBusController(getApplicationContext());
        pointBusController.setList(this, isConnect);
    }

    private void gpsConfig() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (!isLocationEnabled()) {
            Toast.makeText(this, getString(R.string.habilitargps), Toast.LENGTH_SHORT).show();
        }
    }

    private void internetConfig() {
        checkConnection();
        if (!isConnect) {
            Toast.makeText(this, getString(R.string.erroDeConexao), Toast.LENGTH_SHORT).show();
        }
    }

    private void externalServices() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        initialButtonsCheck();

        final Activity self = this;

        FloatingActionButton actionButton = findViewById(R.id.security);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final EditText input = new EditText(MobilityActivity.this);
                input.setHint(getString(R.string.message));
                AlertDialog.Builder builder = new AlertDialog.Builder(self)
                        .setTitle(getString(R.string.serviceSecurityName))
                        .setCancelable(true)
                        .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SecurityMessage message = new SecurityMessage(latitude, longitude, input.getText().toString());
                                alertButton(view, "security", R.string.serviceSecurity, null, message);
                            }
                        });

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(5, 0, 5, 0);

                input.setHighlightColor(Color.BLACK);
                input.setLayoutParams(lp);
                builder.setView(input);
                builder.show().getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });

        actionButton = findViewById(R.id.safety);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final EditText input = new EditText(MobilityActivity.this);
                input.setHint(getString(R.string.message));
                final AlertDialog.Builder builder = new AlertDialog.Builder(self)
                        .setTitle(getString(R.string.serviceSafetyName))
                        .setCancelable(true)
                        .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SafetyMessage message = new SafetyMessage(latitude, longitude, input.getText().toString());
                                alertButton(view, "safety", R.string.serviceSafety, message, null);
                            }
                        });

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(5, 0, 5, 0);
                input.setHighlightColor(Color.BLACK);
                input.setLayoutParams(lp);
                builder.setView(input);
                builder.show().getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        startThreadSafety();
        startThreadSecurity();
    }

    private void alertButton(View view, String type, int service, SafetyMessage safetyMessage, SecurityMessage securityMessage) {
        checkConnection();
        if (!isConnect) {
            Toast.makeText(getApplicationContext(), getString(R.string.erroDeConexao), Toast.LENGTH_SHORT).show();
        } else if (!gpsOn()) {
            Toast.makeText(getApplicationContext(), getString(R.string.habilitargps), Toast.LENGTH_SHORT).show();
        } else {
            buttonClicked(view, type);
            if (type == "security") sharedController.send(securityMessage);
            else sharedController.send(safetyMessage);
            Toast.makeText(getApplicationContext(), getString(service), Toast.LENGTH_SHORT).show();
        }
    }

    private void disableButton(View view) {
        view.setAlpha(.5f);
        view.setEnabled(false);
    }

    private void updateEditor(String type) {
        long lastClickSafety = SystemClock.elapsedRealtime();
        editor.putLong(type, lastClickSafety);
        editor.apply();
    }

    private void buttonClicked(View view, String type) {
        disableButton(view);
        updateEditor(type);
    }

    private void initialButtonsCheck() {
        checkButton(R.id.security, "security");
        checkButton(R.id.safety, "safety");
    }

    private void checkButton(int id, String type) {
        FloatingActionButton actionButton = findViewById(id);
        Long lastClick = pref.getLong(type, 0);
        if (SystemClock.elapsedRealtime() - lastClick < ALERT_TIME) {
            disableButton(actionButton);
        }
    }

    private void startThreadSecurity() {
        createThread(R.id.security, "security");
    }

    private void startThreadSafety() {
        createThread(R.id.safety, "safety");
    }

    private void createThread(final int id, final String type) {
        Thread thread = new Thread() {
            FloatingActionButton actionButton = findViewById(id);

            @Override
            public void run() {
                try {
                    while (true) {
                        sleep(1000);
                        if (!actionButton.isEnabled()) {
                            Long elapsedTime = SystemClock.elapsedRealtime() - pref.getLong(type, 0);
                            if (elapsedTime > ALERT_TIME || elapsedTime < 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        actionButton.setAlpha(1f);
                                        actionButton.setEnabled(true);
                                    }
                                });
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    @Override
    public void addShared(List<SharedPoint> sharedList) {
        for (SharedPoint point : sharedList) {
            for (Shared shared : point.getSharedList()) {
                sharedController.addLocal(shared);
            }
        }
        sharedController.setSharedList(sharedList);
        addMarkerShared();
    }

    private void addMarkerShared() {
        for (SharedPoint shared : sharedController.getSharedList()) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(shared.getLatitude(), shared.getLongitude()))
                    .title(String.valueOf(sharedController.getSharedList().indexOf(shared)))
                    .icon(BitmapDescriptorFactory.fromResource(shared.getType() == SharedType.Traffic ? R.drawable.traffic_map : R.drawable.warning_map)));
        }
    }

    @Override
    public void removeShared(Shared shared) {
        sharedController.removeLocal(shared);
        refreshMap();
    }

    @Override
    public void addPointBus(PointBus shared) {
        pointBusController.addLocal(shared);
        addMarkerPointBuss();
    }

    private void addMarkerPointBuss() {
        for (PointBus pointBus : pointBusController.getPointBusList()) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(pointBus.latitude, pointBus.longitude))
                    .title(pointBus.key)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop)));
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        infoWindowAdapter();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        updateLocationUI();
        getDeviceLocation();

        if (sharedController != null && pointBusController != null) {
            addMarkerShared();
            addMarkerPointBuss();
        }

        mMap.setOnMapLoadedCallback(this);
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.setMyLocationEnabled(true);
                                latitude = mLastKnownLocation.getLatitude();
                                longitude = mLastKnownLocation.getLongitude();
                            }
                        } else {
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                        drawUserMarker(mLastKnownLocation);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                drawUserMarker(mLastKnownLocation);
            } else {
                mMap.setMyLocationEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void drawUserMarker(Location location) {
        if (mMap != null && !lockCamera) {
            if (location != null) {
                LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, DEFAULT_ZOOM));
                lockCamera = true;
            }

        }
    }


    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void cleanDatabase() {
        final OnMapListener listener = this;
        runnable = new Runnable() {
            @Override
            public void run() {
                if (sharedController != null) {
                    sharedController.cleanDatabase(listener);
                }
                handler.postDelayed(this, 120000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public void callBackDestroy() {
        handler.removeCallbacks(runnable);
    }

    private void infoWindowAdapter() {
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker arg0) {
                View v = getLayoutInflater().inflate(R.layout.content_info, null);
                TextView type = v.findViewById(R.id.type);
                ImageView iconType = v.findViewById(R.id.icon_type);
                TextView count = v.findViewById(R.id.count);
                PointBus pointBus = pointBusController.findBusByKey(arg0.getTitle());
                if (pointBus != null) {
                    type.setText(getString(R.string.parada));
                    iconType.setImageResource(R.drawable.ic_bus);
                    iconType.setVisibility(View.VISIBLE);
                } else {
                    SharedPoint shared = sharedController.findSharedByIndex(arg0.getTitle());
                    if (shared != null) {
                        count.setText(String.valueOf(shared.getSharedList().size()));
                        count.setVisibility(View.VISIBLE);
                        type.setText(shared.getType().getDescription() + " - " + shared.getIntensity().getDescription());
                    }
                }
                return v;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                PointBus pointBus = pointBusController.findBusByKey(marker.getTitle());
                if (pointBus != null) {
                    Intent intent = new Intent(getApplicationContext(), PointBusDetailActivity.class);
                    intent.putExtra("id", pointBus.id);
                    startActivity(intent);
                } else {
                    SharedPoint point = sharedController.findSharedByIndex(marker.getTitle());
                    if (point != null) {
                        Intent intent = new Intent(getApplicationContext(), SharedDetailActivity.class);
                        List<String> ids = new ArrayList<>();
                        for (Shared shared : point.getSharedList()) {
                            ids.add(shared.key);
                        }
                        intent.putExtra("keys", Arrays.copyOf(ids.toArray(), ids.size(), String[].class));
                        startActivity(intent);
                    }
                }
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                MarkerOptions markerOptions = new MarkerOptions();
                MarkerOptions option = markerOptions.position(arg0);
                if (option.getTitle() != null) {
                    mMap.clear();
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
                    Marker marker = mMap.addMarker(markerOptions);
                    marker.showInfoWindow();
                }

            }
        });
    }

    private void refreshMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sharedController != null && pointBusController != null) {
            sharedController.cleanEventListener();
            pointBusController.cleanEventListener();
        }
    }

    @Override
    public void onMapLoaded() {
        if (sharedController == null || pointBusController == null) {
            initControllers();
        }
    }

    public boolean lowStorageCheck() {
        IntentFilter lowStorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
        boolean hasLowStorage = registerReceiver(null, lowStorageFilter) != null;
        if (hasLowStorage) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(getString(R.string.messageStorage))
                    .setTitle(getString(R.string.titleStorage))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.show();
            alert.show();
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);

        }
        return hasLowStorage;
    }
}
