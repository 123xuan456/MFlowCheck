package com.mtm.flowcheck.utils.printer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.mtm.flowcheck.R;

import java.util.List;
import java.util.Map;

public class MicroPrinterAdapter extends BaseAdapter {

    private List<Map<String, Object>> printerList;
    private Context context;
    private LayoutInflater mInflater;

    public List<Map<String, Object>> getIllegals() {
        return printerList;
    }

    public MicroPrinterAdapter(List<Map<String, Object>> list, Context context) {
        this.printerList = list;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void clear() {
        if (this.printerList != null)
            this.printerList.clear();
    }

    @Override
    public int getCount() {
        return this.printerList.size();
    }

    @Override
    public Object getItem(int position) {

        return this.printerList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return this.createViewFromResource(position, convertView, parent,
                R.layout.printer_micro_item);
    }

    private View createViewFromResource(int position, View convertView,
                                        ViewGroup parent, int resource) {
        View view;

        if (convertView == null) {
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        Map<String, Object> map = (Map<String, Object>) getItem(position);
        TextView nameText = (TextView) view.findViewById(R.id.printer_name);
        nameText.setText((String) map.get("PRT_NAME"));

        TextView locationText = (TextView) view
                .findViewById(R.id.printer_location);
        TextView printer_type = (TextView) view
                .findViewById(R.id.printer_type);
        locationText.setText((String) map.get("PRT_ADDRESS"));

        ImageView current = (ImageView) view.findViewById(R.id.printer_current);

        if (map.get("printer_type") != null) {//wifi连接
            printer_type.setText("Wifi Printer");
        }
        if (map.get("CURR_IMG") != null) {
            Integer curr_img = (Integer) map.get("CURR_IMG");

            current.setBackgroundDrawable(context.getResources().getDrawable(
                    (curr_img.intValue())));
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent,
                R.layout.standard_word_item);
    }

    public void addItems(List<Map<String, Object>> items) {
        this.printerList.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(Map<String, Object> item) {
        this.printerList.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int idx) {
        this.printerList.remove(idx);
        this.notifyDataSetChanged();
    }
}
