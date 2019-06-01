package com.mobi.samsung.manausmobi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobi.samsung.manausmobi.R;
import com.mobi.samsung.manausmobi.models.InfoBus;
import com.mobi.samsung.manausmobi.models.PointBus;

import java.util.List;

/**
 * Created by fabio.silva on 12/11/2017.
 */

public class PointBusAdapter extends BaseAdapter {

    private Context context;
    private List<InfoBus> infos;

    public PointBusAdapter(Context context, List<InfoBus> infos) {
        this.context = context;
        this.infos = infos;
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int i) {
        return infos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        InfoBus infoBus = (InfoBus) getItem(i);
        View viewContext = LayoutInflater.from(context).inflate(R.layout.content_info_bus_item, viewGroup, false);
        TextView line = viewContext.findViewById(R.id.line);
        line.setText(infoBus.busInfo);
        TextView number = viewContext.findViewById(R.id.number_line);
        number.setText(infoBus.key);
        return viewContext;
    }
}
