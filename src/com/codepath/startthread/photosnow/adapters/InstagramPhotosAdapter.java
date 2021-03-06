package com.codepath.startthread.photosnow.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.startthread.photosnow.R;
import com.codepath.startthread.photosnow.models.InstagramItem;
import com.squareup.picasso.Picasso;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramItem> {

	private String mLikesString;
	
	public InstagramPhotosAdapter(Context context, List<InstagramItem> photos) {
		super(context, R.layout.timeline_list_item, photos);
		mLikesString = context.getResources().getString(R.string.likes);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final InstagramItem photo = getItem(position);
		
		ViewHolder viewHolder;
		
		if (convertView == null) {
			viewHolder = new ViewHolder();
			
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.timeline_list_item, 
					parent, false);
			
			viewHolder.photo = (ImageView) convertView.findViewById(R.id.ivPhoto);
			viewHolder.caption = (TextView) convertView.findViewById(R.id.tvCaption);
			viewHolder.profilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);
			viewHolder.username = (TextView) convertView.findViewById(R.id.tvUsername);
			viewHolder.likes = (TextView) convertView.findViewById(R.id.tvLikes);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		// set profile pic
		viewHolder.profilePic.setImageResource(0);
		Picasso.with(getContext()).load(photo.profilePicUrl).resize(800, 800)
					.centerInside().into(viewHolder.profilePic);
		
		viewHolder.username.setText(photo.username);
		viewHolder.likes.setText(photo.likesCount + " " + mLikesString);
		
		int color = getContext().getResources().getColor(R.color.instagram_blue);
		final SpannableString caption = new SpannableString(photo.username + " " + photo.caption);
		caption.setSpan(new StyleSpan(Typeface.BOLD), 0, photo.username.length(), 0);
		caption.setSpan(new ForegroundColorSpan(color), 0, photo.username.length(), 0);
		
		viewHolder.caption.setText(caption);
		
		// set popular photo
		//viewHolder.photo.getLayoutParams().height = photo.height;
		viewHolder.photo.setImageResource(0);
		Picasso.with(getContext()).load(photo.url).resize(800, 800)
					.centerInside().into(viewHolder.photo);
		
		return convertView;
	}
	
	
	private static class ViewHolder {
		ImageView photo;
		TextView caption;
		ImageView profilePic;
		TextView username;
		TextView likes;
	}
	

}
