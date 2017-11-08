package com.examle.jiang_yan.fast_develop.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.examle.jiang_yan.fast_develop.bean.ProductInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * 扫描到的信息,对数据库的操作
 * 
 * @author jiang_yan
 * 
 */
public class ProductInfoDAO {
	private ProductDBHelp helper;

	public ProductInfoDAO(Context context) {
		helper = new ProductDBHelp(context);
	}

	/**
	 * 插入一条记录
	 * 
	 * @param product_info
	 * @return
	 */
	public boolean insert(String product_info,String scan_date) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("product_info", product_info);
		values.put("scan_date", scan_date);
		long id = db.insert("product_info", null, values);
		db.close();
		return id != -1;
	}

	/**
	 * 根据产品信息删除产品信息
	 * 
	 * @param product_info
	 * @return
	 */
	public boolean delete(String product_info) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String whereClause = "product_info=?";
		String[] whereArgs = { product_info };
		int row = db.delete("product_info", whereClause, whereArgs);
		db.close();
		return row > 0;
	}

	/**
	 * 根据产品id对信息更新
	 * 
	 * @param product_info
	 * @param id
	 * @return
	 */
//	public boolean update(String product_info, String id) {
//		SQLiteDatabase db = helper.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put("mode", product_info);
//		String whereClause = "id=?";
//		String[] whereArgs = { id };
//		int row = db.update("product_info", values, whereClause, whereArgs);
//		db.close();
//		return row > 0;
//	}

	/**
	 * 查询所有记录,存到ArrayList集合中
	 * @return
	 */
	public List<ProductInfo> findAll() {

		List<ProductInfo> list = new ArrayList<ProductInfo>();
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] columns = { "product_info" ,"scan_date"};
		Cursor cursor = db.query("product_info", columns, null, null, null,
				null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String product_info = cursor.getString(0);
				String scan_date = cursor.getString(1);
				System.out.println("ProductInfoDAO.findAll():"+scan_date);
				ProductInfo info = new ProductInfo();
				info.setProductInfo(product_info);
				info.setScanDate(scan_date);
				list.add(info);
			}
			cursor.close();
		}
		db.close();
		return list;
	}
	/**
	 * 返回扫描信息的总数量
	 * @return
	 */
	public int getProductInfoCount() {
		int count=0;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("product_info", null, null, null, null,
				null, null);
		if (cursor != null  ) {
			count= cursor.getCount();
			cursor.close();
		}
		db.close();
		return count;
	}

//	public int findMode(String number) {
//		SQLiteDatabase db = helper.getWritableDatabase();
//		String[] columns = { "number", "mode" };
//
//		String whereClause = "number=?";
//		String[] whereArgs = { number };
//		Cursor cursor = db.query("blacknumber", columns, whereClause,
//				whereArgs, null, null, null);
//		if (cursor != null && cursor.getCount() > 0) {
//			if (cursor.moveToNext()) {
//				int mode = cursor.getInt(1);
//				return mode;
//			}
//			cursor.close();
//		}
//		db.close();
//		// 表示没有根据电话号码没有查到记录
//		return -1;
//	}

	/**
	 * index 表示从哪条记录开始查询,如果index=20,从第21条记录开始查询,查到第30条,总共10条记录
	 * @param size
	 * @return
	 */
	public List<ProductInfo> findPart(int size, int fromIndex) {
		List<ProductInfo> list = new ArrayList<ProductInfo>();
		SQLiteDatabase db = helper.getWritableDatabase();
		// index 表示从哪条记录开始查询,如果index=20,从第21条记录开始查询,查到第30条,总共10条记录
		// String sql =
		// "select number ,mode from blacknumber limit 10 offset "+index;
		String sql = "select product_info  from product_info limit ? offset ? ";

		Cursor cursor = db
				.rawQuery(sql, new String[] { size + "", fromIndex + "" });
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String productInfo = cursor.getString(0);
				ProductInfo info = new ProductInfo( );
				info.setProductInfo(productInfo);
				list.add(info);
			}
			cursor.close();
		}else {
			return null;
		}
		db.close();
		return list;
	}
}
