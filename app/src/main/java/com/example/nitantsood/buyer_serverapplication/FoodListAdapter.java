package com.example.nitantsood.buyer_serverapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by NITANT SOOD on 23-10-2017.
 */

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.OneFoodItemHolder> {
    private Context mContext;
    ArrayList<OneFoodItem> list=new ArrayList<>();
    onFoodItemClickListener mListener;

    public FoodListAdapter(Context mContext, ArrayList<OneFoodItem> list, onFoodItemClickListener mListener) {
        this.mContext = mContext;
        this.list = list;
        this.mListener = mListener;
    }

    interface onFoodItemClickListener{
        public void onFoodItemClicked(View view,OneFoodItem selectedFood);
    }
    @Override
    public FoodListAdapter.OneFoodItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.one_food_item_view,parent,false);
        return  new OneFoodItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FoodListAdapter.OneFoodItemHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.progressBar.setVisibility(View.VISIBLE);
        if(list.get(position).getPrice().equals("0")){
            holder.Price.setText("Free of Cost");
        }
        else {
            holder.Price.setText("Rs. " + list.get(position).getPrice() + "/-");
        }
        holder.Quantity.setText("Qty: "+list.get(position).getQuantity());
        holder.Distance.setText(""+list.get(position).getDistance()+" away");
        holder.itemView.setTag(list.get(position));
        if(!list.get(position).getPicture_URL().equals("none")) {
            Picasso.with(mContext).load(list.get(position).getPicture_URL()).into(holder.image, new Callback() {
                @Override
                public void onSuccess() {
                    holder.progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OneFoodItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView  title,Distance,Price,Quantity,clickHere;
        public ImageView image;
        public ProgressBar progressBar;
        public OneFoodItemHolder(View itemView) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.foodTitle);
            Distance=(TextView) itemView.findViewById(R.id.foodDistance);
            Price=(TextView) itemView.findViewById(R.id.foodPrice);
            Quantity=(TextView) itemView.findViewById(R.id.foodQuantity);
            image=(ImageView) itemView.findViewById(R.id.foodImaage);
            progressBar=itemView.findViewById(R.id.progressBar3);
            clickHere=itemView.findViewById(R.id.clickHere);
            itemView.setOnClickListener(this);
            Distance.setOnClickListener(this);
            clickHere.setOnClickListener(this);
            Distance.setTextColor(Color.BLUE);
        }

        @Override
        public void onClick(View v) {
            mListener.onFoodItemClicked(v,list.get(getAdapterPosition()));
        }
    }
}
