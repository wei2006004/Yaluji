package com.ylj.daemon.parser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public abstract class XmlParser<Result> implements IParser {
    protected DocumentBuilder mBuilder;

    public XmlParser() {
        try {
            mBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
