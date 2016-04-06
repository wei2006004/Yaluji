package com.ylj.db;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
public abstract class AbstractDbManager{
    private DbManager.DaoConfig daoConfig;
    protected DbManager db;

    protected DbManager getDb(){
        return db;
    }

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

    public AbstractDbManager(String dir,String dbName) {
        daoConfig = new DbManager.DaoConfig()
                .setDbDir(new File(dir))
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
