package com.mtm.flowcheck.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mtm.flowcheck.listeners.MyStringCallback;
import com.sinosoft.key.SM2Util;

import org.json.JSONObject;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Liwenqing
 * @date 2020/3/19
 */
public class WebserviceUtil {

    /**
     * webservice请求
     * @param wsdl 接口地址
     * @param namespace 命名空间
     * @param method 接口方法名
     * @return 结果
     */
    public static void requestWebService(String wsdl, String namespace,String method, String name,String passeord){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = "";
                SoapObject soapObject = new SoapObject(namespace, method);
                soapObject.addProperty("arg0", name);
                soapObject.addProperty("arg1", passeord);
                SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapSerializationEnvelope.bodyOut = soapObject;
                soapSerializationEnvelope.dotNet = false;
                HttpTransportSE httpTransportSE = new HttpTransportSE(wsdl,6000);
                httpTransportSE.debug = true;
//添加HeaderProperty信息，解决调用call的时候报java.io.EOFException错误
                ArrayList headerPropertyArrayList = new ArrayList<>();
                headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
                try {
                    httpTransportSE.call("", soapSerializationEnvelope,headerPropertyArrayList);
                    if(soapSerializationEnvelope.getResponse()!=null){
                        result = soapSerializationEnvelope.getResponse().toString();
                        Log.e("ss",result+" aa");
                    }
                } catch (SocketTimeoutException e) {

                    e.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                }
//        return result;
            }
        }).start();
    }

    /**
     * 请求webservice的步骤
     *
     * @param wsdl_url   wsdl 的uri，接口地址 类似于这种：http://47.95.217.28:8080/services/translateMacs?wsdl
     * @param name_sapce 命名空间 在wsdl_url链接里面有命名空间相关信息。
     * @param methodName 方法名
     * @param map        传给webservice的参数。
     * @return
     */

    public static void questToWebService(String wsdl_url, String name_sapce, String methodName, HashMap<String, String> map) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = "";
                //（1）创建HttpTransportSE对象，该对象用于调用WebService操作
                HttpTransportSE httpTransportSE = new HttpTransportSE(wsdl_url);
                //（2）创建SoapSerializationEnvelope对象
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                //（3）创建SoapObject对象，创建该对象时需要传入所要调用的Web Service的命名空间和WebService方法名
                SoapObject request = new SoapObject(name_sapce, methodName);
                //(4) //填入需要传入的参数
                Iterator iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    String value = map.get(key);
                    request.addProperty(key, value);
                }
                envelope.dotNet = false;
                envelope.bodyOut = request;
                //（5）调用SoapSerializationEnvelope的setOutputSoapObject()方法，或者直接对bodyOut属性赋值，
                //将前两步创建的SoapObject对象设为SoapSerializationEnvelope的传出SOAP消息体
//                envelope.setOutputSoapObject(request);
                try {
                    //（6）调用对象的call()方法，并以SoapSerializationEnvelope作为参数调用远程的web service
                    httpTransportSE.call( name_sapce+methodName, envelope);//调用
                    if (envelope.getResponse() != null) {
                        result = envelope.getResponse().toString();
                    }
                    Log.e("resultbb", result + "  aaa");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                //解析该对象，即可获得调用web service的返回值
                Log.e("result", result + "  aaa");
            }
        }).start();
    }

    private MyStringCallback callBack;
    private String methodName, url;
    private Map<String, String> request = new HashMap<String, String>();

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addParams(String id, String s) {
        request.put(id, s);
    }

    public void setCallBack(MyStringCallback callBack) {
        this.callBack = callBack;
    }

    public void exec(MyStringCallback callBack) {
        this.callBack = callBack;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = mHandler.obtainMessage();
                message.what = 1;
                String result = "";
                SoapObject soapObject = new SoapObject(HTTPUtils.NAME_SPACE, methodName);
                for (String key : request.keySet()) {
                    soapObject.addProperty(key, request.get(key));
                }
                SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapSerializationEnvelope.bodyOut = soapObject;
                soapSerializationEnvelope.dotNet = false;
                HttpTransportSE httpTransportSE = new HttpTransportSE(url, 6000);
                httpTransportSE.debug = true;
                //添加HeaderProperty信息，解决调用call的时候报java.io.EOFException错误
                ArrayList headerPropertyArrayList = new ArrayList<>();
                headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
                try {
                    httpTransportSE.call("", soapSerializationEnvelope, headerPropertyArrayList);
                    if (soapSerializationEnvelope.getResponse() != null) {
                        result = soapSerializationEnvelope.getResponse().toString();
                        message.what = 1;
                    }
                } catch (SocketTimeoutException e) {
                    message.what = 2;
                    e.printStackTrace();
                } catch (Exception e) {
                    message.what = 2;
                    e.printStackTrace();
                }
                message.obj = result;
                mHandler.sendMessage(message);
//        return result;
            }
        }).start();

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    String s= (String) msg.obj;
                    callBack.onSuccess(s);
                    break;
                case 2:
                    callBack.onError("失败");
                    break;
                default:
                    break;
            }
        }
    };
}
