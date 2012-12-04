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
import android.widget.TextView;


public class LevelActivity extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_level);
		
		if (GameManager.getManager().getLevel() == GameManager.LEVEL3) {
			TextView tv = (TextView) findViewById(R.id.levelTextView);
			tv.setText(this.getString(R.string.level_3));
		}			

		FrameLayout fl = (FrameLayout) findViewById(R.id.levelLayout);
		fl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}
