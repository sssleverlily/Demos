package com.example.administrator.three;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by Administrator on 2018/4/3.
 */

public class PhotoAdapter extends BaseAdapter {
  //  private BitmapUtils mBitmapUtils;
    private String mImageViews[];
    private MyUtils utils;
    public PhotoAdapter() {
        utils = new MyUtils();
    }

    @Override
    public int getCount() {
        return mImageViews.length;
    }

    @Override
    public Object getItem(int position) {
        return mImageViews[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(),R.layout.list_item,null);
            holder.tvImage = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        utils.display(holder.tvImage,mImageViews[position]);
        return convertView;
    }
}

    class ViewHolder{
    ImageView tvImage;
}

