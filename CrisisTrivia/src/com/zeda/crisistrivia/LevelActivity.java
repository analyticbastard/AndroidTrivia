package com.zeda.crisistrivia;

import java.util.Timer;
import java.util.TimerTask;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.zeda.crisistrivia.engine.GameManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


public class LevelActivity extends Activity implements View.OnClickListener {
	
	private class TransitionTask extends TimerTask {
		LevelActivity levelactivity;
		
		private class UITransitionTask extends TimerTask {
			@Override
			public void run() {
				FrameLayout fl = (FrameLayout) findViewById(R.id.levelLayout);
				fl.setOnClickListener(levelactivity);
				
				TextView tv = (TextView) findViewById(R.id.levelTextView);
				tv.setText(tv.getText() + "\n\n" + 
				levelactivity.getString(R.string.tap));
			}
		}
		
		public TransitionTask(LevelActivity qp) {
			levelactivity = qp;
		}

		@Override
		public void run() {
			levelactivity.runOnUiThread(new UITransitionTask());
		}		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_level);
		
//		MoPubView mpv = (MoPubView) findViewById(R.id.adviewlevel);
//		mpv.setAdUnitId("7febfd1244ca11e2bf1612313d143c11");
//		mpv.loadAd();
//		mpv.bringToFront();
		
		AdView adView = (AdView) this.findViewById(R.id.adviewlevel);
		AdRequest adRequest = new AdRequest();
//		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
//		adRequest.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB");
		adView.loadAd(adRequest);
		
		if (GameManager.getManager().getLevel() == GameManager.LEVEL3) {
			TextView tv = (TextView) findViewById(R.id.levelTextView);
			tv.setText(this.getString(R.string.level_3));
		}
		
		if (GameManager.getManager().getLevel() == GameManager.LEVEL_FLASH) {
			TextView tv = (TextView) findViewById(R.id.levelTextView);
			tv.setText(this.getString(R.string.level_flash));
		}

		Timer timer = new Timer();
		timer.schedule(new TransitionTask(this), 3000);
		
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}
