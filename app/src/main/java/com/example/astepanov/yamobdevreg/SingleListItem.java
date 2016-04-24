package com.example.astepanov.yamobdevreg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.astepanov.yamobdevreg.app.AppController;


// Display selected list item in SingleListItem activity
public class SingleListItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_list_item);

        NetworkImageView fullimage = (NetworkImageView) findViewById(R.id.fullimage);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        TextView genres = (TextView) findViewById(R.id.genres);
        TextView albumtracks = (TextView) findViewById(R.id.albumtracks);
        TextView biography = (TextView) findViewById(R.id.biography);

        Intent i = getIntent();

        setTitle(i.getStringExtra("name"));
        fullimage.setImageUrl(i.getStringExtra("fullimage"), imageLoader);
        genres.setText(i.getStringExtra("genres"));
        albumtracks.setText(i.getStringExtra("albumtracks"));
        biography.setText(i.getStringExtra("biography"));

    }

    // If you want to return to the previous instance of an Activity by pressing of ActionBar home button, without recreating it, you can override getParentActivityIntent method to use the one from the back stack
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

}
