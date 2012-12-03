package com.zeda.crisistrivia;

import com.zeda.crisistrivia.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;


public class LevelActivity extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_level);

		FrameLayout fl = (FrameLayout) findViewById(R.id.levelLayout);
		fl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}
