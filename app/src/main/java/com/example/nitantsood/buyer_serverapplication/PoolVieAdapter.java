package com.example.nitantsood.buyer_serverapplication;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by NITANT SOOD on 28-10-2017.
 */

public class PoolVieAdapter extends RecyclerView.Adapter<PoolVieAdapter.OnePoolViewHolder> {
    Context mContext;
    ArrayList<OnePoolItem> list;
    setOnPoolClickListener mListener;

    public PoolVieAdapter(Context mContext, ArrayList<OnePoolItem> list, setOnPoolClickListener mListener) {
        this.mContext = mContext;
        this.list = list;
        this.mListener = mListener;
    }

    interface setOnPoolClickListener{
        void OnPoolClicked(View v,int position);
    }
    @Override
    public OnePoolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.one_pool_item,parent,false);
        return  new PoolVieAdapter.OnePoolViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OnePoolViewHolder holder, int position) {
        holder.title.setText(list.get(position).getName());
        holder.address.setText(list.get(position).getPlace());
        Timestamp timestamp=new Timestamp(Long.parseLong(list.get(position).getDateTime()));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
        holder.time.setText(simpleDateFormat.format(timestamp));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OnePoolViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView address,time;
        CardView  cardView;
        public OnePoolViewHolder(View itemView) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.PoolName);
            address=(TextView) itemView.findViewById(R.id.poolLocation);
            time=(TextView) itemView.findViewById(R.id.poolTimeAndDate);
            cardView=(CardView) itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.OnPoolClicked(v,getAdapterPosition());
        }
    }
}
