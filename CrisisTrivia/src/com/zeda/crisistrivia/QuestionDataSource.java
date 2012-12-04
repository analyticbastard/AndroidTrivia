package com.zeda.crisistrivia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;



public class QuestionDataSource {
	public static final String DB_NAME = "questions.sqlite";
	public static final int DB_VERSION = 1;
	
	public static final String  TABLE_LEVEL1 = "level1";
	public static final String  TABLE_LEVEL2 = "level2";
	public static final String  TABLE_LEVEL3 = "level3";

	public static final String COLUMN_ID = "rowid";
	public static final String COLUMN_DIFFICULTY = "Difficulty";
	public static final String COLUMN_STATEMENT = "Statement";
	public static final String COLUMN_IMAGE = "Image";
	public static final String COLUMN_ANSWER1 = "Answer1";
	public static final String COLUMN_ANSWER2 = "Answer2";
	public static final String COLUMN_ANSWER3 = "Answer3";
	
	public static final int MAX_QUESTIONS_LEVEL1 = 50;
	public static final int MAX_QUESTIONS_LEVEL2 = 30;
	public static final int MAX_QUESTIONS_LEVEL3 = 10;

	private static QuestionDataSource source;

	private String[] allColumns = { COLUMN_ID, COLUMN_DIFFICULTY,
			COLUMN_STATEMENT, COLUMN_IMAGE, COLUMN_ANSWER1,
			COLUMN_ANSWER2, COLUMN_ANSWER3 };

	//private SQLiteDatabase database;
	private MySQLiteOpenHelper dbHelper;


	/***
	 * Helper singleton class to manage SQLiteDatabase Create and Restore
	 *
	 * @author alessandrofranzi
	 *
	 */
	// Modify by me 24/6/2010
	public class MySQLiteOpenHelper extends SQLiteOpenHelper {
		private SQLiteDatabase sqliteDb;
		
		// the default database path is :
		// /data/data/pkgNameOfYourApplication/databases/
		private static final String DB_PATH_PREFIX = "/data/data/";
		private static final String DB_PATH_SUFFIX = "/databases/";
		private static final String TAG = "MySQLiteOpenHelper";
		private Context context;

		/***
		 * Contructor
		 *
		 * @param context
		 *            : app context
		 * @param name
		 *            : database name
		 * @param factory
		 *            : cursor Factory
		 * @param version
		 *            : DB version
		 */
		private MySQLiteOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			this.context = context;
			
			/**
			 * Try to check if there is an Original copy of DB in asset
			 * Directory
			 */
//			if (!checkDatabase(context, name)) {
				// if not exists, I try to copy from asset dir
			try {
				copyDataBase(context, name);
			} catch (IOException e) {
				Log.e(TAG, "Database " + name
						+ " does not exists and there is no Original Version in Asset dir");
			}
//			}
			
			sqliteDb = this.getReadableDatabase();
			Log.i(TAG, "instance of database (" + name + ") created !");
			
			Log.i(TAG, "Create or Open database : " + name);
		}


		/***
		 * Method to get database instance
		 *
		 * @return database instance
		 */
		public SQLiteDatabase getDatabase() {
			return sqliteDb;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "onCreate : nothing to do");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "onCreate : nothing to do");

		}

		/***
		 * Method for Copy the database from asset directory to application's data
		 * directory
		 *
		 * @param databaseName
		 *            : database name
		 * @throws IOException
		 *             : exception if file does not exists
		 */
		private void copyDataBase(String databaseName) throws IOException {
			copyDataBase(context, databaseName);
		}

		/***
		 * Static method for copy the database from asset directory to application's
		 * data directory
		 *
		 * @param aContext
		 *            : application context
		 * @param databaseName
		 *            : database name
		 * @throws IOException
		 *             : exception if file does not exists
		 */
		private void copyDataBase(Context aContext, String databaseName)
				throws IOException {

			// Open your local db as the input stream
			InputStream myInput = aContext.getAssets().open(databaseName);

			// Path to the just created empty db
			String outFileName = getDatabasePath(aContext, databaseName);

			Log.i(TAG, "Check if create dir : " + DB_PATH_PREFIX
					+ aContext.getPackageName() + DB_PATH_SUFFIX);

			// if the path doesn't exist first, create it
			File f = new File(DB_PATH_PREFIX + aContext.getPackageName()
					+ DB_PATH_SUFFIX);
			if (!f.exists())
				f.mkdir();

			Log.i(TAG, "Trying to copy local DB to : " + outFileName);

			// Open the empty db as the output stream
			OutputStream myOutput = new FileOutputStream(outFileName);

			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();

			Log.i(TAG, "DB (" + databaseName + ") copied!");
		}

		/***
		 * Method to check if database exists in application's data directory
		 *
		 * @param databaseName
		 *            : database name
		 * @return : boolean (true if exists)
		 */
		public boolean checkDatabase(String databaseName) {
			return checkDatabase(context, databaseName);
		}

		/***
		 * Static Method to check if database exists in application's data directory
		 *
		 * @param aContext
		 *            : application context
		 * @param databaseName
		 *            : database name
		 * @return : boolean (true if exists)
		 */
		public boolean checkDatabase(Context aContext, String databaseName) {
			SQLiteDatabase checkDB = null;

			try {
				String myPath = getDatabasePath(aContext, databaseName);

				Log.i(TAG, "Trying to conntect to : " + myPath);
				checkDB = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.OPEN_READONLY);
				Log.i(TAG, "Database " + databaseName + " found!");
				checkDB.close();
			} catch (SQLiteException e) {
				Log.i(TAG, "Database " + databaseName + " does not exists!");

			}

			return checkDB != null ? true : false;
		}

		/***
		 * Method that returns database path in the application's data directory
		 *
		 * @param databaseName
		 *            : database name
		 * @return : complete path
		 */
		private String getDatabasePath(String databaseName) {
			return getDatabasePath(context, databaseName);
		}

		/***
		 * Static Method that returns database path in the application's data
		 * directory
		 *
		 * @param aContext
		 *            : application context
		 * @param databaseName
		 *            : database name
		 * @return : complete path
		 */
		private String getDatabasePath(Context aContext, String databaseName) {
			return DB_PATH_PREFIX + aContext.getPackageName() + DB_PATH_SUFFIX
					+ databaseName;
		}
	};


	private QuestionDataSource (Context context) {
		dbHelper = new MySQLiteOpenHelper(context, DB_NAME, null, DB_VERSION);
	}


	public static QuestionDataSource getSource() {
		return source;
	}

	public static void createSource(Context context) {
		source = new QuestionDataSource(context);
	}

	public void close() {
		dbHelper.close();
	}


//	public Vector<Question> getQuestions(int n1, int n2) {
//		Vector<Question> questions1 = new Vector<Question>(n1);
//		
//		Vector<Long> seq = generateRandomSequence(7, n1);
//
//		Cursor cursor = dbHelper.getDatabase().query(TABLE_LEVEL1,
//				allColumns, null, null, null, null, null);
//
//		cursor.moveToFirst();
//		while (!cursor.isAfterLast()) {
//			Question q = cursorToQuestion(cursor);
//			if (seq.contains(new Long(cursor.getLong(0))))
//				questions1.add(q);
//			cursor.moveToNext();
//		}
//		// Make sure to close the cursor
//		cursor.close();
//		Collections.shuffle(questions1);
//		
//		Vector<Question> questions2 = new Vector<Question>(n2);
//		
//		seq = generateRandomSequence(5, n2);
//
//		cursor = dbHelper.getDatabase().query(TABLE_LEVEL2,
//				allColumns, null, null, null, null, null);
//
//		cursor.moveToFirst();
//		while (!cursor.isAfterLast()) {
//			Question q = cursorToQuestion(cursor);
//			Log.i("xxx", "" + cursor.getLong(0));
//			if (seq.contains(new Long(cursor.getLong(0))))
//				questions2.add(q);
//			cursor.moveToNext();
//		}
//		// Make sure to close the cursor
//		cursor.close();
//		Collections.shuffle(questions2);
//		
//		
//		questions1.addAll(questions2);
//		
//		return questions1;
//	}
	
	public Vector<Question> getQuestionsLevel1(int n) {
		return getQuestionsLevel(n, MAX_QUESTIONS_LEVEL1, TABLE_LEVEL1);
	}
	
	public Vector<Question> getQuestionsLevel2(int n) {
		return getQuestionsLevel(n, MAX_QUESTIONS_LEVEL2, TABLE_LEVEL2);
	}
	
	public Vector<Question> getQuestionsLevel3(int n) {
		return getQuestionsLevel(n, MAX_QUESTIONS_LEVEL3, TABLE_LEVEL3);
	}
	
	public Vector<Question> getQuestionsLevel(int n, int maxQuestions, 
			String tableLevel) {
		Vector<Question> questions = new Vector<Question>(n);
		Vector<Long> seq = generateRandomSequence(maxQuestions, n);

		Cursor cursor = dbHelper.getDatabase().query(tableLevel,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Question q = cursorToQuestion(cursor);
			if (seq.contains(new Long(cursor.getLong(0))))
				questions.add(q);
			cursor.moveToNext();
		}
		
		cursor.close();

		return questions;
	}

	private Question cursorToQuestion(Cursor cursor) {
		Question q = new Question((int) cursor.getLong(1), cursor.getString(2), 
				cursor.getString(3), cursor.getString(4), cursor.getString(5),
				cursor.getString(6), 1);
		q.shuffle();
		
		return q;
	}
	
	private Vector<Long> generateRandomSequence(int n, int howmany) {
		Vector<Long> v = new Vector<Long>();
		
		for (long i=1; i<=n; i++) {
			v.add(i);
		}
		
		Log.i("zzz", "" + v.toString());
		
		Collections.shuffle(v);
		
		for (int i=howmany+1; i<=n; i++) {
			v.removeElementAt(howmany);
		}
		
		Log.i("zzz", "" + v.toString());
		
		return v;
	}
}
