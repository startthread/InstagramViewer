package com.codepath.startthread.photosnow.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ListView;

import com.codepath.startthread.photosnow.R;
import com.codepath.startthread.photosnow.adapters.InstagramPhotosAdapter;
import com.codepath.startthread.photosnow.models.InstagramItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";

	public static final String CLIENT_ID = "3aebcb12934049968494ecf5fafa8c36";

	private SwipeRefreshLayout mSwipeContainer;

	private List<InstagramItem> mPhotos = new ArrayList<InstagramItem>();
	private InstagramPhotosAdapter mPhotosAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPhotosAdapter = new InstagramPhotosAdapter(this, mPhotos);

		final ListView lvTimeline = (ListView) findViewById(R.id.lvTimeline);
		lvTimeline.setAdapter(mPhotosAdapter);

		fetchPopularPhotos();

		mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		mSwipeContainer.setColorSchemeResources(
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_blue_bright,				
				android.R.color.holo_red_light);
		
		mSwipeContainer
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						fetchPopularPhotos();
					}
				});
	}

	private void fetchPopularPhotos() {

		// setup endpoint
		final String popularUrl = "https://api.instagram.com/v1/media/popular?client_id="
				+ CLIENT_ID;

		// network request
		Log.d(TAG, "requesting popular photos");
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(popularUrl, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String response, Throwable e) {
				Log.e(TAG, "failed to fetch data", e);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Log.i(TAG, "successfully received response");

				mSwipeContainer.setRefreshing(false);
				JSONArray photosJSON;

				try {
					photosJSON = response.getJSONArray("data");

					mPhotos.clear();
					mPhotos.addAll(InstagramItem.fromJson(photosJSON));
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
