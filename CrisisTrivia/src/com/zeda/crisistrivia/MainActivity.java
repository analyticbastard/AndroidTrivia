package com.zeda.crisistrivia;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
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

	@Override
	public void onClick(View v) {
		GameManager.getManager().resetGame();
		startActivityQuestion();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		instance = this;
		
		Settings settings = Settings.getSettings();
		
		setContentView(R.layout.activity_main);
		
		if (settings != null) {
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
		
		Button startButton = (Button) findViewById(R.id.buttonStart);		
		startButton.setOnClickListener(this);
		
		QuestionDataSource.createSource(startButton.getContext());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		GameManager manager = GameManager.getManager();
		
		if (requestCode == ACTIVITY_QUESTION) {
			if (manager.isFailed())
				startGameoverActivity();
			else if (!manager.isGameFinished())
				if (manager.getQuestionsAnswered() == 
						GameManager.QUESTIONS_LEVEL1 + 
						GameManager.QUESTIONS_LEVEL2 +
						GameManager.QUESTIONS_LEVEL3)
					startLevelActivity();
				else if (manager.getLevel() == GameManager.LEVEL_FLASH)
					startActivityQuestion();
				else
					startActivityTransition();
			else
				startFinishActivity();
		} else if (requestCode == ACTIVITY_TRANSITION) {
			if (!manager.isGameFinished()) {
				if ((manager.getQuestionsAnswered() == GameManager.QUESTIONS_LEVEL1)
						| (manager.getQuestionsAnswered() == 
						GameManager.QUESTIONS_LEVEL1 + 
						GameManager.QUESTIONS_LEVEL2)
						| (manager.getQuestionsAnswered() == 
						GameManager.QUESTIONS_LEVEL1 + 
						GameManager.QUESTIONS_LEVEL2 +
						GameManager.QUESTIONS_LEVEL3)   ) {
					startLevelActivity();
				} else {
					startActivityQuestion();
				}
			} else
				startFinishActivity();
		} else if (requestCode == ACTIVITY_LEVEL2) {
			startActivityQuestion();
		} else {
			// Return to game: do nothing
		}
	}
	
	public void exit() {
		finish();
	}
	
	public static MainActivity getInstance() {
		return instance;
	}

}
