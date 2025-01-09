package com.sinprl.fetchit.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinprl.fetchit.R;
import com.sinprl.fetchit.data.DataEntry;
import com.sinprl.fetchit.interfaces.OnItemClickListener;

import java.util.List;

public class DataListAdaptor extends RecyclerView.Adapter<DataListAdaptor.ViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<DataEntry> data_entries;
    private final OnItemClickListener mOnItemClickListener;

    public DataListAdaptor(Context mContext, List<DataEntry> data_entries, OnItemClickListener mOnItemClickListener){
        this.context = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        this.data_entries = data_entries;
        this.mOnItemClickListener = mOnItemClickListener;
    }



    @NonNull
    @Override
    public DataListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_data_entry, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataListAdaptor.ViewHolder holder, int position) {
        DataEntry dataEntry = data_entries.get(position);
        holder.user_name.setText(dataEntry.getName());
        holder.user_mobile.setText(dataEntry.getMobile());
        holder.user_status.setText(dataEntry.getStatus());
        switch (dataEntry.getStatus()){
            case "NEW": holder.user_status.setBackground(context.getDrawable(R.color.status_new));
                holder.user_status.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case "KYC": holder.user_status.setBackground(context.getDrawable(R.color.status_kyc));
                holder.user_status.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case "LOG": holder.user_status.setBackground(context.getDrawable(R.color.status_log));
                holder.user_status.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case "SAN": holder.user_status.setBackground(context.getDrawable(R.color.status_san));
                holder.user_status.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case "DIS": holder.user_status.setBackground(context.getDrawable(R.color.status_dis));
                holder.user_status.setTextColor(context.getResources().getColor(R.color.white));
                break;
        }
//        holder.user_address.setText(dataEntry.getAddress());
//        holder.type_of_product.setText(dataEntry.getTypeofproduct());
//        holder.choice_of_bank.setText(dataEntry.getChoiceofbank());
        holder.itemView.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, position));
    }

    @Override
    public int getItemCount() {
        return data_entries.size();
    }

public static class ViewHolder extends RecyclerView.ViewHolder{

    public final TextView user_name;
    public final TextView user_mobile;
    public final TextView user_status;
//    public final TextView user_address;
//    public final TextView type_of_product;
//    public final TextView choice_of_bank;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        user_name =  itemView.findViewById(R.id.text_item_data_name);
        user_mobile =  itemView.findViewById(R.id.text_item_data_mobile);
        user_status =  itemView.findViewById(R.id.text_item_data_status);
//        user_address =  itemView.findViewById(R.id.text_item_data_address);
//        type_of_product =  itemView.findViewById(R.id.text_item_data_type_of_product);
//        choice_of_bank =  itemView.findViewById(R.id.text_item_data_choice_of_bank);
    }
}
}
