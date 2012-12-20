package com.zeda.crisistrivia;

import com.zeda.crisistrivia.engine.GameManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


public class GameOverActivity extends Activity implements View.OnClickListener {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game_over);
		
		String score = getString(R.string.score_) + " " +
				GameManager.getManager().getTotalPoints();
		TextView tv = (TextView) findViewById(R.id.gameOverText1);
		tv.setText(tv.getText() + "\n" + score);
		
		FrameLayout fl = (FrameLayout) findViewById(R.id.gameOverLayout);		
		fl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}
