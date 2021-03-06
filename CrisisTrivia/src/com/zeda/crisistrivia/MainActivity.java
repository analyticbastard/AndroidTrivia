package com.zeda.crisistrivia;


import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.zeda.crisistrivia.database.QuestionDataSource;
import com.zeda.crisistrivia.engine.GameManager;
import com.zeda.crisistrivia.engine.Settings;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {	
	public static final int ACTIVITY_QUESTION = 1;
	public static final int ACTIVITY_TRANSITION = 2;
	public static final int ACTIVITY_LEVEL2 = 11;
	public static final int ACTIVITY_LEVEL3 = 12;
	public static final int ACTIVITY_LEVEL_FLASH = 13;
	public static final int ACTIVITY_GAMEOVER = 9998;
	public static final int ACTIVITY_FINISH = 9999;
	
	private static MainActivity instance;
	
	private Settings settings;
	
	
	private void startActivityQuestion() {
		Intent intent = new Intent();
		String packageName =
				"com.zeda.crisistrivia";
		String className =
				"com.zeda.crisistrivia.QuestionPanel";
		intent.setClassName(packageName, className);
		startActivityForResult(intent, ACTIVITY_QUESTION);
	}
	
	public void startActivityTransition() {
		Intent intent = new Intent();
		String packageName =
				"com.zeda.crisistrivia";
		String className =
				"com.zeda.crisistrivia.TransitionActivity";
		intent.setClassName(packageName, className);
		startActivityForResult(intent, ACTIVITY_TRANSITION);
	}
	
	public void startLevelActivity() {
		Intent intent = new Intent();
		String packageName =
				"com.zeda.crisistrivia";
		String className =
				"com.zeda.crisistrivia.LevelActivity";
		intent.setClassName(packageName, className);
		startActivityForResult(intent, ACTIVITY_LEVEL2);
	}
	
	public void startGameoverActivity() {
		Intent intent = new Intent();
		String packageName =
				"com.zeda.crisistrivia";
		String className =
				"com.zeda.crisistrivia.GameOverActivity";
		intent.setClassName(packageName, className);
		startActivityForResult(intent, ACTIVITY_GAMEOVER);
	}
	
	public void startFinishActivity() {
		Intent intent = new Intent();
		String packageName =
				"com.zeda.crisistrivia";
		String className =
				"com.zeda.crisistrivia.FinishActivity";
		intent.setClassName(packageName, className);
		startActivityForResult(intent, ACTIVITY_FINISH);
	}
	
	public void startHelpActivity() {
		Intent intent = new Intent();
		String packageName =
				"com.zeda.crisistrivia";
		String className =
				"com.zeda.crisistrivia.HelpActivity";
		intent.setClassName(packageName, className);
		startActivityForResult(intent, ACTIVITY_FINISH);
	}
	
	public void startEmailQuestionActivity() {
		Intent intent = new Intent();
		String packageName =
				"com.zeda.crisistrivia";
		String className =
				"com.zeda.crisistrivia.EmailQuestionActivity";
		intent.setClassName(packageName, className);
		startActivityForResult(intent, ACTIVITY_FINISH);
	}

	@Override
	public void onClick(View v) {
		GameManager.getManager().resetGame();
		startActivityQuestion();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		instance = this;
		
		settings = Settings.getSettings();
		
		setContentView(R.layout.activity_main);

		String txt = settings.getName(0);
		TextView tv = (TextView) findViewById(R.id.mainName1);
		tv.setText(txt);

		txt = settings.getName(1);
		tv = (TextView) findViewById(R.id.mainName2);
		tv.setText(txt);

		txt = settings.getName(2);
		tv = (TextView) findViewById(R.id.mainName3);
		tv.setText(txt);

		int points = settings.getPoints(0);
		tv = (TextView) findViewById(R.id.mainPoints1);
		tv.setText("" + points);

		points = settings.getPoints(1);
		tv = (TextView) findViewById(R.id.mainPoints2);
		tv.setText("" + points);

		points = settings.getPoints(2);
		tv = (TextView) findViewById(R.id.mainPoints3);
		tv.setText("" + points);
		
		Button startButton = (Button) findViewById(R.id.buttonStart);		
		startButton.setOnClickListener(this);
		
		QuestionDataSource.createSource(startButton.getContext());
		
//		MoPubView mpv = (MoPubView) findViewById(R.id.adview);
//		mpv.setAdUnitId("7febfd1244ca11e2bf1612313d143c11");
//		mpv.loadAd();
//		mpv.bringToFront();
		
		// Conversion tracking call
//	    new MoPubConversionTracker().reportAppOpen(this);
		
		AdView adView = (AdView) this.findViewById(R.id.adview);
		AdRequest adRequest = new AdRequest();
//		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
//		adRequest.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB");
		adView.loadAd(adRequest);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.menu_help:
        	startHelpActivity();
        	break;
        case R.id.menu_newquestion:
        	startEmailQuestionActivity();
        	break;
        case R.id.menu_reset:
        	Settings.getSettings().resetRank();
        	rewriteRank();
        	break;
        }
        
        return super.onOptionsItemSelected(item);
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Return to main window: rewrite rank
		rewriteRank();
	}
	
	public void rewriteRank() {
		String txt = settings.getName(0);
		TextView tv = (TextView) findViewById(R.id.mainName1);
		tv.setText(txt);

		txt = settings.getName(1);
		tv = (TextView) findViewById(R.id.mainName2);
		tv.setText(txt);

		txt = settings.getName(2);
		tv = (TextView) findViewById(R.id.mainName3);
		tv.setText(txt);

		int points = settings.getPoints(0);
		tv = (TextView) findViewById(R.id.mainPoints1);
		tv.setText("" + points);

		points = settings.getPoints(1);
		tv = (TextView) findViewById(R.id.mainPoints2);
		tv.setText("" + points);

		points = settings.getPoints(2);
		tv = (TextView) findViewById(R.id.mainPoints3);
		tv.setText("" + points);
	}
	
	public void exit() {
		finish();
	}
	
	public static MainActivity getInstance() {
		return instance;
	}

}
