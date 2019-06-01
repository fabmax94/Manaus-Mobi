package com.mobi.samsung.manausmobi.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mobi.samsung.manausmobi.R;
import com.mobi.samsung.manausmobi.adapters.PointBusAdapter;
import com.mobi.samsung.manausmobi.controllers.impl.ConcreteControllerFactory;
import com.mobi.samsung.manausmobi.models.InfoBus;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SharedDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_detail);
        setTitle(getString(R.string.gallery));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String[] keys = getIntent().getExtras().getStringArray("keys");
        List<String> listImages = new ConcreteControllerFactory().createSharedController(getApplicationContext()).findSharedsByKey(keys);

        final ImageView diplayImage = findViewById(R.id.displayImage);
        final LinearLayout myGallery = findViewById(R.id.mygallery);

        if (listImages.size() == 0) {
            TextView text = findViewById(R.id.empty);
            text.setVisibility(View.VISIBLE);
        }
        for (String imageString : listImages) {
            byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            if(listImages.indexOf(imageString) == 0){
                diplayImage.setImageBitmap(bitmap);
            }
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(140, 140));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(bitmap);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    diplayImage.setImageBitmap(bitmap);
                }
            });

            myGallery.addView(imageView);
        }
    }

}
