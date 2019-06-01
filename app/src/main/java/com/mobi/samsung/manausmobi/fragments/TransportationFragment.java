package com.mobi.samsung.manausmobi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mobi.samsung.manausmobi.R;
import com.mobi.samsung.manausmobi.listeners.OnDashboardListener;
import com.mobi.samsung.manausmobi.models.TrafficWarningDashboard;
import com.mobi.samsung.manausmobi.models.Transportation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by taynara.p on 11/22/2017.
 */

public class TransportationFragment extends Fragment {
    private OnDashboardListener listener;
    private PieChart pieChart;
    private List<Transportation> transpList;
    private View contextView;

    public TransportationFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contextView = inflater.inflate(R.layout.fragment_transportation, container, false);

        pieChart = contextView.findViewById(R.id.piechartTransportation);
        pieChart.setUsePercentValues(true);
        pieChart.setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(false);
        pieChart.setNoDataText("");
        if(transpList != null){
            ArrayList<Entry> yvalues = new ArrayList<Entry>();
            int i = -1;
            for(Transportation transp : transpList) {
                yvalues.add(new Entry(transp.amount, i++));
            }

            PieDataSet dataSet = new PieDataSet(yvalues, "");
            ArrayList<String> xVals = new ArrayList<String>();
            for(Transportation transp : transpList) {
                xVals.add(transp.type);
            }

            // set colors and text size
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            dataSet.setValueTextSize(11);

            PieData data = new PieData(xVals, dataSet);

            // In percentage Term
            data.setValueFormatter(new PercentFormatter());

            pieChart.setData(data);
            pieChart.setDescription(getString(R.string.quantidadeVeiculos));
            pieChart.setDrawHoleEnabled(false);

            pieChart.setDrawSliceText(false);
            Legend legend = pieChart.getLegend();
            legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

            pieChart.setNoDataText(getString(R.string.piechart));
        }

        return contextView;

    }

    public void setTranspList(List<Transportation> list) {
        this.transpList = list;
    }

}
