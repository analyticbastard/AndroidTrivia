package com.zeda.crisistrivia;

import com.zeda.crisistrivia.engine.Settings;
import com.zeda.crisistrivia.mail.SendEmailAsyncTask;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EmailQuestionActivity extends Activity implements OnClickListener {
	
	
//	private class NetworkSendEmail extends TimerTask {
//		String statement, a1, a2, a3;
//		
//		public NetworkSendEmail(String st, String aw1, String aw2, String aw3) {
//			statement = st;
//			a1 = aw1;
//			a2 = aw2;
//			a3 = aw3;
//		}
//		
//		@Override
//		public void run() {
//			GMailSender sender = new GMailSender("albrecht.allenstein", "megazeda");
//			try {
//				sender.sendMail("new question",
//						"Statement: " + statement + "\n" +
//								"A1: " + a1 + "A2: " + a2 + "A3: " + a3,							
//						"albrecht.allenstein@gmail.com",
//						"albrecht.allenstein@gmail.com");
//			} catch (Exception e) {
//				Log.w("ZZZ", "" + e.getMessage());
//				// Do nothing to upset the user. He would be annoyed if he finds
//				// out the effort of writing everything down went to waste
//			}
//		}
//		
//	}
	
	private class SendEmail implements OnClickListener {
		EmailQuestionActivity emailquestionactivity = null;
		
		public SendEmail(EmailQuestionActivity em) {
			emailquestionactivity = em;
		}

		@Override
		public void onClick(View v) {
			EditText e1 = (EditText) findViewById(R.id.editText1);
			String statement = e1.getText().toString();

			EditText e2 = (EditText) findViewById(R.id.editText2);
			String a1 = e2.getText().toString();

			EditText e3 = (EditText) findViewById(R.id.editText3);
			String a2 = e3.getText().toString();

			EditText e4 = (EditText) findViewById(R.id.editText4);
			String a3 = e4.getText().toString();
			
			boolean complete = true;
			String reason = "";
			
			if (statement == null) {
				complete = false;
				reason = "statement";				
			} else if (statement.isEmpty()) {
				complete = false;
				reason = "statement";
			}
			
			if (a1 == null) {
				complete = false;
				reason = "correct answer";
			} else if (a1.isEmpty()) {
				complete = false;
				reason = "correct answer";
			}
			
			if (a2 == null) {
				complete = false;
				reason = "first incorrect answer";
			} else if (a2.isEmpty()) {
				complete = false;
				reason = "first incorrect answer";
			}
			
			if (a3 == null) {
				complete = false;
				reason = "second incorrect answer";
			} else if (a3.isEmpty()) {
				complete = false;
				reason = "second incorrect answer";
			}

			if (complete == false) {
				AlertDialog alertDialog;
				alertDialog = new AlertDialog.Builder(emailquestionactivity).create();
				alertDialog.setTitle("Error");
				alertDialog.setMessage("Please fill your info for " + reason);
				alertDialog.show();
				return;
			}

			String body = "Statement: " + statement + "\n" + "A1: " + 
					a1 + "A2: " + a2 + "A3: " + a3;
			new SendEmailAsyncTask(body, 
					Settings.getSettings().getUserName()).execute();
			
			emailquestionactivity.finish();
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_question);
		
		Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		
		Button send = (Button) findViewById(R.id.send);
		send.setOnClickListener(new SendEmail(this));
	}

	@Override
	public void onClick(View v) {
		finish();
	}

}
