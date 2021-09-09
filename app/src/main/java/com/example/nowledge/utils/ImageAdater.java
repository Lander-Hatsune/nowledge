package com.example.nowledge.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.example.nowledge.R;

import java.util.List;

public class ImageAdater extends BaseAdapter {

    private Context context;
    private List<Bitmap> images;
    private int resourceID;

    public ImageAdater(Context ctx, List<Bitmap> src, int id) {
        context = ctx; images = src; resourceID = id;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Bitmap getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resourceID, null);
            viewHolder = new ViewHolder();
            viewHolder.imgVIew = view.findViewById(R.id.entityImage);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.imgVIew.setImageBitmap(images.get(i));
        return view;
    }

    class ViewHolder{
        ImageView imgVIew;
    }
}
