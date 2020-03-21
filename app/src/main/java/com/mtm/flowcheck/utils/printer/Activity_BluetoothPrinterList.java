/**
 * Activity of Searching Bluetooth Printers
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.mtm.flowcheck.utils.printer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mtm.flowcheck.R;
import com.mtm.flowcheck.utils.MdDialogUtils;
import com.mtm.flowcheck.utils.printer.common.Common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.UUID;


public class Activity_BluetoothPrinterList extends ListActivity {

    private static final int MSG_CONN_NO = 1;
    private static final int MSG_CONN_YES = 2;
    private static final int MSG_CONN_ERR = 3;
    private BluetoothAdapter bluetoothAdapter;
    private String ipAddress;
    private String macAddress;
    private String modelName;
    private BluetoothSocket mBluetoothSocket;
    private MicroPrinterAdapter microPrinterAdapter;
    private List<Map<String, Object>> strList = new ArrayList<Map<String, Object>>();
    private SharedPreferences sharedPreferences;
    static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    /**
     * initialize activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netprinterlist);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshButtonOnClick();
            }
        });


        Button btPrinterSettings = (Button) findViewById(R.id.btPrinterSettings);
        btPrinterSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsButtonOnClick();
            }
        });
        getPairedPrinters();
        this.setTitle(R.string.bluetooth_printer_list_title_label);
    }

    /**
     * Called when [Settings] button is tapped
     */
    private void settingsButtonOnClick() {
        Intent bluetoothSettings = new Intent(
                android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivityForResult(bluetoothSettings,
                Common.ACTION_BLUETOOTH_SETTINGS);
    }

    /**
     * Called when [Refresh] button is tapped
     */
    private void refreshButtonOnClick() {
        getPairedPrinters();

    }

    /**
     * Called when the Settings activity exits
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Common.ACTION_BLUETOOTH_SETTINGS) {
            getPairedPrinters();
        }
    }

    /**
     * get paired printers
     */
    private void getPairedPrinters() {
        // get the BluetoothAdapter
        bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(enableBtIntent);
            }
        } else {
            return;
        }

        try {

            /*
             * if the paired devices exist, set the paired devices else set the
             * string of "No Bluetooth Printer."
             */
            List<BluetoothDevice> pairedDevices = getPairedBluetoothDevice(bluetoothAdapter);
            if (pairedDevices.size() > 0) {
                strList = new ArrayList<Map<String, Object>>();
                String sMacAddress = sharedPreferences.getString("macAddress", "");
                for (BluetoothDevice device : pairedDevices) {
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("PRT_NAME", device.getName() + "|"
                            + "已配对");
                    hashMap.put("PRT_DEV_NAME", device.getName());
                    hashMap.put("PRT_ADDRESS", device.getAddress());
                    if (sMacAddress.equals(device.getAddress())) {
                        hashMap.put("CURR_IMG", R.drawable.check_on);
                    } else {
                        hashMap.put("CURR_IMG", R.drawable.check_off);
                    }
                    strList.add(hashMap);
                }
            }
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    microPrinterAdapter = new MicroPrinterAdapter(strList,
                            Activity_BluetoothPrinterList.this);
//                    ArrayAdapter<String> fileList = new ArrayAdapter<String>(
//                            Activity_BluetoothPrinterList.this,
//                            android.R.layout.test_list_item, mItems);
                    Activity_BluetoothPrinterList.this.setListAdapter(microPrinterAdapter);

                }
            });
        } catch (Exception ignored) {
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private List<BluetoothDevice> getPairedBluetoothDevice(BluetoothAdapter bluetoothAdapter) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices == null || pairedDevices.size() == 0) {
            return new ArrayList<>();
        }
        ArrayList<BluetoothDevice> devices = new ArrayList<>();
        for (BluetoothDevice device : pairedDevices) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                devices.add(device);
            } else {
                if (device.getType() != BluetoothDevice.DEVICE_TYPE_LE) {
                    devices.add(device);
                }
            }
        }

        return devices;
    }

    public void showProcessDialog(String msg) {
        MdDialogUtils.getInstance().showLoadingDialog(Activity_BluetoothPrinterList.this, msg,true);
    }

    public void dismissDlg() {
        MdDialogUtils.getInstance().dismissLoadingDialog();
    }

    /**
     * Called when an item in the list is tapped
     */
    @Override
    protected void onListItemClick(ListView listView, View view, int position,
                                   long id) {
        showProcessDialog("正在连接打印设备,请稍候...");
        Map<String, Object> map = (Map<String, Object>) getListAdapter().getItem(position);
        modelName = (String) map.get("PRT_DEV_NAME");
        ipAddress = (String) map.get("PRT_ADDRESS");
        macAddress = (String) map.get("PRT_ADDRESS");
        getBlueId(macAddress);
    }

    /**
     * 连接打印机，发送指令
     * 获取UserId GroudId
     *
     * @param macAddress
     */
    public void getBlueId(final String macAddress) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes = {0x1B, 0x69, 0x61, 0x00, 0x1B, 0x69, 0x55, 0x49, 0x01};
                UUID uuid = UUID.fromString(SPP_UUID);
                BluetoothDevice btDev = bluetoothAdapter
                        .getRemoteDevice(macAddress);// 根据蓝牙地址获取远程蓝牙设备
                final Message msg = new Message();
                try {
                    if (mBluetoothSocket != null) {
                        mBluetoothSocket.close();
                    }
                    mBluetoothSocket = btDev
                            .createRfcommSocketToServiceRecord(uuid);// 根据UUID创建并返回一个BluetoothSocket
                    mBluetoothSocket.connect();
                    if (mBluetoothSocket != null) {
                        OutputStream outStream = mBluetoothSocket.getOutputStream();
                        InputStream is = mBluetoothSocket.getInputStream();
                        if (outStream != null) {
                            // 以utf-8的格式发送出去
                            outStream.write(bytes);
                        }
//                        TODO  暂时关闭型号验证功能
//                        if (is != null) {
//                            /**
//                             * 设备一共返回6个字节数据
//                             * 前四个字节代表的groupId  String类型
//                             * 后两个字节代表的UserId  int类型
//                             */
//                            byte[] gBytes = InputToBytesGroupId(is);
//                            final String btGroupId = ByteToString(gBytes);
//                            byte[] in2b = InputToBytesUserId(is);
//                            final int btUserId = bytesToInt(in2b);
////                            Timber.i("蓝牙", "读取到UserId长度: " + in2b.length);
////                            Timber.i("蓝牙", "读取到UserId: " + btUserId);
//
//                            HttpConnectionUtil httpConnectionUtil = HttpConnectionUtil.i();
//                            IntelligenceVersion intelligenceVersion = httpConnectionUtil.getPrinterVersionFromServer(btGroupId + btUserId);
//                            if (null != intelligenceVersion && intelligenceVersion.getMsg() != null && intelligenceVersion.getMsg().equals("SUCCESS")) {
//                                Bundle bundle = new Bundle();
//                                bundle.putInt("btUserId", btUserId);
//                                bundle.putString("btGroupId", btGroupId);
//                                msg.setData(bundle);
//                                msg.what = MSG_CONN_YES;
//                            } else {
//                                msg.what = MSG_CONN_ERR;
//                            }
//                        }

//                        TODO  暂时关闭型号验证功能,使用测试数据
                        Bundle bundle = new Bundle();
                        bundle.putInt("btUserId", 1);
                        msg.setData(bundle);
                        msg.what = MSG_CONN_YES;
                        mBluetoothSocket.close();
                    } else {
                        msg.what = MSG_CONN_NO;
                    }
                } catch (IOException e) {
                    msg.what = MSG_CONN_NO;
                    e.printStackTrace();
                } finally {
                    connMsgHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler connMsgHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int btUserId = bundle.getInt("btUserId");
            String btGroupId = bundle.getString("btGroupId");
            switch (msg.what) {
                case MSG_CONN_YES:
                    dismissDlg();
                    //TODO  暂时关闭型号验证功能
//                    Toast.makeText(Activity_BluetoothPrinterList.this,
//                            "连接成功! \r\n打印机型号匹配！" + btGroupId + btUserId, Toast.LENGTH_SHORT).show();
                    final Intent settings = new Intent(Activity_BluetoothPrinterList.this, Activity_Settings.class);
                    settings.putExtra("ipAddress",
                            ipAddress);
                    settings.putExtra("macAddress",
                            macAddress);
                    settings.putExtra("localName", "");
                    settings.putExtra("printer", modelName);
                    settings.putExtra("btUserId", btUserId);
                    settings.putExtra("btGroupId", btGroupId);
                    setResult(RESULT_OK, settings);
                    finish();
                    break;
                case MSG_CONN_NO:
                    dismissDlg();
                    Toast.makeText(Activity_BluetoothPrinterList.this,
                            "连接不成功! \r\n检查蓝牙打印机是否开启！", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_CONN_ERR:
                    dismissDlg();
                    MdDialogUtils.getInstance().createAffirmDialog(Activity_BluetoothPrinterList.this, "连接不成功! ","打印设备未注册，请联系应用提供商！").show();
                    resBtUserId();
                    break;
                default:
            }
        }
    };

    private void resBtUserId() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("btUserId", -1);
        editor.putString("btGroupId", "");
        editor.putString("macAddress", "");
        editor.putString("address", "");
        editor.putString("printer", "");
        editor.putString("localName", "");
        editor.apply();
    }


    public static String ByteToString(byte[] bytes) {

        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != 0) {
                strBuilder.append((char) bytes[i]);
            } else {
                break;
            }

        }
        return strBuilder.toString();
    }

    public static byte[] InputToBytesUserId(InputStream is) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] buff = new byte[2];
        int rc = is.read(buff, 0, 2);
        stream.write(buff, 0, rc);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    public static byte[] InputToBytesGroupId(InputStream is) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] buff = new byte[4];
        int rc = is.read(buff, 0, 4);
        stream.write(buff, 0, rc);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    /**
     * @return 整型
     * @方法功能 字节数组和整型的转换
     */
    public static int bytesToInt(byte[] bytes) {
        int num = -1;
        if (bytes.length >= 2) {
            num = bytes[0] & 0xFF;
            num |= ((bytes[1] << 8) & 0xFF00);
        }
        return num;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mBluetoothSocket != null) {
                mBluetoothSocket.close();
            }

            bluetoothAdapter = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}