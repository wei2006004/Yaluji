package com.ylj.daemon.parser;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public interface IParser<Result> {
    Result parserMessage(String message)throws Exception;
}
