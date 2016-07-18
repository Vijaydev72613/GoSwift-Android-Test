package com.cf.testdemo.dbHandler.framework;

import java.util.Iterator;
import java.util.Vector;


public final class SqliteTableInfo {

	
    public String m_cTableName;
    
    
    private Vector<SqliteColumInfo> m_cObjColums;

    
    public SqliteTableInfo(String pObjTableName) {
        m_cTableName = pObjTableName;
        m_cObjColums = new Vector<SqliteColumInfo>();
    }

    
    public void dispose() {
        m_cObjColums.removeAllElements();
    }

    
    public void addColumInfo(SqliteColumInfo pObjColum) {
        m_cObjColums.addElement(pObjColum);
    }

    
    public int getColumCount() {
        return m_cObjColums.size();
    }

    
    public Iterator<SqliteColumInfo> getColumIterator() {
        return m_cObjColums.iterator();
    }

    
    public boolean hasColums() {
        return (0 != getColumCount());
    }
}
