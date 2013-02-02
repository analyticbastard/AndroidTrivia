package com.zeda.crisistrivia;


import java.util.Timer;
import java.util.TimerTask;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.zeda.crisistrivia.engine.GameManager;
import com.zeda.crisistrivia.engine.Question;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * QuestionPanel
 * 
 * Activity that hold the Question view
 * 
 * @author Gabo
 *
 */
public class QuestionPanel extends Activity implements View.OnClickListener {
	private final int TRANSITION_DELAY = 1500;

	ImageView iv;
	Timer timer = null;
	CustomTimerTask ct = null;	
	final Animation animation = new AlphaAnimation(1, 0); 
	// Change alpha from fully visible to invisible

	//MoPubView mpv = null;
	AdView adView = null;
	AdRequest adRequest = null;
	
	
	/*
	 * Abstract class to perform a click on any of the answer buttons
	 * disregarding whether it is the correct answer or not
	 */
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
			
			// Get the center of the button that was clicked to compute the
			// left and top offsets for the image to be painted
			LinearLayout ll = (LinearLayout) findViewById(R.id.answersLayout);
//			int left = Math.round((ll.getX() + b.getX() + (b.getWidth()/2) 
//					- (d.getIntrinsicWidth()/2)));
//			int top = Math.round((ll.getY() + b.getY() + (b.getHeight()/2) 
//					- (d.getIntrinsicHeight()/2)));
			int left = Math.round((ll.getLeft() 
					+ b.getLeft() + (b.getWidth()/2) 
					- (d.getIntrinsicWidth()/2)));
			int top = Math.round((ll.getTop()  
					+ b.getTop() + (b.getHeight()/2) 
					- (d.getIntrinsicHeight()/2)));
			
			// Get the dimensions of the image to be painted (either OK or Wrong)
			iv.setImageDrawable(d);
			//iv.setTranslationX(left);
			//iv.setTranslationY(top);
			LayoutParams lp = new LayoutParams(d.getIntrinsicWidth(), 
					d.getIntrinsicHeight());
			
			// Paint the image over the pressed button
			lp.setMargins(left, top, 0, 0);
			iv.setLayoutParams(lp);
			iv.bringToFront();
			iv.setVisibility(ImageView.VISIBLE);
			
			// Increase answers counter
			GameManager.getManager().advanceQuestionsAnswered();
		}
	}
	
	/*
	 * Class to attach as adapted click for the correct answer
	 */
	private class AdapterRight extends AdapterClick {		
		protected AdapterRight(QuestionPanel qp, Button _b, Button _bo1, Button _bo2) {
			super(qp, _b, _bo1, _bo2, 
					qp.getIv().getResources().getDrawable(R.drawable.right));
		}
		
		@Override
		public void onClick(View arg0) {
			super.onClick(arg0);			
			
			// Update score
			int timeLeft = CustomTimerTask.TIME_MAX - questionpanel.ct.progress;
			GameManager.getManager().setTimeLeft(timeLeft);
			GameManager.getManager().addPoints();

			questionpanel.showScore(); // Can't place it at the superclass
		}
	}
	
	/*
	 * Class to attach as adapted click for the wrong answers
	 */
	private class AdapterWrong extends AdapterClick {		
		protected AdapterWrong(QuestionPanel qp, Button _b, Button _bo1, Button _bo2) {
			super(qp, _b, _bo1, _bo2,
					qp.getIv().getResources().getDrawable(R.drawable.wrong));
		}
		
		@Override
		public void onClick(View arg0) {
			Question q = GameManager.getManager().getQuestion();
			
			super.onClick(arg0);
		    
			// Guess the right button to make it blink
		    Button btn = (Button) findViewById(R.id.answerButton1);
		    if (q.getOk() == 2)
		    	btn = (Button) findViewById(R.id.answerButton2);
		    else if (q.getOk() == 3)
		    	btn = (Button) findViewById(R.id.answerButton3);
		    
		    btn.startAnimation(animation);

			questionpanel.showScore(); // Can't place it at the superclass
		}
	}
	

	/*
	 * Update the timer to awnser the question
	 */
	private class CustomTimerTask extends TimerTask {
		private static final int TIME_MAX = GameManager.QUESTION_TIME;
		
		QuestionPanel questionpanel;
		private ProgressBar progressbar = null;
		private Timer timer;
		private UITimerTask uitask = null;
		
		private int progress = 0;
		
		/* Task to run in the UI Task queue so that UI items can be changed
		 * (they cannot from a non-UI thread such as the timer task).
		 */
		private class UITimerTask extends TimerTask {
			private Question question = null;

			public UITimerTask (Question q) {
				question = q;
			}
			
			@Override
			public void run() {
				Button btn = (Button) findViewById(R.id.answerButton1);
			    if (question.getOk() == 2)
			    	btn = (Button) findViewById(R.id.answerButton2);
			    else if (question.getOk() == 3)
			    	btn = (Button) findViewById(R.id.answerButton3);
			    
			    btn.startAnimation(animation);
			    
				questionpanel.showScore();
			}
			
			public void setQuestion(Question question) {
				this.question = question;
			}
		}
		
		public CustomTimerTask (Timer _timer, QuestionPanel _qp) {
			questionpanel = _qp;
			timer = _timer;
			progressbar = (ProgressBar) findViewById(R.id.progressBar1);
			progressbar.setMax(TIME_MAX);
			progressbar.setProgress(0);
			uitask = new UITimerTask(GameManager.getManager().getQuestion());
		}

		@Override
		public void run() {
			if (progress < TIME_MAX) {
				progress++;
				progressbar.setProgress(progress);
			} else {
				timer.cancel();
				timer.purge();
				
				// It essentially behaves like presing a wrong button
				Question q = GameManager.getManager().getQuestion();
				uitask.setQuestion(q);

				GameManager.getManager().advanceQuestionsAnswered();
				
				// No timer can manager UI tasks (only the UI thread can)
				questionpanel.runOnUiThread(uitask);
			}
		}
		
	}
	
	/*
	 * Shows the transparent screen with the score
	 */
	private class TransitionTask extends TimerTask {
		QuestionPanel questionpanel;
		
		private class UITransitionTask extends TimerTask {
			@Override
			public void run() {
				FrameLayout fl = (FrameLayout) findViewById(R.id.transitionLayout1);
				fl.bringToFront();
				fl.setVisibility(View.VISIBLE);
				
				Button button1=(Button) findViewById(R.id.answerButton1);
				Button button2=(Button) findViewById(R.id.answerButton2);
				Button button3=(Button) findViewById(R.id.answerButton3);
				
				button1.setOnClickListener(null);
				button2.setOnClickListener(null);
				button3.setOnClickListener(null);
				
				TextView tv = (TextView) findViewById(R.id.scoreText1);
				tv.setText(getString(R.string.score_) + " " + GameManager.getManager().getTotalPoints());
			}
		}
		
		public TransitionTask(QuestionPanel qp) {
			questionpanel = qp;
		}

		@Override
		public void run() {
			// No timer can manager UI tasks (only the UI thread can)
			questionpanel.runOnUiThread(new UITransitionTask());
		}		
	}
	
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_questions);
		
//		mpv = (MoPubView) findViewById(R.id.adviewQuestion);
//		mpv.setAdUnitId("7febfd1244ca11e2bf1612313d143c11");
		
		adView = (AdView) this.findViewById(R.id.adviewQuestion);
		adRequest = new AdRequest();
//		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
//		adRequest.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB");
		adView.loadAd(adRequest);
		
		Button tb = (Button) findViewById(R.id.transtitionButton1);
		tb.setOnClickListener(this);
		
		FrameLayout fl = (FrameLayout) findViewById(R.id.transitionLayout1);
		fl.setVisibility(View.INVISIBLE);
		
	    animation.setDuration(200); 
	    animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
	    animation.setRepeatCount(7); 
	    animation.setRepeatMode(Animation.REVERSE);
		
		showQuestion();	
	}
	
	public void onDestroy() {
		super.onDestroy();
		
		synchronized(this) {
			try {
				this.wait(200);
			} catch (InterruptedException e) {
				//Log.w("QuestionPanel", "Wait interrupted: " + e.getMessage());
			}
		}
	}
	
	public void showQuestion() {		
		//mpv.loadAd();
		adView.loadAd(adRequest);
		
		Question q = GameManager.getManager().getQuestion();
		
		iv = (ImageView) findViewById(R.id.floatingMark);
		iv.setVisibility(View.INVISIBLE);
		
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
		
		// One-line answers are difficult to be clicked on
		button1.setMinLines(1);
		button2.setMinLines(1);
		button3.setMinLines(1);
		
		TextView tv = (TextView) findViewById(R.id.questionText);
		tv.setText(q.getStatement());
		if (q.getImageID() != null) {
			int id = getResources().getIdentifier(getPackageName() 
					+ ":drawable/" + q.getImageID(), null, null);
			Drawable dr = getResources().getDrawable(id);
			Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
			// Scale it to 50 x 50
			Drawable d = new BitmapDrawable(getResources(), 
					Bitmap.createScaledBitmap(bitmap, 50, 50, true));
			// Set your new, scaled drawable "d"
			if (id>0) {
				// tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, id);
				tv.setCompoundDrawablesRelative(null, null, null, d);
			}
		} else {
			// Necessary to remove any previous drawn image
			// tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			tv.setCompoundDrawablesRelative(null, null, null, null);
		}
		
		FrameLayout fl = (FrameLayout) findViewById(R.id.transitionLayout1);
		fl.setVisibility(View.INVISIBLE);
		
		// Start timer here to timeout and to update progress bar
		timer = new Timer();
		ct = new CustomTimerTask(timer, this);
		timer.schedule(ct, 0, 500);
	}
	
	public void showScore() {		
		GameManager manager = GameManager.getManager();
		
		if (manager.isFailed()) {
			startGameoverActivity();
			return;
		}
			
		// If we are at the end of a game
		if (manager.isGameFinished()) {
			startFinishActivity();
			return;
		}
		
		timer = new Timer();
		timer.schedule(new TransitionTask(this), TRANSITION_DELAY);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MainActivity.ACTIVITY_FINISH) {
			finish();
			return;
		}
		
		if (requestCode == MainActivity.ACTIVITY_GAMEOVER) {
			finish();
			return;
		}
		
		showQuestion();
	}
	
	@Override
	public void onClick(View v) {		
		GameManager manager = GameManager.getManager();
		
		if (manager.isFailed()) {
			startGameoverActivity();
			
			return;
		}
		
		// If we are changing the level, show this activity
		if ((manager.getQuestionsAnswered() == GameManager.QUESTIONS_LEVEL1)
				| (manager.getQuestionsAnswered() == 
				GameManager.QUESTIONS_LEVEL1 + 
				GameManager.QUESTIONS_LEVEL2)
				| (manager.getQuestionsAnswered() == 
				GameManager.QUESTIONS_LEVEL1 + 
				GameManager.QUESTIONS_LEVEL2 +
				GameManager.QUESTIONS_LEVEL3)) {

			startLevelActivity();
			
			return;
		}
		
		showQuestion();
	}

	
	public void startLevelActivity() {
		Intent intent = new Intent();
		String packageName =
				"com.zeda.crisistrivia";
		String className =
				"com.zeda.crisistrivia.LevelActivity";
		intent.setClassName(packageName, className);
		startActivityForResult(intent, MainActivity.ACTIVITY_LEVEL2);
	}

	public void startFinishActivity() {
		Intent intent = new Intent();
		String packageName =
				"com.zeda.crisistrivia";
		String className =
				"com.zeda.crisistrivia.FinishActivity";
		intent.setClassName(packageName, className);
		startActivityForResult(intent, MainActivity.ACTIVITY_FINISH);
	}
	
	public void startGameoverActivity() {
		Intent intent = new Intent();
		String packageName =
				"com.zeda.crisistrivia";
		String className =
				"com.zeda.crisistrivia.GameOverActivity";
		intent.setClassName(packageName, className);
		startActivityForResult(intent, MainActivity.ACTIVITY_GAMEOVER);
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

}
