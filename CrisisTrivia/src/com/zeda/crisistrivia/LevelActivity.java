package com.zeda.crisistrivia;

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
