package com.mtm.flowcheck.utils.printer;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.mtm.flowcheck.R;
import com.mtm.flowcheck.activity.BaseActivity;
import com.mtm.flowcheck.utils.FileUtils;
import com.mtm.flowcheck.utils.printer.common.MsgDialog;
import com.mtm.flowcheck.utils.printer.common.MsgHandle;
import com.mtm.flowcheck.utils.printer.js_pdf.WebViewHelper;
import com.mtm.flowcheck.utils.printer.printprocess.BasePrint;
import com.mtm.flowcheck.utils.printer.printprocess.PdfPrint;
import com.mtm.flowcheck.utils.printer.printprocess.PrinterModelInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 兄弟打印机浏览打印页面
 */
public class MicroBrotherPrinterActivity extends BaseActivity {

    Button btnPrinterSettings;
    Button btnPrint;
    private String filePath;
    BasePrint myPrint = null;
    MsgHandle mHandle;
    MsgDialog mDialog;
    private BluetoothAdapter bluetoothAdapter;
    private int btUserId;
    private String btGroupId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_micro_brother_printer;
    }

    @Override
    public void initView() {
        try {
            FileUtils.copyAssetsFile("", "bgm.pdf", FileUtils.recordPath(mContext)+"bgm.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnPrinterSettings = (Button) findViewById(R.id.btnPrinterSettings);
        btnPrint = (Button) findViewById(R.id.btnPrint);
        btnPrinterSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MicroBrotherPrinterActivity.this, Activity_Settings.class));
            }
        });
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBlueAddress();
                /**
                 * 如果不符合打印机型号，不允许打印
                 */
                if (btUserId == -1){
                    Toast.makeText(MicroBrotherPrinterActivity.this,
                            "打印不成功! \r\n请检查蓝牙打印机！", Toast.LENGTH_SHORT).show();
                }else {
                    printButtonOnClick();
                }
            }
        });
        // 初始化
        mDialog = new MsgDialog(MicroBrotherPrinterActivity.this);
        mHandle = new MsgHandle(MicroBrotherPrinterActivity.this, mDialog);
        myPrint = new PdfPrint(MicroBrotherPrinterActivity.this, mHandle, mDialog);
    }

    @SuppressLint("NewApi")
    public void initData() {
        setPreferences();
        //filePath = getIntent().getStringExtra("filePath");
//        filePath = "file:///android_asset/bgmb.docx";
        filePath = FileUtils.recordPath(mContext)+"bgm.pdf";
//        filePath = "file:///android_asset/abc.pdf";
        WebView webView = (WebView) findViewById(R.id.webView);
        WebViewHelper.WebViewSetting(webView);
        WebViewHelper.WebViewLoadPDF(webView, filePath);

        // 打印时通过蓝牙方式设置适配器
        bluetoothAdapter = getBluetoothAdapter();

        getBlueAddress();
        myPrint.setBluetoothAdapter(bluetoothAdapter);
        ((PdfPrint) myPrint).setFiles(filePath);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getBlueAddress();
    }

    /**
     * 初始化数据
     */
    private void setPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        // initialization for print
        Printer printer = new Printer();
        PrinterInfo printerInfo = printer.getPrinterInfo();
        if (printerInfo == null) {
            printerInfo = new PrinterInfo();
            printer.setPrinterInfo(printerInfo);
        }
        //添加默认值
        printerInfo.halftone = PrinterInfo.Halftone.THRESHOLD;//
        printerInfo.printQuality = PrinterInfo.PrintQuality.HIGH_RESOLUTION;//高质量
//        printerInfo.pjFeedMode = PrinterInfo.PjFeedMode.PJ_FEED_MODE_ENDOFPAGE;//纸张模式（页尾）
        printerInfo.pjFeedMode = PrinterInfo.PjFeedMode.PJ_FEED_MODE_ENDOFPAGERETRACT;//纸张模式（页尾收回）
        printerInfo.pjPaperKind = PrinterInfo.PjPaperKind.PJ_ROLL;//纸张类型（卷筒纸）
        printerInfo.rollPrinterCase = PrinterInfo.PjRollCase.PJ_ROLLCASE_ON;//打印机外壳
        printerInfo.pjDensity = 10;//打印浓度
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getString("printerModel", "").equals("")) {
            String printerModel = printerInfo.printerModel.toString();
            PrinterModelInfo.Model model = PrinterModelInfo.Model.valueOf(printerModel);
            editor.putString("printerModel", printerModel);
            editor.putString("port", printerInfo.port.toString());
            editor.putString("address", printerInfo.ipAddress);
            editor.putString("macAddress", printerInfo.macAddress);
            editor.putString("localName", printerInfo.getLocalName());

            // Override SDK default paper size
            editor.putString("paperSize", model.getDefaultPaperSize());

            editor.putString("orientation", printerInfo.orientation.toString());
            editor.putString("numberOfCopies",
                    Integer.toString(printerInfo.numberOfCopies));
            editor.putString("halftone", printerInfo.halftone.toString());
            editor.putString("printMode", printerInfo.printMode.toString());
            editor.putString("pjCarbon", Boolean.toString(printerInfo.pjCarbon));
            editor.putString("pjDensity",
                    Integer.toString(printerInfo.pjDensity));
            editor.putString("pjFeedMode", printerInfo.pjFeedMode.toString());
            editor.putString("align", printerInfo.align.toString());
            editor.putString("leftMargin",
                    Integer.toString(printerInfo.margin.left));
            editor.putString("valign", printerInfo.valign.toString());
            editor.putString("topMargin",
                    Integer.toString(printerInfo.margin.top));
            editor.putString("customPaperWidth",
                    Integer.toString(printerInfo.customPaperWidth));
            editor.putString("customPaperLength",
                    Integer.toString(printerInfo.customPaperLength));
            editor.putString("customFeed",
                    Integer.toString(printerInfo.customFeed));
            editor.putString("paperPosition",
                    printerInfo.paperPosition.toString());
            editor.putString("customSetting",
                    sharedPreferences.getString("customSetting", ""));
            editor.putString("rjDensity",
                    Integer.toString(printerInfo.rjDensity));
            editor.putString("rotate180",
                    Boolean.toString(printerInfo.rotate180));
            editor.putString("dashLine", Boolean.toString(printerInfo.dashLine));

            editor.putString("peelMode", Boolean.toString(printerInfo.peelMode));
            editor.putString("mode9", Boolean.toString(printerInfo.mode9));
            editor.putString("pjSpeed", Integer.toString(printerInfo.pjSpeed));
            editor.putString("pjPaperKind", printerInfo.pjPaperKind.toString());
            editor.putString("printerCase",
                    printerInfo.rollPrinterCase.toString());
            editor.putString("printQuality", printerInfo.printQuality.toString());
            editor.putString("skipStatusCheck",
                    Boolean.toString(printerInfo.skipStatusCheck));
            editor.putString("checkPrintEnd", printerInfo.checkPrintEnd.toString());
            editor.putString("imageThresholding",
                    Integer.toString(printerInfo.thresholdingValue));
            editor.putString("scaleValue",
                    Double.toString(printerInfo.scaleValue));
            editor.putString("trimTapeAfterData",
                    Boolean.toString(printerInfo.trimTapeAfterData));
            editor.putString("enabledTethering",
                    Boolean.toString(printerInfo.enabledTethering));


            editor.putString("processTimeout",
                    Integer.toString(printerInfo.timeout.processTimeoutSec));
            editor.putString("sendTimeout",
                    Integer.toString(printerInfo.timeout.sendTimeoutSec));
            editor.putString("receiveTimeout",
                    Integer.toString(printerInfo.timeout.receiveTimeoutSec));
            editor.putString("connectionTimeout",
                    Integer.toString(printerInfo.timeout.connectionWaitMSec));
            editor.putString("closeWaitTime",
                    Integer.toString(printerInfo.timeout.closeWaitDisusingStatusCheckSec));

            editor.putString("overwrite",
                    Boolean.toString(printerInfo.overwrite));
            editor.putString("savePrnPath", printerInfo.savePrnPath);
            editor.putString("softFocusing",
                    Boolean.toString(printerInfo.softFocusing));
            editor.putString("rawMode",
                    Boolean.toString(printerInfo.rawMode));
            editor.putString("workPath", printerInfo.workPath);
            editor.putString("useLegacyHalftoneEngine",
                    Boolean.toString(printerInfo.useLegacyHalftoneEngine));

        } else {
            editor.putString("halftone", printerInfo.halftone.toString());
            editor.putString("printQuality", printerInfo.printQuality.toString());
            editor.putString("pjDensity", Integer.toString(printerInfo.pjDensity));
            editor.putString("pjFeedMode", printerInfo.pjFeedMode.toString());
            editor.putString("pjPaperKind", printerInfo.pjPaperKind.toString());
            editor.putString("printerCase", printerInfo.rollPrinterCase.toString());
        }
        editor.apply();
    }

    BluetoothAdapter getBluetoothAdapter() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            final Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(enableBtIntent);
        }
        return bluetoothAdapter;
    }

    /**
     * Called when [Print] button is tapped
     */
    public void printButtonOnClick() {
        int startPage;
        int endPage;
        // All pages
        startPage = 1;
        endPage = ((PdfPrint) myPrint).getPdfPages(filePath);
        // error if startPage > endPage
        if (startPage > endPage) {
            mDialog.showAlertDialog(getString(R.string.msg_title_warning),
                    getString(R.string.error_input));
            return;
        }

        // call function to print
        ((PdfPrint) myPrint).setPrintPage(startPage, endPage);
        myPrint.print();

    }

    public void getBlueAddress() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        final String macAddress = sharedPreferences.getString("macAddress", "");
        String address = sharedPreferences.getString("address", "");
        String printer = sharedPreferences.getString("printer", "");
        btUserId = sharedPreferences.getInt("btUserId", -1);
        btGroupId = sharedPreferences.getString("btGroupId", "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
