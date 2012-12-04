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

public class MainActivity extends Activity implements OnClickListener {
	public static final String PREFS_NAME = "config";
	
	public static final int ACTIVITY_QUESTION = 1;
	public static final int ACTIVITY_TRANSITION = 2;
	public static final int ACTIVITY_LEVEL2 = 11;
	public static final int ACTIVITY_LEVEL3 = 12;
	public static final int ACTIVITY_LEVEL_FLASH = 13;
	public static final int ACTIVITY_GAMEOVER = 9999;
	
	
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

	@Override
	public void onClick(View v) {
		GameManager.getManager().resetGame();
		startActivityQuestion();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button startButton = (Button) findViewById(R.id.buttonStart);
		
		startButton.setOnClickListener(this);
		
		QuestionDataSource.createSource(startButton.getContext());
		
		//SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		//level1QuestionsNo = settings.getInt("level1QuestionsNo", -1);
		
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
			else
				startActivityTransition();
		} else if (requestCode == ACTIVITY_TRANSITION) {
			if (manager.getQuestionsAnswered() < GameManager.QUESTIONS_IN_GAME) {
				if ((manager.getQuestionsAnswered() == GameManager.QUESTIONS_LEVEL1)
						| (manager.getQuestionsAnswered() == 
						GameManager.QUESTIONS_LEVEL1 + 
						GameManager.QUESTIONS_LEVEL2)) {
					startLevelActivity();
				} else {
					startActivityQuestion();
				}
			} else {
				// Return to game: do nothing
			}
		} else if (requestCode == ACTIVITY_LEVEL2) {
			startActivityQuestion();
		} else {
			// Return to game: do nothing
		}
	}

}
