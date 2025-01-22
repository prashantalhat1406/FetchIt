package com.sinprl.fetchit.adaptor;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sinprl.fetchit.R;

import java.util.List;

public class SpinnerAdaptor extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> mValues;

    public SpinnerAdaptor(Context context, int resource, List<String> values) {
        super(context, resource, values);
        mContext = context;
        mValues = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_value, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.spinnerText);
        textView.setText(mValues.get(position));
        switch (mValues.get(position)){
            case "NEW": textView.setTextColor(mContext.getResources().getColor(R.color.status_new));break;
            case "FOW": textView.setTextColor(mContext.getResources().getColor(R.color.status_fow));break;
            case "LOG": textView.setTextColor(mContext.getResources().getColor(R.color.status_log));break;
            case "SAN": textView.setTextColor(mContext.getResources().getColor(R.color.status_san));break;
            case "DIS": textView.setTextColor(mContext.getResources().getColor(R.color.status_dis));break;
            case "OTC": textView.setTextColor(mContext.getResources().getColor(R.color.status_otc));break;
            case "PAY": textView.setTextColor(mContext.getResources().getColor(R.color.status_pay));break;
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_value, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.spinnerText);
        textView.setText(mValues.get(position));

        switch (mValues.get(position)){
            case "NEW": textView.setTextColor(mContext.getResources().getColor(R.color.status_new));break;
            case "FOW": textView.setTextColor(mContext.getResources().getColor(R.color.status_fow));break;
            case "LOG": textView.setTextColor(mContext.getResources().getColor(R.color.status_log));break;
            case "SAN": textView.setTextColor(mContext.getResources().getColor(R.color.status_san));break;
            case "DIS": textView.setTextColor(mContext.getResources().getColor(R.color.status_dis));break;
            case "OTC": textView.setTextColor(mContext.getResources().getColor(R.color.status_otc));break;
            case "PAY": textView.setTextColor(mContext.getResources().getColor(R.color.status_pay));break;
        }

        return convertView;
    }
}
