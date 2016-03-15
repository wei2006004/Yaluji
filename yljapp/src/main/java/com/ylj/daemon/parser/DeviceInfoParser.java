package com.ylj.daemon.parser;

import com.ylj.connect.bean.DeviceInfo;
import com.ylj.daemon.config.XmlTag;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class DeviceInfoParser extends XmlParser<DeviceInfo> {

    @Override
    public DeviceInfo parserMessage(String str) throws Exception {
        DeviceInfo info = new DeviceInfo();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes());
        Document document = mBuilder.parse(inputStream);
        Element rootElement = document.getDocumentElement();
        NodeList list = rootElement.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            String name = node.getNodeName();
            if (name.equals(XmlTag.XML_deviceId)) {
                info.setDeviceId(node.getFirstChild().getNodeValue());
            } else if (name.equals(XmlTag.XML_version)) {
                info.setVersion(node.getFirstChild().getNodeValue());
            } else if (name.equals(XmlTag.XML_softVersion)) {
                info.setSoftVersion(node.getFirstChild().getNodeValue());
            } else if (name.equals(XmlTag.XML_huoerState)) {
                String value = node.getFirstChild().getNodeValue();
                if (value.equals(XmlTag.XML_state_normal)) {
                    info.setHuoerState(true);
                } else if (value.equals(XmlTag.XML_state_error)) {
                    info.setHuoerState(false);
                }
            } else if (name.equals(XmlTag.XML_tempState)) {
                String value = node.getFirstChild().getNodeValue();
                if (value.equals(XmlTag.XML_state_normal)) {
                    info.setTempState(true);
                } else if (value.equals(XmlTag.XML_state_error)) {
                    info.setTempState(false);
                }
            } else if (name.equals(XmlTag.XML_dirState)) {
                String value = node.getFirstChild().getNodeValue();
                if (value.equals(XmlTag.XML_state_normal)) {
                    info.setDirState(true);
                } else if (value.equals(XmlTag.XML_state_error)) {
                    info.setDirState(false);
                }
            } else if (name.equals(XmlTag.XML_quakeState)) {
                String value = node.getFirstChild().getNodeValue();
                if (value.equals(XmlTag.XML_state_normal)) {
                    info.setQuakeState(true);
                } else if (value.equals(XmlTag.XML_state_error)) {
                    info.setQuakeState(false);
                }
            } else if (name.equals(XmlTag.XML_productDate)) {
                String value = node.getFirstChild().getNodeValue();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                info.setProductDate(sdf.parse(value));
            }
        }
        return info;
    }
}
