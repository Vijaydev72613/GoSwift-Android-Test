package com.cf.testdemo.dbHandler.framework;

import android.content.ContentValues;
import android.database.Cursor;

import com.cf.testdemo.controller.CfApplicationController;


public abstract class TableHandlerBase implements OnlyOneDBHandler.TableHandler {
	
	protected OnlyOneDBHandler m_cObjDBHandler;
	protected SqliteTableInfo m_cObjTableInfo;

	
	protected TableHandlerBase(OnlyOneDBHandler pObjDBHandler) {
		m_cObjDBHandler = pObjDBHandler;
		m_cObjDBHandler.addDBListner(this);
	}

	@Override
	public SqliteTableInfo getCreateTableInfo() {
		return m_cObjTableInfo;
	}

	@Override
	public void databaseClosed() {
		CfApplicationController.StuffsPrint(">>>>TableHandlerBase:databaseClosed()-OnlyOneDBHandler is null....");
		m_cObjDBHandler = null;
		m_cObjTableInfo = null;
	}

	
	public void createTable() {
		m_cObjDBHandler.createTable(this);
	}
	
	public OnlyOneDBHandler getDBHandler() {
		return m_cObjDBHandler;
	}
	
	
	public Cursor getTableContent() {
		Cursor lRetVal = null;
		if(null != m_cObjDBHandler) {
			lRetVal = m_cObjDBHandler.getDBCursor(m_cObjTableInfo.m_cTableName, null, null, null, -1, 0);
		}
		return lRetVal;
	}
	
	
	public void deleteTableContent() {
		if(null != m_cObjDBHandler) {
			m_cObjDBHandler.deleteTableContent(m_cObjTableInfo.m_cTableName, null);
		}
	}
	
	public void deleteRowWithID(String pSelectedItemId) {
		if(null != m_cObjDBHandler) {
			m_cObjDBHandler.deleteTableContent(m_cObjTableInfo.m_cTableName, DBMacros.ID_COLUM_NAME() + DBMacros.EQUALS_STATEMENT_SQL() + pSelectedItemId);
		}
	}
	
	public int getRowsCount() {
		int lRetVal = 0;
		if(null != m_cObjDBHandler) {
	    	lRetVal = m_cObjDBHandler.getRowsCount(m_cObjTableInfo.m_cTableName, null);
    	}
		return lRetVal;
	}

	public long getLastEntryID() {
		long lRetVal = 0;
		Cursor lObjCursor = m_cObjDBHandler.getDBCursor(m_cObjTableInfo.m_cTableName,
				new String[]{DBMacros.ID_COLUM_NAME()},
				null,
				DBMacros.ID_COLUM_NAME() + DBMacros.DESCENDING_SORT_STRING(),
				0,1);
		if(null != lObjCursor) {
			if(lObjCursor.moveToFirst()) {
				lRetVal = getLongValue(lObjCursor, DBMacros.ID_COLUM_NAME());
			}
			lObjCursor.close();
			lObjCursor = null;
		}
		return lRetVal;
	}
	
	public long addEntry(ContentValues pObjContentValues) {
		m_cObjDBHandler.insertIntoTable(m_cObjTableInfo.m_cTableName, pObjContentValues);
		return getLastEntryID();
	}
	
	public void updateEntry(ContentValues pObjContentValues, long pEntryID) {
		m_cObjDBHandler.updateTableContent(m_cObjTableInfo.m_cTableName, pObjContentValues,DBMacros.ID_COLUM_NAME() + DBMacros.EQUALS_STATEMENT_SQL()+ pEntryID, null);
	}
	
	
	public static String getStringValue(Cursor pObjCursor, String pColName) {
    	int lColIndex = pObjCursor.getColumnIndex(pColName);
    	return pObjCursor.getString(lColIndex);
    }
	
	public static Float getFloatValue(Cursor pObjCursor, String pColName) {
		int lColIndex = pObjCursor.getColumnIndex(pColName);
		return pObjCursor.getFloat(lColIndex);
	}
    
	
	public static byte getByteValue(Cursor pObjCursor, String pColName) {
    	int lColIndex = pObjCursor.getColumnIndex(pColName);
    	return Byte.parseByte(pObjCursor.getString(lColIndex));
    }
    
	
	public static int getIntValue(Cursor pObjCursor, String pColName) {
    	int lColIndex = pObjCursor.getColumnIndex(pColName);
    	return pObjCursor.getInt(lColIndex);
    }
    
	
	public static long getLongValue(Cursor pObjCursor, String pColName) {
    	int lColIndex = pObjCursor.getColumnIndex(pColName);
    	return pObjCursor.getLong(lColIndex);
    }
    
	
	public static boolean getBoolValue(Cursor pObjCursor, String pColName) {
    	return (getByteValue(pObjCursor, pColName) == 1);
    }
    
	
	public static double getDoubleValue(Cursor pObjCursor, String pColName) {
    	int lColIndex = pObjCursor.getColumnIndex(pColName);
    	return pObjCursor.getDouble(lColIndex);
    }

	
	public static short getShortValue(Cursor pObjCursor, String pColName) {
    	int lColIndex = pObjCursor.getColumnIndex(pColName);
    	return pObjCursor.getShort(lColIndex);
    }
}
