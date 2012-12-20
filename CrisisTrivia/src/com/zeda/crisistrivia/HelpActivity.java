package com.zeda.crisistrivia;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

public class HelpActivity extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		AdView adView = (AdView) this.findViewById(R.id.adviewHelp);
		AdRequest adRequest = new AdRequest();
//		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
//		adRequest.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB");
		adView.loadAd(adRequest);
		
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.helpLayout1);
		rl.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {
		finish();
	}

}
