package com.zeda.crisistrivia;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class QuestionPanel extends Activity {

	ImageView iv;
	Timer timer = new Timer();
	
	Question question;
	
	
	private abstract class AdapterClick implements View.OnClickListener {
		QuestionPanel questionpanel;

		Button b;
		Button bo1;
		Button bo2;
		
		Drawable d;
		
		protected AdapterClick(QuestionPanel qp, Button _b, Button _bo1, 
				Button _bo2, Drawable _d) {
			questionpanel = qp;
			b =_b;
			bo1 = _bo1;
			bo2 = _bo2;
			d = _d;
		}		
		
		@Override
		public void onClick(View arg0) {
			b.setOnClickListener(null);
			bo1.setOnClickListener(null);
			bo2.setOnClickListener(null);
		
			questionpanel.getTimer().cancel();
			questionpanel.getTimer().purge();
			
			ImageView iv = questionpanel.getIv();
			
			LinearLayout ll = (LinearLayout) findViewById(R.id.answersLayout);
			int left = Math.round((ll.getX() + b.getX() + (b.getWidth()/2) 
					- (d.getIntrinsicWidth()/2)));
			int top = Math.round((ll.getY() + b.getY() + (b.getHeight()/2) 
					- (d.getIntrinsicHeight()/2)));
			
			iv.setImageDrawable(d);
			//iv.setTranslationX(left);
			//iv.setTranslationY(top);
			LayoutParams lp = new LayoutParams(d.getIntrinsicWidth(), 
					d.getIntrinsicHeight());
			lp.setMargins(left, top, 0, 0);
			iv.setLayoutParams(lp);
			iv.bringToFront();
			iv.setVisibility(ImageView.VISIBLE);
			
			GameManager.getManager().advanceQuestionsAnswered();
		}
	}
	
	private class AdapterRight extends AdapterClick {		
		protected AdapterRight(QuestionPanel qp, Button _b, Button _bo1, Button _bo2) {
			super(qp, _b, _bo1, _bo2, 
					qp.getIv().getResources().getDrawable(R.drawable.right));
		}
		
		@Override
		public void onClick(View arg0) {
			super.onClick(arg0);
			
			GameManager.getManager().addPoints();

			questionpanel.finish();
		}
	}
	
	private class AdapterWrong extends AdapterClick {		
		protected AdapterWrong(QuestionPanel qp, Button _b, Button _bo1, Button _bo2) {
			super(qp, _b, _bo1, _bo2,
					qp.getIv().getResources().getDrawable(R.drawable.wrong));
		}
		
		@Override
		public void onClick(View arg0) {
			super.onClick(arg0);
			
			questionpanel.finish();
		}
	}
	
	private class CustomTimerTask extends TimerTask {
		private static final int TIME_MAX = GameManager.QUESTION_TIME;
		
		QuestionPanel qp;
		private ProgressBar progressbar = null;
		private Timer timer;
		
		private int progress = 0;
		
		public CustomTimerTask (Timer _timer, QuestionPanel _qp) {
			qp = _qp;
			timer = _timer;
			progressbar = (ProgressBar) findViewById(R.id.progressBar1);
			progressbar.setMax(TIME_MAX);
			progressbar.setProgress(0);
		}

		@Override
		public void run() {
			if (progress < TIME_MAX) {
				progress++;
				progressbar.setProgress(progress);
			} else {
				timer.cancel();
				timer.purge();

				GameManager.getManager().advanceQuestionsAnswered();
				
				// Finish activity
				qp.finish();
			}
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_questions);
		
		Question q = GameManager.getManager().getQuestion();
		question = q;
		
		iv = (ImageView) findViewById(R.id.floatingMark);
		
		Button button1=(Button) findViewById(R.id.answerButton1);
		Button button2=(Button) findViewById(R.id.answerButton2);
		Button button3=(Button) findViewById(R.id.answerButton3);
		AdapterClick a1, a2, a3;
		if (q.getOk() == 1) {
			a1 = new AdapterRight(this, button1, button2, button3);
			a2 = new AdapterWrong(this, button2, button1, button3);
			a3 = new AdapterWrong(this, button3, button1, button2);
		} else if (q.getOk() == 2) {
			a1 = new AdapterWrong(this, button1, button2, button3);
			a2 = new AdapterRight(this, button2, button1, button3);
			a3 = new AdapterWrong(this, button3, button1, button2);
		} else {
			a1 = new AdapterWrong(this, button1, button2, button3);
			a2 = new AdapterWrong(this, button2, button1, button3);
			a3 = new AdapterRight(this, button3, button1, button2);
		}
		
		button1.setOnClickListener(a1);
		button2.setOnClickListener(a2);
		button3.setOnClickListener(a3);
		
		button1.setText(q.getAnswer1());
		button2.setText(q.getAnswer2());
		button3.setText(q.getAnswer3());
		
		TextView tv = (TextView) findViewById(R.id.questionText);
		tv.setText(q.getStatement());
		if (q.getImageID() != null) {
			int id = getResources().getIdentifier(getPackageName() 
					+ ":drawable/" + q.getImageID(), null, null);
			if (id>0) {
				tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, id);
			}
		}
		
		// Start timer here to timeout and to update progress bar
		CustomTimerTask ct = new CustomTimerTask(timer, this);
		timer.schedule(ct, 0, 500);
	}
	
	public void onDestroy() {
		super.onDestroy();
		
		synchronized(this) {
			try {
				this.wait(200);
			} catch (InterruptedException e) {
				Log.w("QuestionPanel", "Wait interrupted: " + e.getMessage());
			}
		}
	}
	
	public ImageView getIv() {
		return iv;
	}

	public void setIv(ImageView iv) {
		this.iv = iv;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

}
