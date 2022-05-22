package com.example.kalorimetre;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class Adapter extends ArrayAdapter<Meals> {

    Context context;
    List<Meals> arrayListMeals;

    public Adapter(@NonNull Context context, List<Meals> arrayListMeals) {
        super(context, R.layout.list_item, arrayListMeals);
        this.context = context;
        this.arrayListMeals = arrayListMeals;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,null,true);
        TextView product = view.findViewById(R.id.tvproduct);
        TextView calorie = view.findViewById(R.id.tvcalorie);
        TextView tvnumber = view.findViewById(R.id.tvnumber);

        tvnumber.setText(arrayListMeals.get(position).getListID());
        product.setText(arrayListMeals.get(position).getProducts());
        calorie.setText(arrayListMeals.get(position).getCalorie());

        return view;
    }
}
