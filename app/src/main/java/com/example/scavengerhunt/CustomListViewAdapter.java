package com.example.scavengerhunt;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Joyce on 2015/6/6.
 */
public class CustomListViewAdapter extends ArrayAdapter <ParseObject>{
    Context context;
    ViewHolder holder;

    public CustomListViewAdapter (Context context, int resourceId, List<ParseObject> items){
        super(context, resourceId, items);
        this.context = context;
    }

    //private view holder class
    private class ViewHolder{
        ImageView gameCover;
        TextView gameTitle;
    }

    public View getView(int position, View convertView, ViewGroup parent ){

        ParseObject oneGame = getItem(position);
        //LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            //convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder.gameTitle = (TextView)convertView.findViewById(R.id.gameTitle);
            holder.gameCover = (ImageView)convertView.findViewById(R.id.gameIcon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.gameTitle.setText(oneGame.getString("gameName"));
        ParseFile image = oneGame.getParseFile("gameCover");
        image.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e == null) {
                    Bitmap gameImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.gameCover.setImageBitmap(gameImage);
                }
            }

        });

        return convertView;
    }
}
