package com.zeda.crisistrivia;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TransitionActivity extends Activity implements View.OnClickListener {
	
	private class Message {
		private String msg;
		
		public Message(int points) {
			setMsg("Score: " + points + " points!");
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
	
	private class CustomTimerTask extends TimerTask {
		TransitionActivity ta;
		
		public CustomTimerTask (TransitionActivity _ta) {
			ta = _ta;
		}
		
		@Override
		public void run() {
			ta.closeView();
		}
		
	}
	
	public void closeView() {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transition);
		
		Message message = new Message(GameManager.getManager().getTotalPoints());
		
		TextView tv = (TextView) findViewById(R.id.pointsUpdate);
		tv.setText(message.getMsg());
		
		FrameLayout fl = (FrameLayout) findViewById(R.id.transitionFrame);
		fl.setOnClickListener(this);
		
//		Timer timer = new Timer();
//		timer.schedule(new CustomTimerTask(this), 2000);
	}

	@Override
	public void onClick(View arg0) {
		closeView();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_transition, menu);
//		return true;
//	}

}
