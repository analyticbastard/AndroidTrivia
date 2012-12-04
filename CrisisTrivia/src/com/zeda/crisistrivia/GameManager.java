package com.zeda.crisistrivia;

import java.util.Collections;
import java.util.Vector;

import android.util.Log;

public class GameManager {
	private static GameManager manager = null;
	
	public static final int MULTIPLIER = 50;
	
	public static final int QUESTIONS_IN_GAME = 8;
	
	public static final int QUESTIONS_LEVEL1 = 3;
	public static final int QUESTIONS_LEVEL2 = 3;
	public static final int QUESTIONS_LEVEL3 = 2;
	
	public static final int FAIL_QUESTIONS_LEVEL = 2;
	public static final int FAIL_QUESTIONS_FLASH = 1;
	
	public static final int LEVEL1 = 1;
	public static final int LEVEL2 = 2;
	public static final int LEVEL3 = 3;
	public static final int LEVEL_FLASH = 9999;
	
	public static final int QUESTION_TIME = 15;
	
	
	private Vector<Question> questions;
	
	private int totalPoints = 0;
	
	private int questionsAnswered = 0;
	private int questionsOK = 0;
	private int questionsLevelOK = 0;
	
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
						QUESTIONS_LEVEL1);
		Vector<Question> questions2 =
				QuestionDataSource.getSource().getQuestionsLevel2(
						QUESTIONS_LEVEL2);
		Vector<Question> questions3 =
				QuestionDataSource.getSource().getQuestionsLevel3(
						QUESTIONS_LEVEL3);
		
		Collections.shuffle(questions1);
		Collections.shuffle(questions2);
		Collections.shuffle(questions3);
		
		questions = new Vector<Question>();
		
		questions.addAll(questions1);
		questions.addAll(questions2);
		questions.addAll(questions3);
	}
	
	public Question getQuestion() {
		Question q = questions.get(getQuestionsAnswered());
		return q;
	}
	
	public void addPoints() {
		//setTotalPoints(getTotalPoints() + points * MULTIPLIER);
		setTotalPoints(getTotalPoints() 
				+ questions.get(getQuestionsAnswered()).getDifficulty()
				* MULTIPLIER);
		questionsOK++;
		questionsLevelOK++;
	}

	public int getQuestionsAnswered() {
		return questionsAnswered;
	}

	public void advanceQuestionsAnswered() {
		this.questionsAnswered++;
		
		if (getPreLevel() == LEVEL2 & (getQuestionsAnswered() == QUESTIONS_LEVEL1 + 1))
			questionsLevelOK = 0;
		if (getPreLevel() == LEVEL3 & (getQuestionsAnswered() == QUESTIONS_LEVEL1
				+ QUESTIONS_LEVEL2 + 1))
			questionsLevelOK = 0;
		if (getPreLevel() == LEVEL_FLASH & (getQuestionsAnswered() == QUESTIONS_LEVEL1
				+ QUESTIONS_LEVEL2 + QUESTIONS_LEVEL3 + 1))
			questionsLevelOK = 0;
	}
	
	public void resetGame() {
		questionsAnswered = 0;
		setTotalPoints(0);
		questionsOK = 0;
		questionsLevelOK = 0;
		
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


	public int getPreLevel() {
		if (getQuestionsAnswered() <= QUESTIONS_LEVEL1) {
			return LEVEL1;
		} else if (getQuestionsAnswered() <= QUESTIONS_LEVEL1 + QUESTIONS_LEVEL2) {
			return LEVEL2;
		} else if (getQuestionsAnswered() <= QUESTIONS_LEVEL1 + QUESTIONS_LEVEL2
				+ QUESTIONS_LEVEL3) {
			return LEVEL3;
		} else {
			return LEVEL_FLASH;
		}
	}

	public boolean isFailed() {
		boolean ok = false;
		
		if (getPreLevel()==LEVEL1) 
			ok = (getQuestionsAnswered() 
					- questionsLevelOK) < FAIL_QUESTIONS_LEVEL;
		else if (getPreLevel()==LEVEL2)
			ok = (getQuestionsAnswered() - QUESTIONS_LEVEL1 
					- questionsLevelOK) < FAIL_QUESTIONS_LEVEL;
		else if (getPreLevel()==LEVEL3)
			ok = (getQuestionsAnswered() - QUESTIONS_LEVEL1  - QUESTIONS_LEVEL2 
					- questionsLevelOK) < FAIL_QUESTIONS_LEVEL;
		else if (getPreLevel()==LEVEL_FLASH) {
			ok = (getQuestionsAnswered() - QUESTIONS_LEVEL1  - QUESTIONS_LEVEL2
					- QUESTIONS_LEVEL3 - questionsLevelOK) 
					< FAIL_QUESTIONS_FLASH;
		}
		
		return !ok;
	}
}
