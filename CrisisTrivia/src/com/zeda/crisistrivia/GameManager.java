package com.zeda.crisistrivia;

import java.util.Vector;

public class GameManager {
	private static GameManager manager = null;
	
	public static final int MULTIPLIER = 50;
	
	public static final int QUESTIONS_IN_GAME = 6;
	public static final int QUESTIONS_LEVEL1 = 3;
	public static final int QUESTIONS_LEVEL2 = 3;
	
	public static final int QUESTION_TIME = 15;
	
	Vector<Question> questions;
	
	private int totalPoints = 0;
	
	private int questionsAnswered = 0;
	
	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	private GameManager() {
		questions = QuestionDataSource.getSource().getQuestions(QUESTIONS_LEVEL1,
				QUESTIONS_LEVEL2);		
	}
	
	public static GameManager getManager() {
		if (manager == null) {
			manager = new GameManager();
		}
		
		return manager;
	}
	
	public Question getQuestion() {
//		String st = "Who is Bernanke?";
//		String imag = "bernanke1";
//		
//		Vector<String> a = new Vector<String>(3);
//		a.add("President of the Federal Reserve");
//		a.add("President of the ECB");
//		a.add("Secretary of the Treasury");
//		
//		int ok = 1;
//
//		String truth = a.get(0);
//		
//		Collections.shuffle(a);
//		for (int i=0; i<3; i++) {
//			if (a.get(i).equals(truth))
//				ok = i + 1;
//		}
//		
//		Question q = new Question(10, st, imag, a.get(0), a.get(1), a.get(2), ok);
		Question q = questions.get(getQuestionsAnswered());
		return q;
	}
	
	public void addPoints(int points) {
		setTotalPoints(getTotalPoints() + points * MULTIPLIER);
	}

	public int getQuestionsAnswered() {
		return questionsAnswered;
	}

	public void setQuestionsAnswered(int questionsAnswered) {
		this.questionsAnswered = questionsAnswered;
	}
	
	public void resetGame() {
		setQuestionsAnswered(0);
		setTotalPoints(0);
	}
}
