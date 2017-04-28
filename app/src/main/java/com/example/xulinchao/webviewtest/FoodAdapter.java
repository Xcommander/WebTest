package com.example.xulinchao.webviewtest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by xulinchao on 2017/4/27.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodHolder> {
    private List<Food> foodList;

    public FoodAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @Override
    public FoodHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        final FoodHolder foodHolder = new FoodHolder(view);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Food food ;
                food = foodList.get(foodHolder.getAdapterPosition());
                Toast.makeText(parent.getContext(), "所点餐为:" + food.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return foodHolder;
    }

    @Override
    public void onBindViewHolder(FoodHolder holder, int position) {
        Food food=foodList.get(position);
        holder.fooName.setText(food.getName());
        holder.foodPrice.setText(food.getPrice());
        holder.foodDes.setText(food.getDescription());

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    static class FoodHolder extends RecyclerView.ViewHolder {
        TextView fooName;
        TextView foodPrice;
        TextView foodDes;

        public FoodHolder(View itemView) {
            super(itemView);
            fooName = (TextView) itemView.findViewById(R.id.food_name);
            foodPrice = (TextView) itemView.findViewById(R.id.food_price);
            foodDes = (TextView) itemView.findViewById(R.id.food_des);
        }
    }
}
