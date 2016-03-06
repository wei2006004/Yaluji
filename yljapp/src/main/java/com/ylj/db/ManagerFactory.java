package com.ylj.db;

import com.ylj.db.account.AccountManager;
import com.ylj.db.account.IAccountManager;
import com.ylj.db.config.Constant;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
class ManagerFactory {
    private static IAccountManager accountManager;
    public static IAccountManager getAccountManager(){
        if(accountManager==null){
            accountManager=new AccountManager(Constant.ACCOUNT_DB_NAME);
        }
        return accountManager;
    }
}
