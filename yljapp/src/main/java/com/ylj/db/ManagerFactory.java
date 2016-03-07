package com.ylj.db;

import com.ylj.db.account.AccountManager;
import com.ylj.db.config.Constant;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
class ManagerFactory {
    private static AccountManager accountManager;
    public static AccountManager getAccountManager(){
        if(accountManager==null){
            accountManager=new AccountManager(Constant.ACCOUNT_DB_NAME);
        }
        return accountManager;
    }
}
