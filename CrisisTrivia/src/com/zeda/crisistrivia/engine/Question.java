package com.zeda.crisistrivia.engine;

import java.util.Collections;
import java.util.Vector;


public class Question {
	private int difficulty;

	private String statement;
	private String imageID;
	
	private String answer1;
	private String answer2;
	private String answer3;
	
	private int ok;
	
	public Question(int dif, String st, String iid, String a1, String a2, String a3, int o) {
		difficulty = dif;
		
		statement = st;
		imageID = iid;
		
		answer1 = a1;
		answer2 = a2;
		answer3 = a3;
		
		ok = o;
	}
	
	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public String getStatement() {
		return statement;
	}
	
	public void setStatement(String statement) {
		this.statement = statement;
	}
	
	public String getImageID() {
		return imageID;
	}
	
	public void setImageID(String imageID) {
		this.imageID = imageID;
	}
	
	public String getAnswer1() {
		return answer1;
	}
	
	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}
	
	public String getAnswer2() {
		return answer2;
	}
	
	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}
	
	public String getAnswer3() {
		return answer3;
	}
	
	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}
	
	public int getOk() {
		return ok;
	}
	
	public void setOk(int ok) {
		this.ok = ok;
	}
	
	public void shuffle() {
		Vector<String> a = new Vector<String>(3);
		int ok = 1;

		String truth = getAnswer1();

		a.add(getAnswer1());
		a.add(getAnswer2());
		a.add(getAnswer3());

		Collections.shuffle(a);
		for (int i=0; i<3; i++) {
			if (a.get(i).equals(truth))
				ok = i + 1;
		}
		
		setAnswer1(a.get(0));
		setAnswer2(a.get(1));
		setAnswer3(a.get(2));
		
		setOk(ok);
	}
}
