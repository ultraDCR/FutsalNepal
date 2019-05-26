package com.example.futsalnepal.futsal;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.futsalnepal.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends BaseAdapter{
    private ArrayList<String> list;
    private Activity activity;
    private LayoutInflater inflater;

    public SpinnerAdapter(ArrayList<String> list, Activity activity) {
        this.list = list;
        this.activity = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView == null)
            view = inflater.inflate(R.layout.custom_spinner_layout,null);
        TextView textView = view.findViewById(R.id.spinner_txt);
        textView.setText(list.get(position));
        return view;
    }
}
