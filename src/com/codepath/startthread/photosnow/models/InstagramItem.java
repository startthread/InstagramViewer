package com.codepath.startthread.photosnow.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InstagramItem {
	public String username;
	public String profilePicUrl;
	public String caption;
	public String url;
	public int height;
	public int likesCount;
	public long createdTime;
	
	
	// data -> [i] -> user -> username
	// data -> [i] -> user -> profile_picture
	// data -> [i] -> caption -> text
	// data -> [i] -> likes -> count
	// data -> [i] -> images -> standard_resolution -> url
	// data -> [i] -> images -> standard_resolution -> height
	public static List<InstagramItem> fromJson(JSONArray photosJSON) throws JSONException {
		final List<InstagramItem> photos = new ArrayList<InstagramItem>();
		
		for (int i = 0; i < photosJSON.length(); i++) {
			JSONObject photoJSON = photosJSON.getJSONObject(i);
			InstagramItem photo = new InstagramItem();

			photo.username = photoJSON.getJSONObject("user")
					.getString("username");
			photo.profilePicUrl = photoJSON.getJSONObject("user")
					.getString("profile_picture");

			if (photoJSON.optJSONObject("caption") != null) {
				photo.caption = photoJSON.getJSONObject("caption")
						.getString("text");
			}

			JSONObject likesJSON = photoJSON.getJSONObject("likes");
			if (likesJSON != null) {
				photo.likesCount = likesJSON.getInt("count");
			}

			photo.url = photoJSON.getJSONObject("images")
					.getJSONObject("standard_resolution")
					.getString("url");
			photo.height = photoJSON.getJSONObject("images")
					.getJSONObject("standard_resolution")
					.getInt("height");

			photos.add(photo);
		}
		
		return photos;
	}
}
