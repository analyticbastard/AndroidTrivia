package com.zeda.crisistrivia;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;

public class FinishActivity extends Activity implements View.OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish);		
		
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.finishLayout);
		rl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		finish();
	}

}
