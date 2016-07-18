package com.cf.testdemo.dbHandler.framework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.cf.testdemo.constants.CFMacros;
import com.cf.testdemo.controller.CfApplicationController;
import com.cf.testdemo.dbHandler.DBProvider;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

public final class OnlyOneDBHandler extends SQLiteOpenHelper {

	private static String DB_PATH;
	private static String DB_NAME = CFMacros.CONFIG_DB;
	private Vector<TableHandler> m_cObjTableHandlers;
	private SQLiteDatabase m_cObjDatabaseHandle;
	private static OnlyOneDBHandler m_cDBHandler;

	public static void DB_PATH(String pPath) {
		DB_PATH = pPath;
	}

	public static String DB_NAME() {
		return DB_NAME;
	}

	@Override
	public void onCreate(SQLiteDatabase pDB) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase pDB, int pOldVersion, int pNewVersion) {
	}

	@Override
	public synchronized void close() {
		if (null != m_cObjDatabaseHandle) {
			if (true == m_cObjDatabaseHandle.isOpen()) {
				m_cObjDatabaseHandle.close();
				CfApplicationController
						.StuffsPrint(">>>>clearCacheData()-Database closed....");
			}
			m_cObjDatabaseHandle = null;
		}
		if (null != m_cObjTableHandlers) {
			for (TableHandler lObjTableHandler : m_cObjTableHandlers) {
				lObjTableHandler.databaseClosed();
			}
			m_cObjTableHandlers.removeAllElements();
		}
		m_cObjTableHandlers = null;

		super.close();
	}

	public boolean isDBOperateable() {
		return (null != m_cObjDatabaseHandle);
	}

	private OnlyOneDBHandler(Context pContext) {
		super(pContext, DB_NAME, null, DBProvider.DATABASE_VERSION);
	}

	public static synchronized OnlyOneDBHandler getInstance(Context pContext) {
		if (m_cDBHandler == null) {
			m_cDBHandler = new OnlyOneDBHandler(
					pContext.getApplicationContext());
		}
		return m_cDBHandler;
	}

	public void createDataBase() throws IOException {
		boolean lDbExist = checkDataBase();
		if (lDbExist == false) {

			m_cObjDatabaseHandle = this.getWritableDatabase();
		}
		if (null != m_cObjDatabaseHandle) {
			if (null != m_cObjTableHandlers) {
				for (TableHandler lObjTableHandler : m_cObjTableHandlers) {
					createTable(lObjTableHandler);
				}
			}
		}
	}

	public void createTable(TableHandler pObjTableHandler) {
		String lObjStatement = null;
		if (null != m_cObjDatabaseHandle && null != pObjTableHandler) {
			SqliteTableInfo lObjTableCreateInfo = pObjTableHandler
					.getCreateTableInfo();
			if (null != lObjTableCreateInfo) {

				lObjStatement = getCreateTableStatement(lObjTableCreateInfo);
				if (null != lObjStatement) {
					m_cObjDatabaseHandle.execSQL(lObjStatement);
				}
			}
		}
	}

	private String getCreateTableStatement(SqliteTableInfo pObjTableInfo) {
		String lRetVal = null;
		int i = 0, lCnt;

		if (true == pObjTableInfo.hasColums()) {
			lCnt = pObjTableInfo.getColumCount();
			StringBuffer lObjBuffer = new StringBuffer();
			lObjBuffer.append(DBMacros.CREATE_TABLE_SQL());
			lObjBuffer.append(pObjTableInfo.m_cTableName).append(
					DBMacros.STATEMENT_OPEN_MARKER());
			Iterator<SqliteColumInfo> lObjColumInfo = pObjTableInfo
					.getColumIterator();
			while (lObjColumInfo.hasNext()) {
				SqliteColumInfo lEachColum = lObjColumInfo.next();
				lObjBuffer.append(lEachColum.m_cColumName).append(" ")
						.append(lEachColum.m_cColumType);
				if (0 < lEachColum.m_cColumSize) {
					lObjBuffer.append("(").append(lEachColum.m_cColumSize);
					lObjBuffer.append(DBMacros.QUERY_CLOSE_MARKER());
				}
				if (true == lEachColum.m_cIsPrimaryKey) {
					lObjBuffer.append(DBMacros.PRIMARY_KEY_SQL());
				}
				if (true == lEachColum.m_cIsAutoIncremented) {
					lObjBuffer.append(DBMacros.AUTO_INCREMENT_SQL());
				}
				if (false == lEachColum.m_cIsNullPossible) {
					lObjBuffer.append(DBMacros.NOT_NULL_SQL());
				}
				if (i < lCnt - 1) {
					lObjBuffer.append(DBMacros.STATEMENT_ELEMENT_SEPARATOR())
							.append(" ");
				}
				i++;
			}
			lObjBuffer.append(DBMacros.STATEMENT_CLOSE_MARKER());
			lRetVal = lObjBuffer.toString();
		}
		return lRetVal;
	}

	private boolean checkDataBase() {
		boolean lRetVal = false;
		if (null == m_cObjDatabaseHandle) {
			try {
				m_cObjDatabaseHandle = SQLiteDatabase.openDatabase(DB_PATH,
						null, SQLiteDatabase.OPEN_READWRITE);

			} catch (SQLiteException e) {
				CfApplicationController
						.ExceptionPrint("checkDataBase()-SQLiteException open database....");
			} catch (Exception pEx) {
				CfApplicationController
						.ExceptionPrint("checkDataBase()-Exception open database....");
			}
		}
		if (m_cObjDatabaseHandle != null) {
			lRetVal = true;
		}
		return lRetVal;
	}

	public void addDBListner(TableHandler pObjListner) {
		if (null == m_cObjTableHandlers) {
			m_cObjTableHandlers = new Vector<TableHandler>();
		}
		m_cObjTableHandlers.addElement(pObjListner);
	}

	public Cursor getDBCursor(String pTableName, String[] pColNames,
			String pWhereClause, String pOrderBy, int pStartIndex, int pCount) {
		Cursor lRetVal = null;
		try {
			String lLimitSTR = null;
			if (-1 < pStartIndex) {
				lLimitSTR = String.valueOf(pStartIndex);
			}
			if (0 < pCount) {
				if (null != lLimitSTR) {
					lLimitSTR += DBMacros.STATEMENT_ELEMENT_SEPARATOR()
							+ String.valueOf(pCount);
				} else {
					lLimitSTR = String.valueOf(pCount);
				}
			}
			lRetVal = m_cObjDatabaseHandle.query(pTableName, pColNames,
					pWhereClause, null, null, null, pOrderBy, lLimitSTR);
		} catch (Exception ex) {
			CfApplicationController.ExceptionPrint(String.format(
					"OnlyOneDBHandler:DBHandler: Query failed: %s",
					ex.toString()));
		}
		return lRetVal;
	}

	public void deleteTableContent(String pTableName, String pWhereClause) {
		try {
			m_cObjDatabaseHandle.delete(pTableName, pWhereClause, null);
		} catch (Exception ex) {
		}
	}

	public void insertIntoTable(String pTableName, ContentValues pObjValues) {
		try {
			m_cObjDatabaseHandle.insert(pTableName, null, pObjValues);
		} catch (Exception ex) {
		}
	}

	public void updateTableContent(String pTableName, ContentValues pObjValues,
			String pWhereClause ,String[] pWhereArgs) {
		try {
			m_cObjDatabaseHandle.update(pTableName, pObjValues, pWhereClause, pWhereArgs);
		} catch (Exception ex) {
		}
	}

	public int getRowsCount(String pTableName, String pWhereCondition) {
		int lRetVal = 0;
		if (null != m_cObjDatabaseHandle) {
			String lQuery = "SELECT COUNT(*) AS \"Cnt\" FROM " + pTableName;
			if (null != pWhereCondition) {
				lQuery += pWhereCondition;
			}
			try {
				Cursor lObjCursor = m_cObjDatabaseHandle.rawQuery(lQuery, null);
				lObjCursor.moveToFirst();
				try {
					lRetVal = lObjCursor.getInt(lObjCursor
							.getColumnIndex("Cnt"));
				} catch (Exception ex) {
					if (null != lObjCursor)
						lObjCursor.close();
					lObjCursor = null;
				}
				if (null != lObjCursor)
					lObjCursor.close();
				lObjCursor = null;
			} catch (Exception ex) {
			}
		}
		return lRetVal;
	}

	public Cursor getDBCursor(String pRawQuery) {
		Cursor lRetVal = null;
		try {
			if (null != m_cObjDatabaseHandle
					&& (true == m_cObjDatabaseHandle.isOpen())) {
				lRetVal = m_cObjDatabaseHandle.rawQuery(pRawQuery, null);
			}
		} catch (Exception ex) {
			CfApplicationController.ExceptionPrint("ERROR:: getDBCursor -> "
					+ ex.toString());
		}
		return lRetVal;
	}

	public void executeRawQuery(String pQuery, Object[] pBinderObjects) {
		try {
			if (null != m_cObjDatabaseHandle) {
				m_cObjDatabaseHandle.execSQL(pQuery);
			}
		} catch (Exception ex) {
			CfApplicationController
					.ExceptionPrint("ERROR:: executeRawQuery -> "
							+ ex.toString());
		}
	}

	public interface TableHandler {

		public SqliteTableInfo getCreateTableInfo();

		public void databaseClosed();
	}

	public boolean isDBOpen() {
		if (null != m_cObjDatabaseHandle) {
			return m_cObjDatabaseHandle.isOpen();
		} else {
			return false;
		}

	}

}
