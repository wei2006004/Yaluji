package com.ylj.daemon.client;

import android.content.Context;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class ClientFactory {

    public final static int CLIENT_TYPE_YLJ = 0;

    public static BaseClient createClient(Context context,int type){
        switch (type){
            case CLIENT_TYPE_YLJ:
                return new YljClient(context);
        }
        return null;
    }
}
