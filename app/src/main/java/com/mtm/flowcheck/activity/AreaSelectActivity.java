package com.mtm.flowcheck.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.mtm.flowcheck.R;
import com.mtm.flowcheck.bean.dao.OrganBean;
import com.mtm.flowcheck.bean.dao.RegionBean;
import com.mtm.flowcheck.dao.DBHelper;
import com.mtm.flowcheck.manager.BaseApplication;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jxl.Sheet;
import jxl.Workbook;

public class AreaSelectActivity extends BaseActivity {

    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.all_title)
    TextView allTitle;
    @BindView(R.id.area_tv_country)
    TextView areaTvCountry;
    @BindView(R.id.area_ll_country)
    LinearLayout areaLlCountry;
    @BindView(R.id.area_tv_province)
    TextView areaTvProvince;
    @BindView(R.id.area_ll_province)
    LinearLayout areaLlProvince;
    @BindView(R.id.area_tv_city)
    TextView areaTvCity;
    @BindView(R.id.area_ll_city)
    LinearLayout areaLlCity;
    @BindView(R.id.area_tv_district)
    TextView areaTvDistrict;
    @BindView(R.id.area_ll_district)
    LinearLayout areaLlDistrict;
    @BindView(R.id.area_tv_street)
    TextView areaTvStreet;
    @BindView(R.id.area_ll_street)
    LinearLayout areaLlStreet;
    @BindView(R.id.area_tv_organ)
    TextView areaTvHispital;
    @BindView(R.id.area_ll_organ)
    LinearLayout areaLlOrgan;
    private String TAG = "选中条目";
    private String pid2, pid3, pid4, pid5, pid6;

    @Override
    public int getLayoutId() {
        return R.layout.activity_area_select;
    }

    @Override
    public void initView() {

    }


    @Override
    public void initData() {
        allTitle.setText("区域选择");
        Bundle extras = getIntent().getExtras();
        String linkCode = extras.getString("linkCode");
        String position = extras.getString("position");
        String fieldName = extras.getString("fieldName");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("linkStrId", linkStrId);
//                intent.putExtra("position", position);
//                intent.putExtra("titleStrId", titleStrId);
//                //设置返回数据
//                setResult(RESULT_OK, intent);
                finish();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                DBHelper.getInstance(AreaSelectActivity.this).insertRegion(readAreaExcel());
                DBHelper.getInstance(AreaSelectActivity.this).insertOrgan(readOrganExcel());
            }
        }).start();

        showAreaBtn(fieldName);
    }

    //    areaLlCountry.setVisibility(View.VISIBLE);
//         areaLlProvince.setVisibility(View.VISIBLE);
//         areaLlCity.setVisibility(View.VISIBLE);
//         areaLlDistrict.setVisibility(View.VISIBLE);
//         areaLlStreet.setVisibility(View.VISIBLE);
//         areaLlOrgan.setVisibility(View.VISIBLE);
    private void showAreaBtn(String status) {
        switch (status) {
            case "regionCase":
                areaLlDistrict.setVisibility(View.VISIBLE);
                break;
            case "addrCode":
                areaLlProvince.setVisibility(View.VISIBLE);
                areaLlCity.setVisibility(View.VISIBLE);
                areaLlDistrict.setVisibility(View.VISIBLE);
                areaLlStreet.setVisibility(View.VISIBLE);
                break;
            case "reportHospital":
            case "intoHospital":
                areaLlDistrict.setVisibility(View.VISIBLE);
                areaLlOrgan.setVisibility(View.VISIBLE);
                break;
            case "infectCity":
                areaLlProvince.setVisibility(View.VISIBLE);
                areaLlCity.setVisibility(View.VISIBLE);
                break;
        }
    }


    @OnClick({R.id.area_ll_country, R.id.area_ll_province, R.id.area_ll_city, R.id.area_ll_district, R.id.area_ll_street, R.id.area_ll_organ})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.area_ll_country:
                queryData(1, "", areaTvCountry);//国家
                break;
            case R.id.area_ll_province:
                queryData(2, pid2, areaTvProvince);//省
                break;
            case R.id.area_ll_city:
                queryData(3, pid3, areaTvCity);//市
                break;
            case R.id.area_ll_district:
                queryData(4, pid4, areaTvDistrict);//区
                break;
            case R.id.area_ll_street:
                queryData(5, pid5, areaTvStreet);//街道
                break;
            case R.id.area_ll_organ:
                queryOrganData(6, pid6, areaTvHispital);//医院
                break;
            default:
                break;
        }
    }

    //查询区域
    private void queryData(int position, String pid, TextView textView) {
        List<RegionBean> list = DBHelper.getInstance(this).queryRegionByPid(pid);
        switch (position) {
            case 1:
                pid2 = pid;
                break;
            case 2:
                pid3 = pid;
                break;
            case 3:
                pid4 = pid;
                break;
            case 4:
                pid5 = pid;
                break;
            case 5:
                pid6 = pid;
                break;
        }
        String[] code = new String[list.size()];
        String[] ares = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            code[i] = list.get(i).getID();
            ares[i] = list.get(i).getAREA_NAM();
        }
        showAlertView(position, textView, code, ares);
    }

    //查询机构
    private void queryOrganData(int position, String pid, TextView textView) {
        List<OrganBean> list = DBHelper.getInstance(this).queryOrganByPid(pid);
        if (position == 6) {
            pid6 = pid;
        }
        String[] code = new String[list.size()];
        String[] ares = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            code[i] = list.get(i).getID();
            ares[i] = list.get(i).getORG_NAME();
        }
        showAlertView(position, textView, code, ares);
    }

    //展示数据条目
    private void showAlertView(int position, TextView textView, String[] code, String[] arr) {
        new AlertView(null, null, "取消", null, arr,
                AreaSelectActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int positions) {
                if (positions != -1) {
                    textView.setText(arr[positions]);
                    switch (position) {
                        case 1:
                            pid2 = String.valueOf(code[positions]);
                            break;
                        case 2:
                            pid3 = String.valueOf(code[positions]);
                            break;
                        case 3:
                            pid4 = String.valueOf(code[positions]);
                            break;
                        case 4:
                            pid5 = String.valueOf(code[positions]);
                            break;
                        case 5:
                            pid6 = String.valueOf(code[positions]);
                            break;
                    }
                }
            }
        }).show();
    }

    /**
     * 读取地区Excel数据
     *
     * @return List<Country>
     */
    private static List<RegionBean> readAreaExcel() {
        List<RegionBean> countryList = new ArrayList<>();
        try {
            InputStream is = BaseApplication.getApplication().getAssets().open("surveymanage.xls");
            Workbook book = Workbook.getWorkbook(is);
            book.getNumberOfSheets();
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();

            for (int i = 1; i < Rows; ++i) {
                //将每一列的数据读取
                String id = (sheet.getCell(0, i)).getContents();
                String pid = (sheet.getCell(1, i)).getContents();
                String areaCode = (sheet.getCell(2, i)).getContents();
                String areaNam = (sheet.getCell(3, i)).getContents();
                String areaName = (sheet.getCell(4, i)).getContents();
                countryList.add(new RegionBean(id, pid, areaCode, areaNam, areaName));
            }
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countryList;
    }

    /**
     * 读取机构数据
     *
     * @return List<OrganBean>
     */
    private static List<OrganBean> readOrganExcel() {
        List<OrganBean> organList = new ArrayList<>();
        try {
            InputStream is = BaseApplication.getApplication().getAssets().open("surveymanage.xls");
            Workbook book = Workbook.getWorkbook(is);
            book.getNumberOfSheets();
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(1);
            int Rows = sheet.getRows();
            for (int i = 1; i < Rows; ++i) {
                //将每一列的数据读取
                String id = (sheet.getCell(1, i)).getContents();
                String orgName = (sheet.getCell(2, i)).getContents();
                String orgCode = (sheet.getCell(3, i)).getContents();
                String pid = (sheet.getCell(4, i)).getContents();
                organList.add(new OrganBean(id, orgName, orgCode, pid));
            }
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return organList;
    }

}
