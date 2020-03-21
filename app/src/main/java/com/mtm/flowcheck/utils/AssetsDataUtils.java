package com.mtm.flowcheck.utils;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.crypto.MacSpi;

/**
 * Created By WangYanBin On 2020\03\20 17:07.
 * <p>
 * （AssetsDataUtils）
 * 参考：
 * 描述：
 */
public class AssetsDataUtils {

    public static void init(Context context) {
        new Thread(() -> {
            updateRegionData(context);
            updatOrganData(context);
        }).start();
    }

    private static void updateRegionData(Context context) {
        try {
            InputStream mStream = context.getAssets().open("json/region.json");
            String json = convertStraemToString(mStream);
            Log.e("AssetsDataUtils", json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updatOrganData(Context context) {
        try {
            InputStream mStream = context.getAssets().open("json/organ.json");
            String json = convertStraemToString(mStream);
            Log.e("AssetsDataUtils", json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String convertStraemToString(InputStream mStream) {
        BufferedReader mReader = new BufferedReader(new InputStreamReader(mStream));
        StringBuilder mBuilder = new StringBuilder();
        try {
            String line;
            while ((line = mReader.readLine()) != null) {
                mBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mStream.close();
                mReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mBuilder.toString();
    }
}
