package com.example.projectapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectapplication.R;
import com.example.projectapplication.model.Item;

import java.util.List;

public class MyCustomListAdapter extends ArrayAdapter<Item> {
    private Context mCtx;
    private int resource;
    private List<Item> itemList;

    public MyCustomListAdapter(Context mCtx, int resource, List<Item> itemList){
        super(mCtx,resource,itemList);
        this.mCtx=mCtx;
        this.resource=resource;
        this.itemList=itemList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(resource, null);

        TextView textViewName=view.findViewById(R.id.textViewName);
        TextView textViewDate=view.findViewById(R.id.textViewDate);
        TextView textViewAdults=view.findViewById(R.id.textViewAdults);
        TextView textViewCost=view.findViewById(R.id.textViewCost);
        ImageView imageViewAvatar=view.findViewById(R.id.imageViewAvatar);

        Item item=itemList.get(position);

        String tmp;//temporary string variable

        textViewName.setText(item.getName());
        tmp=item.getStartDate()+" - "+item.getEndDate();
        textViewDate.setText(tmp);
        textViewAdults.setText(String.valueOf(item.getAdults()));
        tmp=item.getMinCost()+" - "+item.getMaxCost();
        textViewCost.setText(tmp);
        if (item.getAvatar()!=null)
            imageViewAvatar.setImageURI(Uri.parse(item.getAvatar()));

        return view;
    }
}
