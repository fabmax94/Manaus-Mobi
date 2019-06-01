package com.mobi.samsung.manausmobi.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mobi.samsung.manausmobi.R;
import com.mobi.samsung.manausmobi.models.Shared;
import com.mobi.samsung.manausmobi.models.SharedIntensity;
import com.mobi.samsung.manausmobi.models.SharedType;
import com.mobi.samsung.manausmobi.services.ISharedService;
import com.mobi.samsung.manausmobi.services.impl.ConcreteServiceFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class MobiWidget extends AppWidgetProvider {
    private Geocoder geocoder;
    private List<Address> addresses;
    private FusedLocationProviderClient mFusedLocationClient;
    private Shared shared;
    private ISharedService sharedService;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        RemoteViews remoteViews = null;
        ComponentName watchWidget = null;
        String selectedType = null;


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals("traffic")) {
            selectedType = SharedType.Traffic.toString();
            remoteViews = new RemoteViews(context.getPackageName(), com.mobi.samsung.manausmobi.R.layout.mobi_widget_traffic);
            watchWidget = new ComponentName(context, MobiWidget.class);

            remoteViews.setOnClickPendingIntent(R.id.btn_moderate, getPendingSelfIntent(context, "moderate"));
            remoteViews.setOnClickPendingIntent(R.id.btn_intense, getPendingSelfIntent(context, "intense"));
            remoteViews.setOnClickPendingIntent(R.id.btn_stopped, getPendingSelfIntent(context, "stopped"));
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        } else if (intent.getAction().equals("warning")) {
            selectedType = SharedType.Warning.toString();
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.mobi_widget_warning);
            watchWidget = new ComponentName(context, MobiWidget.class);

            remoteViews.setOnClickPendingIntent(R.id.btn_road, getPendingSelfIntent(context, "road"));
            remoteViews.setOnClickPendingIntent(R.id.btn_coasting, getPendingSelfIntent(context, "coasting"));
            remoteViews.setOnClickPendingIntent(R.id.btn_weather, getPendingSelfIntent(context, "weather"));
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        } else if (intent.getAction().equals("moderate") || intent.getAction().equals("intense") || intent.getAction().equals("stopped") || intent.getAction().equals("road") || intent.getAction().equals("coasting") || intent.getAction().equals("weather")) {
            shared = new Shared();
            if (intent.getAction().equals("moderate")) {
                shared.intensity = SharedIntensity.Moderate;
                shared.type = SharedType.Traffic;
            } else if (intent.getAction().equals("intense")) {
                shared.intensity = SharedIntensity.Intense;
                shared.type = SharedType.Traffic;
            } else if (intent.getAction().equals("stopped")) {
                shared.intensity = SharedIntensity.Stopped;
                shared.type = SharedType.Traffic;
            } else if (intent.getAction().equals("road")) {
                shared.intensity = SharedIntensity.InRoute;
                shared.type = SharedType.Warning;
            } else if (intent.getAction().equals("coasting")) {
                shared.intensity = SharedIntensity.Coasting;
                shared.type = SharedType.Warning;
            } else if (intent.getAction().equals("weather")) {
                shared.intensity = SharedIntensity.Climate;
                shared.type = SharedType.Warning;
            }
            geocoder = new Geocoder(context, Locale.getDefault());
            sharedService = new ConcreteServiceFactory().createSharedService();
            shared.date = new Date().getTime();
            shared.userId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            setLocation(shared, context);
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.mobi_widget);
            remoteViews.setOnClickPendingIntent(R.id.btn_traffic, getPendingSelfIntent(context, "traffic"));
            remoteViews.setOnClickPendingIntent(R.id.btn_warning, getPendingSelfIntent(context, "warning"));
            watchWidget = new ComponentName(context, MobiWidget.class);
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);
            Toast.makeText(context, "Informação compartilhada com sucesso", Toast.LENGTH_SHORT).show();

        }

    }

    private void setLocation(final Shared shared, final Context context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            shared.longitude = location.getLongitude();
                            shared.latitude = location.getLatitude();

                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            shared.subLocality = addresses.get(0).getSubLocality();
                            shared.thoroughfare = addresses.get(0).getThoroughfare();

                            sharedService.add(shared);
                        }
                    }
                });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.mobi_widget);
        ComponentName watchWidget = new ComponentName(context, MobiWidget.class);
        remoteViews.setOnClickPendingIntent(R.id.btn_traffic, getPendingSelfIntent(context, "traffic"));
        remoteViews.setOnClickPendingIntent(R.id.btn_warning, getPendingSelfIntent(context, "warning"));

        appWidgetManager.updateAppWidget(watchWidget, remoteViews);

    }


    private PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, MobiWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

