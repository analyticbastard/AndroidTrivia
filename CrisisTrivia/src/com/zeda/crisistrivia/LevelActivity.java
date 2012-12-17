package com.zeda.crisistrivia;

import com.mopub.mobileads.MoPubView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


public class LevelActivity extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_level);
		
		MoPubView mpv = (MoPubView) findViewById(R.id.adviewlevel);
		mpv.setAdUnitId("7febfd1244ca11e2bf1612313d143c11");
		mpv.loadAd();
		mpv.bringToFront();
		
		if (GameManager.getManager().getLevel() == GameManager.LEVEL3) {
			TextView tv = (TextView) findViewById(R.id.levelTextView);
			tv.setText(this.getString(R.string.level_3));
		}
		
		if (GameManager.getManager().getLevel() == GameManager.LEVEL_FLASH) {
			TextView tv = (TextView) findViewById(R.id.levelTextView);
			tv.setText(this.getString(R.string.level_flash));
		}

		FrameLayout fl = (FrameLayout) findViewById(R.id.levelLayout);
		fl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}
