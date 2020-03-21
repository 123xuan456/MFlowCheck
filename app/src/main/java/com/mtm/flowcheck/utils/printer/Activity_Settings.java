package com.mtm.flowcheck.utils.printer; /**
 * Activity of printer settings
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.mtm.flowcheck.R;
import com.mtm.flowcheck.utils.printer.common.Common;
import com.mtm.flowcheck.utils.printer.printprocess.PrinterModelInfo;

import java.io.File;
import java.util.Arrays;

public class Activity_Settings extends PreferenceActivity implements
        OnPreferenceChangeListener {

    private SharedPreferences sharedPreferences;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // initialize the printerModel ListPreference
        ListPreference printerModelPreference = (ListPreference) getPreferenceScreen()
                .findPreference("printerModel");
        printerModelPreference.setEntryValues(PrinterModelInfo.getModelNames());
        printerModelPreference.setEntries(PrinterModelInfo.getModelNames());
        // initialize the settings
        setPreferenceValue("printerModel");
        String printerModel = sharedPreferences.getString("printerModel", "");

        // set paper size & port information
        printerModelChange(printerModel);

        setPreferenceValue("paperSize");
        setEditValue("numberOfCopies");

        // initialize the custom paper size's settings
        File newdir = new File(Common.CUSTOM_PAPER_FOLDER);
        if (!newdir.exists()) {
            newdir.mkdir();
        }
        File[] files = new File(Common.CUSTOM_PAPER_FOLDER).listFiles();
        String[] entries = new String[files.length];
        String[] entryValues = new String[files.length];
        int i = 0;
        for (File file : files) {
            String filename = file.getName();
            String extention = filename.substring(
                    filename.lastIndexOf(".", filename.length()) + 1,
                    filename.length());
            if (extention.equalsIgnoreCase("bin")) {
                entries[i] = filename;
                entryValues[i] = filename;
                i++;
            }
        }
        Arrays.sort(entries);
        Arrays.sort(entryValues);


        // initialization for printer
        PreferenceScreen printerPreference = (PreferenceScreen) getPreferenceScreen()
                .findPreference("printer");

        String printer = sharedPreferences.getString("printer", "");
        if (!printer.equals("")) {
            printerPreference.setSummary(printer);
        }


        printerPreference
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        String printerModel = sharedPreferences.getString(
                                "printerModel", "");
                        setPrinterList(printerModel);
                        return true;
                    }
                });
/**
 * 下面的蓝牙设置用不到
 */
        getPreferenceScreen().removePreference(findPreference("otherSettings"));//隐藏其他设置选项

    }


    /**
     * Called when a Preference has been changed by the user. This is called
     * before the state of the Preference is about to be updated and before the
     * state is persisted.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (newValue != null) {
            if (preference.getKey().equals("printerModel")) {
                String printerModel = sharedPreferences.getString(
                        "printerModel", "");
                if (printerModel.equalsIgnoreCase(newValue.toString())) {
                    return true;
                }

                // initialize if printer model is changed
                printerModelChange(newValue.toString());
                ListPreference paperSizePreference = (ListPreference) getPreferenceScreen()
                        .findPreference("paperSize");
                paperSizePreference.setValue(paperSizePreference
                        .getEntryValues()[0].toString());
                paperSizePreference.setSummary(paperSizePreference
                        .getEntryValues()[0].toString());

            }


            preference.setSummary((CharSequence) newValue);

            return true;
        }

        return false;

    }

    /**
     * Called when the searching printers activity you launched exits.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Common.PRINTER_SEARCH == requestCode) {
//            EditTextPreference addressPreference = (EditTextPreference) getPreferenceScreen()
//                    .findPreference("address");
//            EditTextPreference macAddressPreference = (EditTextPreference) getPreferenceScreen()
//                    .findPreference("macAddress");
            PreferenceScreen printerPreference = (PreferenceScreen) getPreferenceScreen()
                    .findPreference("printer");
//
            if (resultCode == RESULT_OK) {
                printerPreference.setSummary(data.getStringExtra("printer"));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("macAddress", data.getStringExtra("macAddress"));
                editor.putString("address", data.getStringExtra("ipAddress"));
                editor.putString("printer", data.getStringExtra("printer"));
                editor.putString("localName", data.getStringExtra("localName"));
                editor.putInt("btUserId", data.getIntExtra("btUserId",-1));
                editor.putString("btGroupId", data.getStringExtra("btGroupId"));
                editor.apply();
            }
        }
    }

    /**
     * set data of a particular ListPreference
     */
    private void setPreferenceValue(String value) {
        String data = sharedPreferences.getString(value, "");

        ListPreference printerValuePreference = (ListPreference) getPreferenceScreen()
                .findPreference(value);
        printerValuePreference.setOnPreferenceChangeListener(this);
        if (!data.equals("")) {
            printerValuePreference.setSummary(data);
        }
    }

    /**
     * set data of a particular EditTextPreference
     */
    private void setEditValue(String value) {
        String name = sharedPreferences.getString(value, "");
        EditTextPreference printerValuePreference = (EditTextPreference) getPreferenceScreen()
                .findPreference(value);
        printerValuePreference.setOnPreferenceChangeListener(this);

        if (!name.equals("")) {
            printerValuePreference.setSummary(name);
        }
    }

    /**
     * Called when [printer] is tapped
     */
    private void setPrinterList(String printModel) {
            Intent printerList = new Intent(this,
                    Activity_BluetoothPrinterList.class);
            startActivityForResult(printerList, Common.PRINTER_SEARCH);
    }


    /**
     * set paper size & port information with changing printer model
     */
    private void printerModelChange(String printerModel) {

        // paper size
        ListPreference paperSizePreference = (ListPreference) getPreferenceScreen()
                .findPreference("paperSize");
        // port
//        ListPreference portPreference = (ListPreference) getPreferenceScreen()
//                .findPreference("port");
        if (!printerModel.equals("")) {

            String[] entryPort;
            String[] entryPaperSize;
            entryPort = PrinterModelInfo.getPortOrPaperSizeInfo(printerModel, Common.SETTINGS_PORT);
            entryPaperSize = PrinterModelInfo.getPortOrPaperSizeInfo(printerModel, Common.SETTINGS_PAPERSIZE);


            paperSizePreference.setEntryValues(entryPaperSize);
            paperSizePreference.setEntries(entryPaperSize);

        }
    }

}
