package com.zeda.crisistrivia;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;

public class HelpActivity extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.helpLayout1);
		rl.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {
		finish();
	}

}
