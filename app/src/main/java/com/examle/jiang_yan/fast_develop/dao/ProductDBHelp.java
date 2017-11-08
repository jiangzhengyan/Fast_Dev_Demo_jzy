package com.examle.jiang_yan.fast_develop.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDBHelp extends SQLiteOpenHelper {

	/**
	 * 用来创建\打开\管理数据库对象的
	 * 
	 * @param context
	 * public SQLiteOpenHelper(Context context, String name,
	 *            CursorFactory factory, int version) { this(context, name,
	 *            factory, version, null); }
	 */
	public ProductDBHelp(Context context) {
		super(context, "product_info.db", null, 1);
	}

	/**
	 * 第一次创建数据库对象时调用这个方法 通常在这个方法中初始化数据库:创建表结构\插入初始记录
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建表
		System.out.println("onCreate_创建表");
		String sql = "create table product_info (_id integer primary key autoincrement,product_info varchar(300),scan_date vachar(50))";
		db.execSQL(sql);

	}

	/**
	 * 升级数据库时调用这个方法 新版本号大于老版本号时升级数据库 通常在这个方法修改表结构/删除表/增加表
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
