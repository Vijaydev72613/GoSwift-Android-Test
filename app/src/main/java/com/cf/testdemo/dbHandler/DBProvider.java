package com.cf.testdemo.dbHandler;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.cf.testdemo.constants.CFMacros;

public class DBProvider extends ContentProvider {
	private static String TAG = DBProvider.class.getSimpleName();
	private static String DATABASE_NAME = CFMacros.CONFIG_DB;

	public static final int DB_FIRST_VERSION = 1;
	public static final int DATABASE_VERSION = DB_FIRST_VERSION;

	private static final UriMatcher m_cUriMatcher;


	private ManageDatabase m_cManageDatabase;

	private static class ManageDatabase extends SQLiteOpenHelper {
		public ManageDatabase(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			getWritableDatabase();
		}

		@Override
		public void onCreate(SQLiteDatabase pDb) {
			Log.d(TAG, "Oncreate DBProvider--");
			try {
				createAllTabels(pDb);
			} catch (Exception ex) {

			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase pDb, int pOldVersion,
				int pNewVersion) {
			Log.d(TAG, "Upgrading database from version " + pOldVersion
					+ " to " + pNewVersion
					+ ", which will destroy all old data");

			if (pOldVersion < pNewVersion) {
				dropAllTabels(pDb);
				createAllTabels(pDb);
			}
		}

		public void dropAllTabels(SQLiteDatabase pDb) {
		}

		public void createAllTabels(SQLiteDatabase pDb) {
		}



	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase lDb = m_cManageDatabase.getWritableDatabase();
		int lCount = -1;
		switch (m_cUriMatcher.match(uri)) {
		}
		return lCount;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri pUri, ContentValues pValues) {
		switch (m_cUriMatcher.match(pUri)) {
		}
		return null;
	}

	@Override
	public boolean onCreate() {
		m_cManageDatabase = new ManageDatabase(getContext());
		SQLiteDatabase lDB = m_cManageDatabase.getWritableDatabase();
		return (lDB == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder pQb = new SQLiteQueryBuilder();
		switch (m_cUriMatcher.match(uri)) {
		}

		String lOrderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			lOrderBy = null;
		} else {
			lOrderBy = sortOrder;
		}

		SQLiteDatabase lDb = m_cManageDatabase.getReadableDatabase();
		try {
			Cursor lC = pQb.query(lDb, projection, selection, selectionArgs,
					null, null, lOrderBy);
			return lC;
		} catch (Exception e) {
		}

		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase lDb = m_cManageDatabase.getWritableDatabase();
		int count = -1;

		switch (m_cUriMatcher.match(uri)) {
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	static {
		m_cUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	}
}