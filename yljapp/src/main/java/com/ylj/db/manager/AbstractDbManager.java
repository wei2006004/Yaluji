package com.ylj.db.manager;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
public abstract class AbstractDbManager extends AbstractManager {
    private DbManager.DaoConfig daoConfig;
    protected DbManager db;

    public AbstractDbManager(String dbName) {
        daoConfig = new DbManager.DaoConfig()
                .setDbName(dbName)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();
                    }
                });
        db = x.getDb(daoConfig);
    }
}
