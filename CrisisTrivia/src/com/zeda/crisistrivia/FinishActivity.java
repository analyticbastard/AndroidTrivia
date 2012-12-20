package com.zeda.crisistrivia;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.zeda.crisistrivia.engine.Settings;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FinishActivity extends Activity implements View.OnClickListener {
	private Settings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish);
		
		AdView adView = (AdView) this.findViewById(R.id.adviewfinish);
		AdRequest adRequest = new AdRequest();
//		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
//		adRequest.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB");
		adView.loadAd(adRequest);
		
		Settings.getSettings().saveRank();
		
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.finishLayout);
		rl.setOnClickListener(this);
		
		settings = Settings.getSettings();
		
		String txt = settings.getName(0);
		TextView tv = (TextView) findViewById(R.id.finishName1);
		tv.setText(txt);

		txt = settings.getName(1);
		tv = (TextView) findViewById(R.id.finishName2);
		tv.setText(txt);

		txt = settings.getName(2);
		tv = (TextView) findViewById(R.id.finishName3);
		tv.setText(txt);

		int points = settings.getPoints(0);
		tv = (TextView) findViewById(R.id.finishPoints1);
		tv.setText("" + points);

		points = settings.getPoints(1);
		tv = (TextView) findViewById(R.id.finishPoints2);
		tv.setText("" + points);

		points = settings.getPoints(2);
		tv = (TextView) findViewById(R.id.finishPoints3);
		tv.setText("" + points);
	}

	@Override
	public void onClick(View v) {
		finish();
	}

}
