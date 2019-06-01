package com.mobi.samsung.manausmobi.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.mobi.samsung.manausmobi.R;
import com.mobi.samsung.manausmobi.adapters.PointBusAdapter;
import com.mobi.samsung.manausmobi.controllers.impl.AbstractControllerFactory;
import com.mobi.samsung.manausmobi.controllers.impl.ConcreteControllerFactory;
import com.mobi.samsung.manausmobi.models.InfoBus;
import com.mobi.samsung.manausmobi.models.PointBus;

import java.util.List;

public class PointBusDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_bus_detail);
        setTitle(getString(R.string.parada));
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

        int id = getIntent().getExtras().getInt("id");
        List<InfoBus> infos = new ConcreteControllerFactory().createPointBusController(getApplicationContext()).findInfoBusByKey(id);
        PointBusAdapter adapter = new PointBusAdapter(getApplicationContext(), infos);
        ListView listView = findViewById(R.id.list_line);
        listView.setAdapter(adapter);
    }

}
