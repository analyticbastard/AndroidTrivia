package com.zeda.crisistrivia.engine;

import java.util.Collections;
import java.util.Vector;

import com.zeda.crisistrivia.database.QuestionDataSource;

import android.util.Log;


public class GameManager {
	private static GameManager manager = null;
	
	public static final int MULTIPLIER = 50;
	
	public static final int QUESTIONS_LEVEL1 = 4;
	public static final int QUESTIONS_LEVEL2 = 3;
	public static final int QUESTIONS_LEVEL3 = 2;
	
	public static final int QUESTIONS_FLASH_LEVEL1 = 1;
	public static final int QUESTIONS_FLASH_LEVEL2 = 1;
	public static final int QUESTIONS_FLASH_LEVEL3 = 1;
	
	public static final int QUESTIONS_FLASH = QUESTIONS_FLASH_LEVEL1
			+ QUESTIONS_FLASH_LEVEL2 + QUESTIONS_FLASH_LEVEL3;
	
	public static final int QUESTIONS_IN_GAME = QUESTIONS_LEVEL1
			+ QUESTIONS_LEVEL2 + QUESTIONS_LEVEL3 + QUESTIONS_FLASH;
	
	public static final int FAIL_QUESTIONS_LEVEL1 = 2;
	public static final int FAIL_QUESTIONS_LEVEL2 = 2;
	public static final int FAIL_QUESTIONS_LEVEL3 = 1;
	public static final int FAIL_QUESTIONS_FLASH = 1;
	
	public static final int LEVEL1 = 1;
	public static final int LEVEL2 = 2;
	public static final int LEVEL3 = 3;
	public static final int LEVEL_FLASH = 9999;
	
	public static final int QUESTION_TIME = 25;
	
	
	private Vector<Question> questions;
	
	private int totalPoints = 0;
	
	private int questionsAnswered = 0;
	private int questionsLevelOK = 0;
	private int questionsOK = 0;
	
	private int timeLeft = 0;
	
	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}
	
	public int getQuestionsOK() {
		return questionsOK;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	private GameManager() {
		getQuestionFromSource();
	}
	
	public static GameManager getManager() {
		if (manager == null) {
			manager = new GameManager();
		}
		
		return manager;
	}
	
	public void getQuestionFromSource() {
//		questions = QuestionDataSource.getSource().getQuestions(QUESTIONS_LEVEL1,
//				QUESTIONS_LEVEL2);
		Vector<Question> questions1 = 
				QuestionDataSource.getSource().getQuestionsLevel1(
						QUESTIONS_LEVEL1 + QUESTIONS_FLASH_LEVEL1);
		Vector<Question> questions2 =
				QuestionDataSource.getSource().getQuestionsLevel2(
						QUESTIONS_LEVEL2 + QUESTIONS_FLASH_LEVEL2);
		Vector<Question> questions3 =
				QuestionDataSource.getSource().getQuestionsLevel3(
						QUESTIONS_LEVEL3 + QUESTIONS_FLASH_LEVEL3);
		Vector<Question> questions_flash = new Vector<Question>();
		
		Collections.shuffle(questions1);
		Collections.shuffle(questions2);
		Collections.shuffle(questions3);
		
		for (int i=0; i<QUESTIONS_FLASH_LEVEL1; i++)
			questions_flash.add(questions1.remove(0));
		for (int i=0; i<QUESTIONS_FLASH_LEVEL2; i++)
			questions_flash.add(questions2.remove(0));
		for (int i=0; i<QUESTIONS_FLASH_LEVEL3; i++)
		questions_flash.add(questions3.remove(0));
		
		questions = new Vector<Question>();
		
		questions.addAll(questions1);
		questions.addAll(questions2);
		questions.addAll(questions3);
		questions.addAll(questions_flash);
	}
	
	public Question getQuestion() {
		Question q = questions.get(getQuestionsAnswered());
		return q;
	}
	
	public void addPoints() {
		if (getLevel()==LEVEL_FLASH)
			setTotalPoints((int) Math.round(getTotalPoints() 
					+ questions.get(getQuestionsAnswered() - 1).getDifficulty()
					* MULTIPLIER * 
					( Math.exp( ((float) timeLeft) / QUESTION_TIME ) ) ));
		else
			setTotalPoints(getTotalPoints() 
					+ questions.get(getQuestionsAnswered() - 1).getDifficulty()
					* MULTIPLIER);		
		
		questionsLevelOK++;
		questionsOK++;
	}

	public int getQuestionsAnswered() {
		return questionsAnswered;
	}

	public void advanceQuestionsAnswered() {
		this.questionsAnswered++;
		
		if (getLevel() == LEVEL2 & (getQuestionsAnswered() == QUESTIONS_LEVEL1 + 1))
			questionsLevelOK = 0;
		if (getLevel() == LEVEL3 & (getQuestionsAnswered() == QUESTIONS_LEVEL1
				+ QUESTIONS_LEVEL2 + 1))
			questionsLevelOK = 0;
		if (getLevel() == LEVEL_FLASH & (getQuestionsAnswered() == QUESTIONS_LEVEL1
				+ QUESTIONS_LEVEL2 + QUESTIONS_LEVEL3 + 1))
			questionsLevelOK = 0;
		Log.w("xx", "" + getQuestionsAnswered() + ":" + questionsLevelOK);
	}
	
	public void resetGame() {
		questionsAnswered = 0;
		setTotalPoints(0);
		questionsLevelOK = 0;
		questionsOK = 0;
		
		getQuestionFromSource();
	}
	
	public int getLevel() {
		if (getQuestionsAnswered() < QUESTIONS_LEVEL1) {
			return LEVEL1;
		} else if (getQuestionsAnswered() < QUESTIONS_LEVEL1 + QUESTIONS_LEVEL2) {
			return LEVEL2;
		} else if (getQuestionsAnswered() < QUESTIONS_LEVEL1 + QUESTIONS_LEVEL2
				+ QUESTIONS_LEVEL3) {
			return LEVEL3;
		} else {
			return LEVEL_FLASH;
		}
	}

	public boolean isFailed() {
		boolean ok = true;
		
		if (getQuestionsAnswered() <= QUESTIONS_LEVEL1) 
			ok = (getQuestionsAnswered() 
					- questionsLevelOK) < FAIL_QUESTIONS_LEVEL1;
		else if (getQuestionsAnswered() <= (QUESTIONS_LEVEL1 + QUESTIONS_LEVEL2))
			ok = (getQuestionsAnswered() - QUESTIONS_LEVEL1 
					- questionsLevelOK) < FAIL_QUESTIONS_LEVEL2;
		else if (getQuestionsAnswered() <= (QUESTIONS_LEVEL1 + QUESTIONS_LEVEL2
				+ QUESTIONS_LEVEL3))
			ok = (getQuestionsAnswered() - QUESTIONS_LEVEL1  - QUESTIONS_LEVEL2 
					- questionsLevelOK) < FAIL_QUESTIONS_LEVEL3;
		else {
			ok = (getQuestionsAnswered() - QUESTIONS_LEVEL1  - QUESTIONS_LEVEL2
					- QUESTIONS_LEVEL3 - questionsLevelOK) 
					< FAIL_QUESTIONS_FLASH;
		}
		
		return !ok;
	}
	
	public boolean isGameFinished() {
		return getQuestionsAnswered() >= QUESTIONS_IN_GAME;
	}
	
}
