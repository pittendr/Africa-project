package com.crop_sense.farmerinterfaceapplication;

import android.content.Context;
import android.graphics.Bitmap;
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

public class ListViewAdapter extends BaseAdapter{

    private Context context;

    private List<String> videoList;
    private List<Bitmap> thumbList;
    private ArrayList<String> arraylist;

    public ListViewAdapter(Context ctxt, List<String> vdLst, List<Bitmap> tbLst) {
        this.context = ctxt;
        this.videoList= vdLst;
        this.thumbList = tbLst;
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
        Bitmap thumb = thumbList.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listtextview, null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.titlerow);
        ImageView image = (ImageView) convertView.findViewById(R.id.thumbnail);
        image.setImageBitmap(thumb);
        title.setText(entry);
        title.setTypeface(type);

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