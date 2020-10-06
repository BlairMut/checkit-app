package com.example.checkit_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View row = inflater.inflate(R.layout.custom,parent,false);
        TextView item = row.findViewById(R.id.listText);
        item.setText(items.get(position));

        checkBox = row.findViewById(R.id.checkBox);
        checkBox.setTag(position);

        if(MainActivity.isActionMode){
            checkBox.setVisibility(View.VISIBLE);
        }else{
            checkBox.setVisibility(View.GONE);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int postion = (int)compoundButton.getTag();

                if(MainActivity.userSelection.contains(items.get(position))){
                    MainActivity.userSelection.remove(items.get(position));
                }else{
                    MainActivity.userSelection.add(items.get(position));
                }

                MainActivity.actionMode.setTitle(MainActivity.userSelection.size()+ " items selected...");
            }
        });

        return row;
    }
    //Removing items from the list view
    public void removeItems(List<String> stuff){
        for(String item: stuff){
            items.remove(item);
        }
        notifyDataSetChanged();
    }
}