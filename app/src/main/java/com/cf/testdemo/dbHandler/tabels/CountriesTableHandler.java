package com.cf.testdemo.dbHandler.tabels;

import com.cf.testdemo.dbHandler.framework.OnlyOneDBHandler;
import com.cf.testdemo.dbHandler.framework.TableHandlerBase;

/**
 * Created by Arun on 10-Jul-15.
 */
public class CountriesTableHandler extends TableHandlerBase {
    private String TAG = CountriesTableHandler.class.getSimpleName();

    private static CountriesTableHandler m_cObjInstance = null;

    protected CountriesTableHandler(OnlyOneDBHandler pObjDBHandler) {
        super(pObjDBHandler);
        initTableInfo();
    }

    public static synchronized CountriesTableHandler getInstance(OnlyOneDBHandler pObjDBHandler) {
        if (null == m_cObjInstance) {
            m_cObjInstance = new CountriesTableHandler(pObjDBHandler);
        }
        return m_cObjInstance;
    }

    private void initTableInfo() {
    }

    @Override
    public void databaseClosed() {
        super.databaseClosed();
        deInitailize();
    }

    private static void deInitailize() {
        m_cObjInstance = null;
    }

}
