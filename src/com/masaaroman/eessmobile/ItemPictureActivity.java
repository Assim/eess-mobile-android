package com.masaaroman.eessmobile;

import java.io.InputStream;

import com.masaaroman.eessmobile.model.Item;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class ItemPictureActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_picture);
		
		TextView itemName = (TextView)findViewById(R.id.itemName);
		ProgressBar progressBar = (ProgressBar)findViewById(R.id.itemImageProgressBar);
		ImageView imageView = (ImageView)findViewById(R.id.itemImage);
		
		imageView.setVisibility(View.GONE);
		
		DatabaseAdapter db = new DatabaseAdapter(this);
		int itemId = (int) getIntent().getExtras().getLong("item_id");
		Item item = db.getItem(itemId);
		
		itemName.setText("Item Picture: "+item.getName());
		
		new DownloadImageTask(imageView, progressBar).execute(item.getPicture());
	}
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	ImageView imageView;
	ProgressBar progressBar;

	public DownloadImageTask(ImageView imageView, ProgressBar progressBar) {
		this.imageView = imageView;
		this.progressBar = progressBar;
		}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		imageView.setImageBitmap(result);
		imageView.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
	}
}