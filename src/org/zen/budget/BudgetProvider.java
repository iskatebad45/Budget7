package org.zen.budget;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class BudgetProvider extends ContentProvider implements BaseColumns {
	private static final String				TAG				= ".BudgetProvider";
	private static final String				DB_NAME			= "budgetitems.db", TABLE_NAME = "items";
	private static final int				DB_VERSION		= 1, CODE_TABLE = 1, CODE_ITEM = 2;
	private static final UriMatcher			matcher;
	private static HashMap<String, String>	projectionMap;
	private static BudgetDbHelper			helper;
	
	public static final String				AUTHORITY		= Budget7.PKG + ".provider", COL_AMT = "amt",
			COL_LABEL = "label", COL_DATE = "date",
			CONTENT_TYPE_TABLE = "vnd.android.cursor.dir/vnd.zen.budget7_items",
			CONTENT_TYPE_ROW = "vnd.android.cursor.item/vnd.zen.budget7_item";
	public static final Uri					CONTENT_URI		= Uri.parse( "content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final String[]			PROJECTION_ALL	= new String[] { _ID, COL_AMT, COL_LABEL, COL_DATE};
	
	static {
		matcher = new UriMatcher( UriMatcher.NO_MATCH);
		matcher.addURI( AUTHORITY, TABLE_NAME, CODE_TABLE);
		matcher.addURI( AUTHORITY, TABLE_NAME + "/#", CODE_ITEM);
		projectionMap = new HashMap<String, String>();
		projectionMap.put( _ID, _ID);
		projectionMap.put( COL_AMT, COL_AMT);
		projectionMap.put( COL_LABEL, COL_LABEL);
		projectionMap.put( COL_DATE, COL_DATE);
	}
	
	private static class BudgetDbHelper extends SQLiteOpenHelper {
		private static final String	TAG				= ".BudgetDbHelper";
		
		private static final String	CREATE_TABLE	= "CREATE TABLE " + TABLE_NAME + " (" + _ID
															+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_AMT
															+ " INTEGER NOT NULL, " + COL_LABEL
															+ " TEXT DEFAULT \"\", " + COL_DATE + " INTEGER NOT NULL);";
		
		public BudgetDbHelper( final Context context) {
			super( context, DB_NAME, null, DB_VERSION);
			Log.d( TAG, "BudgetDbHelper()");
		}
		
		@Override
		public void onCreate( final SQLiteDatabase db) {
			Log.d( TAG, "onCreate");
			db.execSQL( CREATE_TABLE);
		}
		
		@Override
		public void onUpgrade( final SQLiteDatabase db, final int oldVersion, final int newVersion) {
			Log.d( TAG, "onUpgrade");
			db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate( db);
		}
	}
	
	@Override
	public boolean onCreate() {
		helper = new BudgetDbHelper( getContext());
		Log.d( TAG, "onCreate");
		return true;
	}
	
	@Override
	public int delete( final Uri uri, final String selection, final String[] selectionArgs) {
		Log.d( TAG, "delete");
		int count;
		switch( matcher.match( uri)) {
			case CODE_ITEM:
				count = helper.getWritableDatabase().delete( TABLE_NAME, selection, selectionArgs);
				break;
			case CODE_TABLE:
				count = helper.getWritableDatabase().delete( TABLE_NAME, _ID + " NOT NULL", null);
			default:
				throw new IllegalArgumentException( "Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange( uri, null);
		return count;
	}
	
	@Override
	public String getType( final Uri uri) {
		Log.d( TAG, "getType");
		switch( matcher.match( uri)) {
			case CODE_TABLE:
				return CONTENT_TYPE_TABLE;
			case CODE_ITEM:
				return CONTENT_TYPE_ROW;
			default:
				throw new IllegalArgumentException( "Unknown URI " + uri);
		}
	}
	
	@Override
	public Uri insert( final Uri uri, final ContentValues initValues) {
		Log.d( TAG, "insert");
		final ContentValues values = initValues != null ? initValues : new ContentValues();
		long rowid;
		switch( matcher.match( uri)) {
			case CODE_ITEM:
				throw new IllegalArgumentException( "Can't insert a row into a row, bad URI: " + uri);
			case CODE_TABLE:
				rowid = helper.getWritableDatabase().insert( TABLE_NAME, COL_LABEL, values);
				break;
			default:
				throw new IllegalArgumentException( "Unknown URI " + uri);
		}
		if( rowid > 0) {
			final Uri ruri = ContentUris.withAppendedId( CONTENT_URI, rowid);
			getContext().getContentResolver().notifyChange( ruri, null);
			return ruri;
		}
		throw new SQLException( "Failed to insert row into " + uri);
	}
	
	@Override
	public Cursor query( Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.d( TAG, "query");
		final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables( TABLE_NAME);
		qb.setProjectionMap( projectionMap);
		Cursor c;
		switch( matcher.match( uri)) {
			case CODE_ITEM:
				c =
						qb.query( helper.getReadableDatabase(), projection, selection, selectionArgs, null, null,
								sortOrder);
				break;
			case CODE_TABLE:
				c =
						qb.query( helper.getReadableDatabase(), projection, _ID + " NOT NULL", selectionArgs, null,
								null, sortOrder);
				break;
			default:
				throw new IllegalArgumentException( "Unknown URI " + uri);
		}
		Log.d( TAG, "query: uri: " + uri);
		c.setNotificationUri( getContext().getContentResolver(), uri);
		return c;
	}
	
	@Override
	public int update( Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		Log.d( TAG, "update");
		int count;
		switch( matcher.match( uri)) {
			case CODE_ITEM:
				count = helper.getWritableDatabase().update( TABLE_NAME, values, selection, selectionArgs);
				break;
			case CODE_TABLE:
				throw new IllegalArgumentException( "Can't update an entire table, bad URI: " + uri);
			default:
				throw new IllegalArgumentException( "Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange( uri, null);
		return count;
	}
	
}