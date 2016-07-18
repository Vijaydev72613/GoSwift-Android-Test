package com.cf.testdemo.dbHandler.framework;


public final class SqliteColumInfo {

    public String m_cColumName;
    public String m_cColumType;
    public int m_cColumSize;
    public boolean m_cIsPrimaryKey;
    public boolean m_cIsNullPossible;
    public boolean m_cIsAutoIncremented;

    
    public SqliteColumInfo(String pColName, String pColType) {
        m_cColumName = pColName;
        m_cColumType = pColType;
        m_cColumSize = 0;
        m_cIsPrimaryKey = false;
        m_cIsNullPossible = true;
        m_cIsAutoIncremented = false;
    }

    
    public SqliteColumInfo(String pColName, String pColType, int pColSize) {
        m_cColumName = pColName;
        m_cColumType = pColType;
        m_cColumSize = pColSize;
        m_cIsPrimaryKey = false;
        m_cIsNullPossible = true;
        m_cIsAutoIncremented = false;
    }

    
    public SqliteColumInfo(String pColName, String pColType, int pColSize, boolean pIsPrimaryKey) {
        m_cColumName = pColName;
        m_cColumType = pColType;
        m_cColumSize = pColSize;
        m_cIsPrimaryKey = pIsPrimaryKey;
        m_cIsNullPossible = true;
        m_cIsAutoIncremented = false;
    }

    
    public SqliteColumInfo(String pColName, String pColType, boolean pIsPrimaryKey) {
        m_cColumName = pColName;
        m_cColumType = pColType;
        m_cColumSize = 0;
        m_cIsPrimaryKey = pIsPrimaryKey;
        m_cIsNullPossible = true;
        m_cIsAutoIncremented = false;
    }

    
    public SqliteColumInfo(String pColName, String pColType, int pColSize, boolean pIsPrimaryKey, boolean pIsNullPos, boolean pIsAuto) {
        m_cColumName = pColName;
        m_cColumType = pColType;
        m_cColumSize = pColSize;
        m_cIsPrimaryKey = pIsPrimaryKey;
        m_cIsNullPossible = pIsNullPos;
        m_cIsAutoIncremented = pIsAuto;
    }
}
