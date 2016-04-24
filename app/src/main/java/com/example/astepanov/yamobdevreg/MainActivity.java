package com.example.astepanov.yamobdevreg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.ListView;

import com.example.astepanov.yamobdevreg.adapter.CustomListAdapter;
import com.example.astepanov.yamobdevreg.app.AppController;
import com.example.astepanov.yamobdevreg.model.*;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;


public class MainActivity extends AppCompatActivity {

    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
    // Artists json url
    private static final String url = "http://download.cdn.yandex.net/mobilization-2016/artists.json";

    private ProgressDialog pDialog;
    private List<Artist> artistList = new ArrayList<Artist>();
    private ListView listView;
    private CustomListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        // Setting up list view by custom list adapter
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, artistList);
        listView.setAdapter(adapter);

        // Showing progress dialog before making http request
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        // Creating volley request obj, getting json from url
        JsonArrayRequest artistReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json and store data into an ArrayList as Artist objects
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Artist a = new Artist();

                                a.setId(obj.getInt("id"));
                                a.setName(obj.getString("name"));

                                JSONArray genreArry = obj.getJSONArray("genres");
                                ArrayList<String> genres = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genres.add((String) genreArry.get(j));
                                }
                                a.setGenres(genres);

                                a.setTracks(obj.getInt("tracks"));
                                a.setAlbums(obj.getInt("albums"));
                                //a.setLink(obj.getString("link")); // not used anywhere
                                a.setDescription(obj.getString("description"));

                                JSONObject objCover = obj.getJSONObject("cover");
                                Cover c = new Cover();
                                c.setSmall(objCover.getString("small"));
                                c.setBig(objCover.getString("big"));
                                a.setCover(c);

                                // adding artist to artists array
                                artistList.add(a);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(artistReq);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}
