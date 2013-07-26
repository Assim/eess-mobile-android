package com.masaaroman.eessmobile.model;

import android.widget.ProgressBar;

public class ProgressBarUpdater {
	private ProgressBar progressBar;
	private int totalCount;
	private int currentCount;
	
	public ProgressBarUpdater(ProgressBar progressBar, int totalCount) {
		this.progressBar = progressBar;
		this.totalCount = totalCount;
	}
	
	public void increment() {
		progressBar.setProgress((int) ((++currentCount / (float) totalCount) * 100));
	}
}