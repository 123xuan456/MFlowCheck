package com.mtm.flowcheck.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IOUtils {
    /**
     * 关闭流
     */
    public static boolean close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }
        return true;
    }

    /**
     * 把对象序列化到本地
     *
     * @param fileName 序列化的文件名称
     * @param soObj    需要序列化的 对象
     */
    public static void saveObject(final String fileName, final Object soObj) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                FileOutputStream fos = null;
                ObjectOutputStream oos = null;
                try {
                    File file = new File(FileUtils.getCacheDir() + fileName);
                    if (file.exists()) {
                        file.mkdirs();
                    }
                    FileUtils.chmod(FileUtils.getCacheDir() + fileName, "777");
                    fos = new FileOutputStream(file);
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(soObj);
                } catch (Exception e) {
                    e.printStackTrace();
                    //这里是保存文件产生异常
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            //fos流关闭异常
                            e.printStackTrace();
                        }
                    }
                    if (oos != null) {
                        try {
                            oos.close();
                        } catch (IOException e) {
                            //oos流关闭异常
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }

    /**
     * 得到反序列化的对象
     *
     * @param fileName 反序列化的文件名称
     * @return Object 反序列化 对象
     */
    public static Object getObject(final String fileName) {
        final Object[] obj = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileInputStream fis = null;
                ObjectInputStream ois = null;
                try {
                    fis = new FileInputStream(FileUtils.getCacheDir() + fileName);
                    ois = new ObjectInputStream(fis);
                    obj[0] = ois.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                    //这里是读取文件产生异常
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            //fis流关闭异常
                            e.printStackTrace();
                        }
                    }
                    if (ois != null) {
                        try {
                            ois.close();
                        } catch (IOException e) {
                            //ois流关闭异常
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        //读取产生异常，返回null
        return obj[0];
    }

}
