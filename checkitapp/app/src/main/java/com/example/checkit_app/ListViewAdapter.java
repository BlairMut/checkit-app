package com.example.checkit_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import android.content.Context;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<String> {

    private List<String> items = new ArrayList<>();
    private Context context;
    private CheckBox checkBox;

    public ListViewAdapter(List<String> items, Context context){
        super(context, R.layout.custom, items);
        this.context = context;
        this.items = items;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View row = inflater.inflate(R.layout.custom,parent,false);
        TextView item = row.findViewById(R.id.listText);
        item.setText(items.get(position));

        checkBox = row.findViewById(R.id.checkBox);

        //if(MainActivity.isActionMode){
        //    checkBox.setVisibility(View.VISIBLE);
        //}else if(MainActivity.isActionMode ){
        //    checkBox.setVisibility(View.GONE);
        //}

        return row;
    }

    public void removeItems(List<String> items){
        for(String item: items){
            items.remove(item);
        }

        notifyDataSetChanged();
    }
}