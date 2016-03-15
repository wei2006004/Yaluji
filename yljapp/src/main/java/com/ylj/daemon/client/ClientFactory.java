package com.ylj.daemon.client;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class ClientFactory {

    public final static int CLIENT_TYPE_YLJ = 0;

    public static BaseClient createClient(int type){
        switch (type){
            case CLIENT_TYPE_YLJ:
                return new YljClient();
        }
        return null;
    }
}
