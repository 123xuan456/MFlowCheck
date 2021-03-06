/**
 * PdfPrint for printing
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */
package com.mtm.flowcheck.utils.printer.printprocess;

import android.content.Context;
import android.os.Build;

import com.brother.ptouch.sdk.PrinterInfo;
import com.mtm.flowcheck.utils.printer.common.MsgDialog;
import com.mtm.flowcheck.utils.printer.common.MsgHandle;


public class PdfPrint extends BasePrint {

    private int startIndex;
    private int endIndex;
    private String mPdfFile;

    public PdfPrint(Context context, MsgHandle mHandle, MsgDialog mDialog) {
        super(context, mHandle, mDialog);
    }

    /**
     * get print pdf pages
     */
    public int getPdfPages(String file) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return mPrinter.getPDFPages(file);
        } else {
            return mPrinter.getPDFFilePages(file);

        }
    }

    /**
     * set print pdf pages
     */
    public void setPrintPage(int start, int end) {

        startIndex = start;
        endIndex = end;
    }

    /**
     * set print data
     */
    public void setFiles(String file) {
        mPdfFile = file;

    }

    /**
     * do the particular print
     */
    @Override
    protected void doPrint() {
        try {
            for (int i = startIndex; i <= endIndex; i++) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    mPrintResult = mPrinter.printPDF(mPdfFile, i);
                } else {
                    mPrintResult = mPrinter.printPdfFile(mPdfFile, i);
                }
                if (mPrintResult.errorCode != PrinterInfo.ErrorCode.ERROR_NONE) {
                    break;
                }
            }
        } catch (Exception e) {

        }

    }

}
