package com.mobi.samsung.manausmobi.activities;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;
import android.util.Base64;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mobi.samsung.manausmobi.R;
import com.mobi.samsung.manausmobi.controllers.impl.ConcreteControllerFactory;
import com.mobi.samsung.manausmobi.fragments.SharedFragment;
import com.mobi.samsung.manausmobi.controllers.ISharedController;
import com.mobi.samsung.manausmobi.listeners.OnSharedListener;
import com.mobi.samsung.manausmobi.models.Shared;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SharedActivity extends NavigationActivity implements OnSharedListener {
    private ISharedController sharedController;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;
    private SharedFragment fragment;
    private Intent mobilityIntent;

    private Geocoder geocoder;
    private List<Address> addresses;

    private boolean screenStateFlag;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    int getContentViewId() {
        return R.layout.activity_sharing;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_share;
    }

    @Override
    void callBackCreate() {
        sharedController = new ConcreteControllerFactory().createSharedController(getApplicationContext());

        geocoder = new Geocoder(this, Locale.getDefault());
        createFragmet();
    }

    @Override
    void callBackDestroy() {

    }

    public void saveShared(Shared shared) {
        shared.date = new Date().getTime();
        shared.userId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        setLocation(shared);
    }

    private void setLocation(final Shared shared) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final OnSharedListener listener = this;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                shared.longitude = location.getLongitude();
                                shared.latitude = location.getLatitude();

                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    shared.subLocality = addresses.get(0).getSubLocality();
                                    shared.thoroughfare = addresses.get(0).getThoroughfare();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.erroDeConexao), Toast.LENGTH_SHORT).show();
                                }

                                if (shared.image != null) {
                                    sharedController.existsWithImage(shared, listener);
                                } else {
                                    isValidSaveShared(true, shared);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.habilitargps), Toast.LENGTH_SHORT).show();
                                createFragmet();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.habilitargps), Toast.LENGTH_SHORT).show();
            createFragmet();
        }
    }

    @Override
    public void onBackPressed() {
        if (screenStateFlag) {
            createFragmet();
        } else {
            Intent intent = new Intent(getApplicationContext(), MobilityActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
    }

    @Override
    public void screenState(boolean screenStateFlag) {
        this.screenStateFlag = screenStateFlag;
    }


    private void createFragmet() {
        fragment = new SharedFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_car, fragment);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            fragment.setImage(encoded);

            Toast.makeText(getApplicationContext(), getString(R.string.imagemCarregada), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void isValidSaveShared(boolean isValid, Shared shared) {
        if (!isValid) {
            Toast.makeText(getApplicationContext(), getString(R.string.limiteImagem), Toast.LENGTH_SHORT).show();
            createFragmet();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.informacaoEnviada), Toast.LENGTH_SHORT).show();
            sharedController.add(shared);
            finish();
        }
    }
}