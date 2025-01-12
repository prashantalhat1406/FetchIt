package com.sinprl.fetchit.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinprl.fetchit.R;
import com.sinprl.fetchit.data.Profile;
import com.sinprl.fetchit.interfaces.OnItemClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProfileListAdaptor extends RecyclerView.Adapter<ProfileListAdaptor.ViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<Profile> data_entries;
    private final OnItemClickListener mOnItemClickListener;

    public ProfileListAdaptor(Context mContext, List<Profile> data_entries, OnItemClickListener mOnItemClickListener){
        this.context = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        this.data_entries = data_entries;
        this.mOnItemClickListener = mOnItemClickListener;
    }



    @NonNull
    @Override
    public ProfileListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_profile_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileListAdaptor.ViewHolder holder, int position) {
        Profile profile = data_entries.get(position);
        holder.profile_id.setText(profile.getId());
        holder.user_name.setText(profile.getName());
        holder.user_status.setText(profile.getStatus());

        SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
        Date fromatedDate = null;
        try {
            fromatedDate = spf.parse(profile.getEntry_date());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        spf= new SimpleDateFormat("dd-MMM-yyyy");
        holder.created_date.setText(spf.format(fromatedDate));

        switch (profile.getStatus()){
            case "NEW": holder.user_status.setBackground(context.getDrawable(R.drawable.circle_new));
                holder.user_status.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case "KYC": holder.user_status.setBackground(context.getDrawable(R.drawable.circle_kyc));
                holder.user_status.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case "LOG": holder.user_status.setBackground(context.getDrawable(R.drawable.circle_log));
                holder.user_status.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case "SAN": holder.user_status.setBackground(context.getDrawable(R.drawable.circle_san));
                holder.user_status.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case "DIS": holder.user_status.setBackground(context.getDrawable(R.drawable.circle_dis));
                holder.user_status.setTextColor(context.getResources().getColor(R.color.white));
                break;
        }
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
    public final TextView created_date;
    public final TextView profile_id;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        user_name =  itemView.findViewById(R.id.text_item_data_name);
        user_mobile =  itemView.findViewById(R.id.text_item_data_mobile);
        user_status =  itemView.findViewById(R.id.text_item_data_status);
        created_date =  itemView.findViewById(R.id.text_item_data_createddate);
        profile_id = itemView.findViewById(R.id.text_item_profile_id);

    }
}
}
