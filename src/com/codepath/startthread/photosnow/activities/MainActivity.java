package com.codepath.startthread.photosnow.activities;

import java.util.ArrayList;


import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codepath.startthread.photosnow.R;
import com.codepath.startthread.photosnow.R.id;
import com.codepath.startthread.photosnow.R.layout;
import com.codepath.startthread.photosnow.adapters.InstagramPhotosAdapter;
import com.codepath.startthread.photosnow.models.InstagramPhoto;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	
	public static final String CLIENT_ID = "3aebcb12934049968494ecf5fafa8c36";
	
	private ArrayList<InstagramPhoto> mPhotos = new ArrayList<InstagramPhoto>();
	private InstagramPhotosAdapter mPhotosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mPhotosAdapter = new InstagramPhotosAdapter(this, mPhotos);
        
        final ListView lvTimeline = (ListView) findViewById(R.id.lvTimeline);
        lvTimeline.setAdapter(mPhotosAdapter);
        
        fetchPopularPhotos();
    }

	private void fetchPopularPhotos() {
		
		// setup endpoint
		final String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
		
		// network request
		Log.d(TAG, "requesting popular photos");
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(popularUrl, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
				Log.e(TAG, "failed to fetch data", e);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.i(TAG, "successfully received response");
				
				JSONArray photosJSON;
				
				try {
					// data -> [i] -> user -> username
					// data -> [i] -> caption -> text
					// data -> [i] -> likes -> count
					// data -> [i] -> images -> standard_resolution -> url
					// data -> [i] -> images -> standard_resolution -> height
				
					photosJSON = response.getJSONArray("data");
					
					mPhotos.clear();
					
					for (int i=0; i<photosJSON.length(); i++) {
						JSONObject photoJSON = photosJSON.getJSONObject(i);
						InstagramPhoto photo = new InstagramPhoto();
						
						photo.username = photoJSON.getJSONObject("user").getString("username");
						//JSONObject captionJSON = photoJSON.getJSONObject("caption");
						if (!photoJSON.isNull("caption")) {
							photo.caption = photoJSON.getJSONObject("caption").getString("text");
						}
						
						JSONObject likesJSON = photoJSON.getJSONObject("likes");
						if (likesJSON != null) {
							photo.likesCount = likesJSON.getInt("count");
						}
						
						photo.url = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
						photo.height = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
						
						mPhotos.add(photo);
					}
					mPhotosAdapter.notifyDataSetChanged();
					 
				} catch (JSONException e) {
					Log.w(TAG, "failed to parse JSON response", e);
				}
			}
			
		});
		
		// handle response
		
		// show data
	}
}
