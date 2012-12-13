package com.zeda.crisistrivia;

import java.util.Date;
import java.util.Vector;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings {
	public static final String PREFS_NAME = "config";
	
	private static final String EDITOR_NAME1 = "name1";
	private static final String EDITOR_NAME2 = "name2";
	private static final String EDITOR_NAME3 = "name3";
	private static final String EDITOR_POINTS1 = "points1";
	private static final String EDITOR_POINTS2 = "points2";
	private static final String EDITOR_POINTS3 = "points3";

	private static Settings instance;
	
	private String userName = "Me";
	
	private Vector<GameRank> gameRank = new Vector<GameRank>();
	
	private class GameRank {
		private String name;
		private Date date;
		private int points;
		
		public GameRank(String _name, Date _date, int _points) {
			name = _name;
			date = _date;
			points = _points;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public int getPoints() {
			return points;
		}

		public void setPoints(int points) {
			this.points = points;
		}
	}
	
	private Settings() {
		GameRank gr = new GameRank("Alan Greenspan", new Date(), 10000);
		gameRank.add(gr);
		gr = new GameRank("Ben Bernanke", new Date(), 7500);
		gameRank.add(gr);
		gr = new GameRank("Paul Krugman", new Date(), 2500);
		gameRank.add(gr);
		
		SharedPreferences settings = MainActivity.getInstance()
				.getSharedPreferences(PREFS_NAME, 0);
		String name = settings.getString(EDITOR_NAME1, null);
		if (name == null) {
			return;
		}
		
		int points = settings.getInt(EDITOR_POINTS1, -1);
		gameRank.get(0).setName(name);
		gameRank.get(0).setPoints(points);
		
		name = settings.getString(EDITOR_NAME2, null);
		points = settings.getInt(EDITOR_POINTS2, -1);
		gameRank.get(1).setName(name);
		gameRank.get(1).setPoints(points);
		
		name = settings.getString(EDITOR_NAME3, null);
		points = settings.getInt(EDITOR_POINTS3, -1);
		gameRank.get(2).setName(name);
		gameRank.get(2).setPoints(points);
		
		AccountManager accountManager = AccountManager.get(
				MainActivity.getInstance().getApplicationContext());

	    Account[] accounts =
	    accountManager.getAccountsByType("com.google");

	    if (accounts.length > 0) {
	    	name = accountManager.getUserData(accounts[0], 
	    			AccountManager.KEY_ACCOUNT_NAME);

	    	if (name != null) {
	    		userName = name;
	    	}
	    }
	}
	
	public void saveRank() {
		SharedPreferences settings = MainActivity.getInstance()
				.getSharedPreferences(PREFS_NAME, 0);
		
		int points = GameManager.getManager().getTotalPoints();
		
		int points1 = gameRank.get(0).getPoints();
		int points2 = gameRank.get(1).getPoints();
		int points3 = gameRank.get(2).getPoints();
		
		String name1 = gameRank.get(0).getName();
		String name2 = gameRank.get(1).getName();
		String name3 = gameRank.get(2).getName();
		
		if (points > points1) {
			points3 = points2;
			points2 = points1;
			points1 = points;
			
			name3 = name2;
			name2 = name1;
			name3 = userName;
		} else if (points > points2) {
			points3 = points2;
			points2 = points;
			
			name3 = name2;
			name2 = userName;
		} else if (points > points2) {
			points3 = points;
			
			name3 = userName;
		}
		
		gameRank.get(0).setName(name1);
		gameRank.get(1).setName(name2);
		gameRank.get(2).setName(name3);
		
		gameRank.get(0).setPoints(points1);
		gameRank.get(1).setPoints(points2);
		gameRank.get(2).setPoints(points3);

		Editor editor = settings.edit();
		
		editor.clear();
		editor.commit();
		
		editor.putString(EDITOR_NAME1, gameRank.get(0).getName());
		editor.putInt(EDITOR_POINTS1, gameRank.get(0).getPoints());
		editor.putString(EDITOR_NAME2, gameRank.get(1).getName());
		editor.putInt(EDITOR_POINTS2, gameRank.get(1).getPoints());
		editor.putString(EDITOR_NAME3, gameRank.get(2).getName());
		editor.putInt(EDITOR_POINTS3, gameRank.get(2).getPoints());
		
		editor.apply();
	}
	
	public static Settings getSettings() {
		if (instance == null) {
			instance = new Settings();
		}
		
		return instance;
	}

	public Vector<GameRank> getGameRank() {
		return gameRank;
	}
	
	public String getName(int position) {
		return gameRank.get(position).getName();
	}
	
	public Date getDate(int position) {
		return gameRank.get(position).getDate();
	}
	
	public int getPoints(int position) {
		return gameRank.get(position).getPoints();
	}
	
	public String getUserName(int position) {
		return userName;
	}
	
}
