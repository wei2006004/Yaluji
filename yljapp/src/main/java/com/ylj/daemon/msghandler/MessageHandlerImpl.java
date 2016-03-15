package com.ylj.daemon.msghandler;

import com.ylj.connect.bean.DeviceInfo;
import com.ylj.daemon.config.XmlTag;
import com.ylj.daemon.parser.DeviceDataParser;
import com.ylj.daemon.parser.DeviceInfoParser;
import com.ylj.task.bean.DeviceData;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class MessageHandlerImpl implements IMessageHandler {

    private OnHandleListener mListener;

    private DeviceDataParser mDataParser = new DeviceDataParser();
    private DeviceInfoParser mInfoParser = new DeviceInfoParser();

    public MessageHandlerImpl(OnHandleListener listener) {
        mListener = listener;
    }

    @Override
    public void onHandleMessage(final String msg) {
        try {
            if (msg.contains(XmlTag.XML_deviceInfo)) {
                DeviceInfo info = mInfoParser.parserMessage(msg);
                mListener.onHandleDeviceInfo(info);
            } else if (msg.contains(XmlTag.XML_deviceData)) {
                DeviceData deviceData = mDataParser.parserMessage(msg);
                mListener.onHandleDeviceData(deviceData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mListener.onHandleWrongMessage(msg);
        }

    }
}
