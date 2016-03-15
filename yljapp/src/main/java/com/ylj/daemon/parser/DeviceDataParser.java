package com.ylj.daemon.parser;

import com.ylj.daemon.config.XmlTag;
import com.ylj.task.bean.DeviceData;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class DeviceDataParser extends XmlParser<DeviceData> {

    @Override
    public DeviceData parserMessage(String str) throws Exception {
        DeviceData data = new DeviceData();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes());
        Document document = mBuilder.parse(inputStream);
        Element rootElement = document.getDocumentElement();
        NodeList list = rootElement.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            String name = node.getNodeName();
            if (name.equals(XmlTag.XML_pulse)) {
                data.setPulse(Integer.parseInt(node.getFirstChild().getNodeValue()));
            } else if (name.equals(XmlTag.XML_temp)) {
                data.setTemp(Integer.parseInt(node.getFirstChild().getNodeValue()));
            } else if (name.equals(XmlTag.XML_quake)) {
                data.setQuake(Integer.parseInt(node.getFirstChild().getNodeValue()));
            } else if (name.equals(XmlTag.XML_speed)) {
                data.setSpeed(Float.parseFloat(node.getFirstChild().getNodeValue()));
            } else if (name.equals(XmlTag.XML_compassHeading)) {
                data.setCompassHeading(Float.parseFloat(node.getFirstChild().getNodeValue()));
            } else if (name.equals(XmlTag.XML_compassPitch)) {
                data.setCompassPitch(Float.parseFloat(node.getFirstChild().getNodeValue()));
            } else if (name.equals(XmlTag.XML_compassRoll)) {
                data.setCompassRoll(Float.parseFloat(node.getFirstChild().getNodeValue()));
            } else if (name.equals(XmlTag.XML_state)) {
                String value = node.getFirstChild().getNodeValue();
                if (value.equals(XmlTag.XML_state_back)) {
                    data.setState(DeviceData.STATE_BACKING);
                } else if (value.equals(XmlTag.XML_state_stop)) {
                    data.setState(DeviceData.STATE_STOP);
                } else if (value.equals(XmlTag.XML_state_head)) {
                    data.setState(DeviceData.STATE_HEADING);
                }
            }
        }
        return data;
    }
}
