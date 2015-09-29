package com.crop_sense.farmerinterfaceapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VideoListViewAdapter extends BaseAdapter{

    private Context context;

    private List<String> videoList;
    private ArrayList<String> arraylist;

    public VideoListViewAdapter(Context ctxt, List<String> vdLst) {
        this.context = ctxt;
        this.videoList= vdLst;
        this.arraylist = new ArrayList<String>();
        this.arraylist.addAll(vdLst);

    }

    public int getCount() {
        return videoList.size();
    }
    public Object getItem(int position){
        return videoList.get(position);
    }
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Typeface type = Typeface.createFromAsset(context.getAssets(), "myriadproregular.otf");

        String entry = videoList.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listvideoview, null);
        }
        TextView text1 = (TextView) convertView.findViewById(R.id.text1);
        text1.setText(entry);
        text1.setTypeface(type);

        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        videoList.clear();
        if (charText.length() == 0) {
            videoList.addAll(arraylist);
        }
        else
        {
            for (String wp : arraylist)
            {
                if (wp.toLowerCase(Locale.getDefault()).contains(charText) )
                {
                    videoList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }




}