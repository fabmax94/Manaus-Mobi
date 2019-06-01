package com.mobi.samsung.manausmobi.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrafficWarningDashboardFragment extends Fragment {

    private OnDashboardListener listener;
    private PieChart pieChart;
    private List<TrafficWarningDashboard> dashboardList;
    private View contextView;

    final int[] MY_COLORS = {Color.rgb(192, 0, 0), Color.rgb(255, 0, 0), Color.rgb(255, 192, 0),
            Color.rgb(127, 127, 127), Color.rgb(146, 208, 80), Color.rgb(0, 176, 80), Color.rgb(79, 129, 189)};
    ArrayList<Integer> colors = new ArrayList<Integer>();

    public TrafficWarningDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        listener.getTrafficWarning();
        contextView = inflater.inflate(R.layout.fragment_traffic_warning_dashboard, container, false);
        initSpinners(contextView);

        pieChart = contextView.findViewById(R.id.piechart);
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
        pieChart.setNoDataText("");
        return contextView;
    }

    private void initSpinners(View contextView) {
        MaterialBetterSpinner spinner = contextView.findViewById(R.id.local);
        ArrayList<String> list = new ArrayList<String>() {
            {
                add(getString(R.string.bairro));
                add(getString(R.string.rua));
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
                listener.changeLocal();
            }
        });

        spinner = contextView.findViewById(R.id.trafficWarning);
        list = new ArrayList<String>() {
            {
                add(getString(R.string.transito));
                add(getString(R.string.perigo));
            }
        };
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_frame, list);
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
                refreshDashboard();
            }
        });
    }

    public void refreshTrafficDashboard() {

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int c : MY_COLORS) colors.add(c);

        int index = 0;
        Collections.sort(dashboardList, new Comparator<TrafficWarningDashboard>() {
            @Override
            public int compare(final TrafficWarningDashboard object1, final TrafficWarningDashboard object2) {
                return object1.getTrafficWeight() > object2.getTrafficWeight() ? -1 : 1;
            }
        });
        for (TrafficWarningDashboard trafficWarningDashboard : this.dashboardList.subList(0, dashboardList.size() <= 4 ? dashboardList.size() : 4)) {
            if (trafficWarningDashboard.getTrafficWeight() != 0) {
                yvalues.add(new Entry(trafficWarningDashboard.getTrafficWeight(), index));
                xVals.add(trafficWarningDashboard.getLocal());
                index++;
            }
        }

        if (yvalues.size() > 0) {
            pieChart.setDescription(getString(R.string.lgd_traffic));
            setChart(yvalues, xVals);
        }
    }

    public void refreshWarningDashboard() {

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int c : MY_COLORS) colors.add(c);
        int index = 0;
        Collections.sort(dashboardList, new Comparator<TrafficWarningDashboard>() {
            @Override
            public int compare(final TrafficWarningDashboard object1, final TrafficWarningDashboard object2) {
                return object1.getWarningWeight() > object2.getWarningWeight() ? -1 : 1;
            }
        });

        for (TrafficWarningDashboard trafficWarningDashboard : this.dashboardList.subList(0, dashboardList.size() <= 4 ? dashboardList.size() : 4)) {
            if (trafficWarningDashboard.getWarningWeight() != 0) {
                yvalues.add(new Entry(trafficWarningDashboard.getWarningWeight(), index));
                xVals.add(trafficWarningDashboard.getLocal());
                index++;
            }
        }
        if (yvalues.size() > 0) {
            pieChart.setDescription(getString(R.string.lgd_warning));
            setChart(yvalues, xVals);
        }
    }

    private void setChart(ArrayList<Entry> yvalues, ArrayList<String> xVals) {
        PieDataSet dataSet = new PieDataSet(yvalues, "");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(11);
        PieData data = new PieData(xVals, dataSet);

        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        pieChart.setNoDataText(getString(R.string.piechart));

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (OnDashboardListener) activity;
    }

    public void setDashboardList(List<TrafficWarningDashboard> list) {
        this.dashboardList = list;
    }

    public void refreshDashboard() {
        MaterialBetterSpinner spinner = contextView.findViewById(R.id.local);
        if (this.dashboardList != null && !spinner.getText().toString().isEmpty()) {
            spinner = contextView.findViewById(R.id.trafficWarning);
            pieChart.clear();
            if (spinner.getText().toString().equals(getString(R.string.transito))) {
                refreshTrafficDashboard();
            } else if (spinner.getText().toString().equals(getString(R.string.perigo))) {
                refreshWarningDashboard();
            }
        }
    }

    public boolean isSubLocal() {
        MaterialBetterSpinner spinner = contextView.findViewById(R.id.local);
        return spinner.getText().toString().equals(getString(R.string.bairro));
    }
}
