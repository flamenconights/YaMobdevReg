package com.example.astepanov.yamobdevreg.adapter;

import com.example.astepanov.yamobdevreg.R;
import com.example.astepanov.yamobdevreg.SingleListItem;
import com.example.astepanov.yamobdevreg.app.AppController;
import com.example.astepanov.yamobdevreg.model.*;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;


// CustomListAdapter class provides data to list view. It renders the layout_row.xml in list by pre-filling appropriate information
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Artist> artistItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Artist> artistItems) {
        this.activity = activity;
        this.artistItems = artistItems;
    }

    @Override
    public int getCount() {
        return artistItems.size();
    }

    @Override
    public Object getItem(int location) {
        return artistItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        final TextView name = (TextView) convertView.findViewById(R.id.name);
        final TextView genres = (TextView) convertView.findViewById(R.id.genres);
        final TextView albumtracks = (TextView) convertView.findViewById(R.id.albumtracks);

        // getting Artist data for the row
        final Artist a = artistItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(a.getCover().getSmall(), imageLoader);

        // name
        name.setText(a.getName());

        // genres
        String genreStr = "";
        for (String str : a.getGenres()) {
            genreStr += str + ", ";
        }
        genreStr = genreStr.length() > 0 ? genreStr.substring(0, genreStr.length() - 2) : genreStr;
        genres.setText(genreStr);

        // albums, tracks
        String albumtracksStr = a.getAlbums().toString() + " альбомов, " + a.getTracks().toString() + " песен";
        albumtracks.setText(albumtracksStr);


        // listening to single list item on click
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // starting single item activity
                Intent in = new Intent(v.getContext(), SingleListItem.class);
                in.putExtra("name", name.getText());
                in.putExtra("fullimage", a.getCover().getBig());
                in.putExtra("genres", genres.getText());
                in.putExtra("albumtracks", albumtracks.getText());
                in.putExtra("biography", a.getDescription());

                v.getContext().startActivity(in);

            }
        });

        return convertView;
    }

}