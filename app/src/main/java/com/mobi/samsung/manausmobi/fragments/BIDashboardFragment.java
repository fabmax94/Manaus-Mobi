package com.mobi.samsung.manausmobi.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.mobi.samsung.manausmobi.R;
import com.mobi.samsung.manausmobi.listeners.OnDashboardListener;
import com.mobi.samsung.manausmobi.models.TrafficBIDashboard;
import com.mobi.samsung.manausmobi.models.TrafficWarningDashboard;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class BIDashboardFragment extends Fragment {


    private OnDashboardListener listener;
    private BarChart barChart;
    private PieChart pieChart;
    private List<TrafficBIDashboard> dashboardList;
    private View contextView;

    private TextWatcher changedListener;

    final int[] MY_COLORS = {Color.rgb(192, 0, 0), Color.rgb(255, 0, 0), Color.rgb(255, 192, 0),
            Color.rgb(127, 127, 127), Color.rgb(146, 208, 80), Color.rgb(0, 176, 80), Color.rgb(79, 129, 189)};
    ArrayList<Integer> colors = new ArrayList<Integer>();

    public BIDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (OnDashboardListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        listener.getTrafficWarning();
        contextView = inflater.inflate(R.layout.fragment_bi, container, false);
        barChart = contextView.findViewById(R.id.barChart);
        barChart.setDrawValueAboveBar(true);
        barChart.setDrawBarShadow(true);
        barChart.setDrawHighlightArrow(true);

        pieChart = contextView.findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        pieChart.setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.setNoDataText(getString(R.string.piechart));
        pieChart.setDrawSliceText(false);
        Legend legend = pieChart.getLegend();
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        initSpinners();
        return contextView;
    }

    private void initSpinners() {
        MaterialBetterSpinner spinner = contextView.findViewById(R.id.option);
        ArrayList<String> list = new ArrayList<String>() {
            {
                add(getString(R.string.bairro));
                add(getString(R.string.hora));
            }
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_frame, list);
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
                listener.changeBIOption(editable.toString().equals(getString(R.string.hora)));
                MaterialBetterSpinner spinner = contextView.findViewById(R.id.sub_option);
                spinner.setVisibility(View.VISIBLE);

            }
        });
    }

    public void initLocalSpinners() {
        MaterialBetterSpinner spinner = contextView.findViewById(R.id.sub_option);
        spinner.setText("");
        ArrayList<String> list = new ArrayList<String>();
        for (TrafficBIDashboard dashboard : dashboardList) {
            if (dashboard.getLocal() != null) {
                list.add(dashboard.getLocal());
            }
        }

        Set<String> listNotDuplicate = new HashSet<String>();
        listNotDuplicate.addAll(list);
        list = new ArrayList<>(listNotDuplicate);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_frame, list);
        spinner.setAdapter(adapter);
        if (changedListener != null) {
            spinner.removeTextChangedListener(changedListener);
        }
        spinner.addTextChangedListener(changedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                listener.changeBILocal();
            }
        });
    }

    public void initHoursSpinners() {
        MaterialBetterSpinner spinner = contextView.findViewById(R.id.sub_option);
        spinner.setText("");
        ArrayList<String> list = new ArrayList<String>();
        for (TrafficBIDashboard dashboard : dashboardList) {
            if (dashboard.getHours() != null) {
                list.add(dashboard.getHours());
            }
        }

        Set<String> listNotDuplicate = new HashSet<String>();
        listNotDuplicate.addAll(list);
        list = new ArrayList<>(listNotDuplicate);

        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(final String object1, final String object2) {
                return object1.compareTo(object2);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_frame, list);
        spinner.setAdapter(adapter);
        if (changedListener != null) {
            spinner.removeTextChangedListener(changedListener);
        }
        spinner.addTextChangedListener(changedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                listener.changeBIHours();
            }
        });
    }

    public void refreshLocalDashboard(List<TrafficBIDashboard> locationList) {
        barChart.clear();
        barChart.setVisibility(View.VISIBLE);
        barChart.setNoDataText(getString(R.string.piechart));
        pieChart.setVisibility(View.GONE);

        ArrayList<BarEntry> yvalues = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int c : MY_COLORS) colors.add(c);
        int index = 0;
        for (TrafficBIDashboard trafficWarningDashboard : locationList) {
            if (trafficWarningDashboard.getTrafficWeight() != 0) {
                yvalues.add(new BarEntry(trafficWarningDashboard.getTrafficWeight(), index));
                xVals.add(trafficWarningDashboard.getHours());
                index++;
            }
        }
        if (xVals.size() > 0) {
            BarDataSet dataSet = new BarDataSet(yvalues, "");
            dataSet.setColors(colors);
            BarData data = new BarData(xVals, dataSet);
            barChart.setData(data);
            barChart.setDescription(getString(R.string.lgd_bi_local));

        }
    }

    public void refreshHoursDashboard(List<TrafficBIDashboard> locationList) {
        pieChart.clear();
        barChart.setVisibility(View.GONE);
        pieChart.setVisibility(View.VISIBLE);

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int c : MY_COLORS) colors.add(c);
        int index = 0;
        Collections.sort(locationList, new Comparator<TrafficBIDashboard>() {
            @Override
            public int compare(final TrafficBIDashboard object1, final TrafficBIDashboard object2) {
                return object1.getTrafficWeight() > object2.getTrafficWeight() ? -1 : 1;
            }
        });
        for (TrafficBIDashboard trafficWarningDashboard : locationList.subList(0, locationList.size() <= 4 ? locationList.size() : 4)) {
            if (trafficWarningDashboard.getTrafficWeight() != 0) {
                yvalues.add(new Entry(trafficWarningDashboard.getTrafficWeight(), index));
                xVals.add(trafficWarningDashboard.getLocal());
                index++;
            }
        }

        if (xVals.size() > 0) {
            PieDataSet dataSet = new PieDataSet(yvalues, "");
            dataSet.setColors(colors);

            PieData data = new PieData(xVals, dataSet);

            dataSet.setValueTextSize(11);
            data.setValueFormatter(new PercentFormatter());
            pieChart.setData(data);
            pieChart.setDescription(getString(R.string.lgd_bi_hours));
        }
    }

    public void setDashboardList(List<TrafficBIDashboard> list) {
        this.dashboardList = list;
    }

    public List<TrafficBIDashboard> getDashboardList() {
        return dashboardList;
    }

    public String getSubOption() {
        MaterialBetterSpinner spinner = contextView.findViewById(R.id.sub_option);
        return spinner.getText().toString();
    }
}