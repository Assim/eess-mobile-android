package com.masaaroman.eessmobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class StartActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_start);
		
		
		setProgressBarIndeterminateVisibility(true);
	}
}