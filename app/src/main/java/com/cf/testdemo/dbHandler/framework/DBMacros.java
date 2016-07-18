package com.cf.testdemo.dbHandler.framework;


public final class DBMacros {

	public static String CREATE_TABLE_SQL() {
		return "CREATE TABLE IF NOT EXISTS ";
	}
	public static String STATEMENT_OPEN_MARKER() {
		return " (";
	}
	public static String QUERY_CLOSE_MARKER() {
		return ")";
	}
	public static String PRIMARY_KEY_SQL() {
		return " PRIMARY KEY";
	}
	public static String AUTO_INCREMENT_SQL() {
		return " AUTOINCREMENT";
	}
	public static String NOT_NULL_SQL() {
		return " NOT NULL";
	}
	public static String STATEMENT_ELEMENT_SEPARATOR() {
		return ",";
	}
	public static String STATEMENT_CLOSE_MARKER() {
		return ");";
	}
    public static String ASCENDING_SORT_STRING() {
		return " ASC ";
    }
    public static String DESCENDING_SORT_STRING() {
		return " DESC ";
    }
    public static String AND_STATEMENT_SQL() {
		return " AND ";
    }
    public static String OR_STATEMENT_SQL() {
		return " OR ";
    }
    public static String LIKE_STATEMENT_SQL() {
		return " LIKE ";
    }
    public static String WHERE_STATEMENT_SQL() {
		return " WHERE ";
    }
    public static String GREATER_THAN_STATEMENT_SQL() {
		return " > ";
    }
    public static String LESS_THAN_STATEMENT_SQL() {
		return " < ";
    }
    public static String EQUALS_STATEMENT_SQL() {
		return " = ";
    }
    public static String NOT_IN_STATEMENT_SQL() {
		return " NOT IN ";
    }
    public static String IN_STATEMENT_SQL() {
    	return " IN ";
    }
    
	public static String ID_COLUM_NAME() {
		return "_id";
	}
}
