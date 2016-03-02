package ylj.common.tool;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;

import com.ylj.R;
import ylj.common.bean.DeviceData;
import ylj.common.bean.DeviceInfo;

public class XmlTool
{
	public static final String TAG="XmlTool";
	public static final boolean D=true;
	
	DocumentBuilder builder;
	Context context;
	
	public XmlTool(Context context)
	{
		this.context=context;
		try {
			builder=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public DeviceData getDeviceDataFromString(String str)
	{
		DeviceData data=new DeviceData();
		try {			
			ByteArrayInputStream inputStream=new ByteArrayInputStream(str.getBytes());
			Document document=builder.parse(inputStream);	
			Log.d(TAG,"parse accept!");
			Element rootElement=document.getDocumentElement();
			NodeList list=rootElement.getChildNodes();
			for(int i=0;i<list.getLength();i++){
				Node node=list.item(i);
				String name=node.getNodeName();
				if(name.equals(context.getString(R.string.xml_pulse))){
					data.setPulse(Integer.parseInt(node.getFirstChild().getNodeValue()));
				}else if(name.equals(context.getString(R.string.xml_temp))){
					data.setTemp(Integer.parseInt(node.getFirstChild().getNodeValue()));
				}else if(name.equals(context.getString(R.string.xml_quake))){
					data.setQuake(Integer.parseInt(node.getFirstChild().getNodeValue()));
				}else if(name.equals(context.getString(R.string.xml_speed))){
					data.setSpeed(Float.parseFloat(node.getFirstChild().getNodeValue()));
				}else if(name.equals(context.getString(R.string.xml_compassHeading))){
					data.setCompassHeading(Float.parseFloat(node.getFirstChild().getNodeValue()));
				}else if(name.equals(context.getString(R.string.xml_compassPitch))){
					data.setCompassPitch(Float.parseFloat(node.getFirstChild().getNodeValue()));
				}else if(name.equals(context.getString(R.string.xml_compassRoll))){
					data.setCompassRoll(Float.parseFloat(node.getFirstChild().getNodeValue()));
				}else if(name.equals(context.getString(R.string.xml_state))){
					String value=node.getFirstChild().getNodeValue();
					if (value.equals(context.getString(R.string.xml_state_back))) {
						data.setState(DeviceData.STATE_BACKING);
					}else if(value.equals(context.getString(R.string.xml_state_stop))) {
						data.setState(DeviceData.STATE_STOP);
					}else if(value.equals(context.getString(R.string.xml_state_head))) {
						data.setState(DeviceData.STATE_HEADING);
					}
				}
			}
		} catch(DOMException e){
			Log.e(TAG,"parse error:"+str);
			return null;
		} 
		catch (SAXException e) {
			Log.e(TAG,"parse error:"+str);
			return null;
		} catch (IOException e) {
			Log.e(TAG,"parse error:"+str);
			return null;
		}
		return data;
	}
	
	public DeviceInfo getDeviceInfoFromString(String str)
	{
		DeviceInfo info=new DeviceInfo();
		try {		
			ByteArrayInputStream inputStream=new ByteArrayInputStream(str.getBytes());
			Document document=builder.parse(inputStream);		
			Log.d(TAG,"parse accept!");
			Element rootElement=document.getDocumentElement();
			NodeList list=rootElement.getChildNodes();
			if(D)Log.d(TAG, "list:"+rootElement.getNodeName()+list.getLength());
			for(int i=0;i<list.getLength();i++){
				Node node=list.item(i);
				String name=node.getNodeName();
				if(D)Log.d(TAG, "tag:"+name);
				if(D)Log.d(TAG, "value:"+node.getFirstChild().getNodeValue());
				if(name.equals(context.getString(R.string.xml_deviceId))){
					info.setDeviceId(node.getFirstChild().getNodeValue());
					if(D)Log.d(TAG, "id:"+info.getDeviceId());
				}else if(name.equals(context.getString(R.string.xml_version))){
					if(D)Log.d(TAG, "version:"+info.getVersion());
					info.setVersion(node.getFirstChild().getNodeValue());
				}else if(name.equals(context.getString(R.string.xml_softVersion))){
					info.setSoftVersion(node.getFirstChild().getNodeValue());
				}else if(name.equals(context.getString(R.string.xml_huoerState))){
					String value=node.getFirstChild().getNodeValue();
					if (value.equals(context.getString(R.string.xml_state_normal))) {
						info.setHuoerState(true);
					}else if(value.equals(context.getString(R.string.xml_state_error))) {
						info.setHuoerState(false);
					}
				}else if(name.equals(context.getString(R.string.xml_tempState))){
					String value=node.getFirstChild().getNodeValue();
					if (value.equals(context.getString(R.string.xml_state_normal))) {
						info.setTempState(true);
					}else if(value.equals(context.getString(R.string.xml_state_error))) {
						info.setTempState(false);
					}
				}else if(name.equals(context.getString(R.string.xml_dirState))){
					String value=node.getFirstChild().getNodeValue();
					if (value.equals(context.getString(R.string.xml_state_normal))) {
						info.setDirState(true);
					}else if(value.equals(context.getString(R.string.xml_state_error))) {
						info.setDirState(false);
					}
				}else if(name.equals(context.getString(R.string.xml_quakeState))){
					String value=node.getFirstChild().getNodeValue();
					if (value.equals(context.getString(R.string.xml_state_normal))) {
						info.setQuakeState(true);
					}else if(value.equals(context.getString(R.string.xml_state_error))) {
						info.setQuakeState(false);
					}
				}else if(name.equals(context.getString(R.string.xml_productDate))){
					String value=node.getFirstChild().getNodeValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					
					try {
						info.setProductDate(sdf.parse(value));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}  catch (SAXException e) {
			Log.e(TAG,"parse error:"+str);
			return info;
		} catch (IOException e) {
			Log.e(TAG,"parse error:"+str);
			return info;
		}
		return info;
	}
}
