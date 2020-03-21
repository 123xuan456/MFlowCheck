package com.mtm.flowcheck.service;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

/**
 * Created By WangYanBin On 2020\03\17 13:06.
 * <p>
 * （NFCService）
 * 参考：
 * 描述：
 */
public class NFCService extends HostApduService {

    public final static String COMMANDS_NO = "";// 无操作
    public final static String COMMANDS_START_RECORD_VIDEO = "I0401";// 开始录制视频
    public final static String COMMANDS_STOP_RECORD_VIDEO = "I0402";// 结束录制视频
    public final static String COMMANDS_TAKE_PICTURE = "I0403";// 拍摄照片
    public final static String COMMANDS_START_RECORD_AUDIO = "I0404";// 开始录制音频
    public final static String COMMANDS_STOP_RECORD_AUDIO = "I0405";// 结束录制音频

    private static String nowRecordId;
    private static String nowCommands = COMMANDS_NO;

    /**
     * 取消命令
     */
    public static void cancelCommands() {
        nowRecordId = "";
        nowCommands = COMMANDS_NO;
    }

    /**
     * 开始录制视频
     *
     * @param recordId
     */
    public static void startRecordVideo(String recordId) {
        nowRecordId = recordId;
        nowCommands = COMMANDS_START_RECORD_VIDEO;
    }
    public static void stopRecordVideo() {
        nowRecordId = "";
        nowCommands = COMMANDS_STOP_RECORD_VIDEO;
    }
    /**
     * 拍摄照片
     *
     * @param recordId
     */
    public static void takePicture(String recordId) {
        nowRecordId = recordId;
        nowCommands = COMMANDS_TAKE_PICTURE;
    }

    /**
     * 开始录制音频
     *
     * @param recordId
     */
    public static void startRecordAudio(String recordId) {
        nowRecordId = recordId;
        nowCommands = COMMANDS_START_RECORD_AUDIO;
    }

    // -------------------------------------------------- 执行 --------------------------------------------------

    private final byte[] SELECT_OK_SW = hexStringToByteArray("9000");
    private final byte[] UNKNOWN_CMD_SW = hexStringToByteArray("0000");
    private final String CARD_AID = "F222222222";
    private final String SELECT_CARD_APDU_HEADER = "00A40400";
    private final byte[] SELECT_CARD_APDU = buildSelectCardAPDU(CARD_AID);
    private final String GET_DATA_APDU_HEADER = "00CA0000";
    private final byte[] GET_DATA_APDU = buildGetDataAPDU();

    private int pointer;

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        Log.e("TAG", "processCommandApdu：进入" + nowCommands + nowRecordId);
        if (Arrays.equals(SELECT_CARD_APDU, commandApdu)) {
            Log.e("TAG", "processCommandApdu：选择卡" + nowCommands + nowRecordId);
            byte[] orderBytes = (nowCommands + nowRecordId).getBytes();
            return concatByteArray(orderBytes, SELECT_OK_SW);
        } else if ((Arrays.equals(GET_DATA_APDU, commandApdu))) {
            Log.e("TAG", "processCommandApdu：获取数据" + nowCommands + nowRecordId);
            byte[] orderBytes;
            if (pointer == 0) {
                orderBytes = (nowCommands + nowRecordId).getBytes();
                pointer = 1;
                Log.e("TAG", "processCommandApdu：命令" + nowCommands + nowRecordId);
            } else {
                orderBytes = "END".getBytes();
                pointer = 0;
                Log.e("TAG", "processCommandApdu：结束" + nowCommands + nowRecordId);
                EventBus.getDefault().post(nowCommands);
//                refreshCommands();
                Log.e("TAG", "processCommandApdu：刷新" + nowCommands + nowRecordId);
            }
            return concatByteArray(orderBytes, SELECT_OK_SW);
        } else {
            Log.e("TAG", "processCommandApdu：未知" + nowCommands + nowRecordId);
            return UNKNOWN_CMD_SW;
        }
    }

    private void refreshCommands() {
        switch (nowCommands) {
            case COMMANDS_START_RECORD_VIDEO:// 开始录制视频
                nowRecordId = "";
                nowCommands = COMMANDS_STOP_RECORD_VIDEO;
                break;
            case COMMANDS_START_RECORD_AUDIO:// 开始录制音频
                nowRecordId = "";
                nowCommands = COMMANDS_STOP_RECORD_AUDIO;
                break;
            case COMMANDS_STOP_RECORD_VIDEO:// 结束录制视频
            case COMMANDS_STOP_RECORD_AUDIO:// 结束录制音频
//                nowRecordId = "";
//                nowCommands = COMMANDS_NO;
                break;
        }
    }

    @Override
    public void onDeactivated(int reason) {

    }

    // -------------------------------------------------- 其他 --------------------------------------------------

    /**
     * 建立选择卡APDU
     *
     * @param aid
     * @return
     */
    public byte[] buildSelectCardAPDU(String aid) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return hexStringToByteArray(SELECT_CARD_APDU_HEADER + String.format("%02X", aid.length() / 2) + aid);
    }

    /**
     * 建立获取数据APDU
     *
     * @return
     */
    private byte[] buildGetDataAPDU() {
        return hexStringToByteArray(GET_DATA_APDU_HEADER + "0FFF");
    }

    /**
     * 16进制字符串转字节数组
     *
     * @param str
     * @return
     * @throws IllegalArgumentException
     */
    private byte[] hexStringToByteArray(String str) throws IllegalArgumentException {
        int len = str.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException("hex string must have even number of characters");
        }
        byte[] data = new byte[len / 2]; // Allocate 1 byte per 2 hex characters
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 合并字节数组
     *
     * @param first
     * @param rest
     * @return
     */
    public static byte[] concatByteArray(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}
