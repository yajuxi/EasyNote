package com.dxxy.note.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库工具类
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * 数据库的名字
     */
	private static final String DATABASE_NAME = "Note.db";

	/**
	 * 数据库版本号
	 */
	private static final int VERSION = 1;

	/**
	 * 用户表
	 */
	public static final String USER_TABLE = "User";

	/**
	 * 创建用户表
	 */
	private String createUserTable = "create table if not exists " + USER_TABLE+ "(id integer primary key autoincrement,name varchar(20)" +
			",password varchar(20), avatar varchar(255))";

	/**
	 * 记事表
	 */
	public static final String NOTE_TABLE = "Note";

	/**
	 * 创建记事表
	 */
	private String createNoteTable = "create table if not exists " + NOTE_TABLE+ "(id integer primary key autoincrement,title varchar(255)," +
			"image varchar(255),content varchar(255),createTime varchar(20),updateTime varchar(20))";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//执行创建用户表
		db.execSQL(createUserTable);
		//执行创建记事表
		db.execSQL(createNoteTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
