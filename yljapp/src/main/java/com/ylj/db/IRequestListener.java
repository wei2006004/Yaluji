package com.ylj.db;

/**
 * Created by Administrator on 2016/3/6 0006.
 */
public interface IRequestListener<Result> {
    void onRequestSuccess(Result result);
    void onRequestFail(int error);
}
