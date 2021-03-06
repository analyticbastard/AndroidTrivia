package com.zeda.crisistrivia.mail;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

//import com.zeda.crisistrivia.BuildConfig;

import android.os.AsyncTask;


public class SendEmailAsyncTask extends AsyncTask <Void, Void, Boolean> {
    Mail m = new Mail("androidtriviaframework@gmail.com", "benbernanke");

    public SendEmailAsyncTask(String body, String author) {
        //if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
        String[] toArr = { "androidtriviaframework@gmail.com"};
        m.setTo(toArr);
        m.setFrom("androidtriviaframework@gmail.com");
        m.setSubject("new question by " + author);
        m.setBody(body);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        //if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");
        try {
            m.send();
            return true;
        } catch (AuthenticationFailedException e) {
            return false;
        } catch (MessagingException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}