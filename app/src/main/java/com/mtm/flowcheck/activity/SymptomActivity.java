package com.mtm.flowcheck.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mtm.flowcheck.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ProjectName: MFlowCheck
 * @Package: com.mtm.flowcheck.activity
 * @ClassName: SymptomActivity
 * @Description: java类作用描述 症状和体征
 */
public class SymptomActivity extends BaseActivity {

    @BindView(R.id.symptom_rb_yes)
    RadioButton symptomRbYes;
    @BindView(R.id.symptom_rb_no)
    RadioButton symptomRbNo;
    @BindView(R.id.symptom_rg)
    RadioGroup symptomRg;
    @BindView(R.id.symptom_et)
    EditText symptomEt;
    @BindView(R.id.symptom_cbox_chill)
    CheckBox symptomCboxChill;
    @BindView(R.id.symptom_cbox_hoose)
    CheckBox symptomCboxHoose;
    @BindView(R.id.symptom_cbox_cough)
    CheckBox symptomCboxCough;
    @BindView(R.id.symptom_cbox_rhinobyon)
    CheckBox symptomCboxRhinobyon;
    @BindView(R.id.symptom_cbox_running_nose)
    CheckBox symptomCboxRunningNose;
    @BindView(R.id.symptom_cbox_running_pharyngalgia)
    CheckBox symptomCboxRunningPharyngalgia;
    @BindView(R.id.symptom_cbox_headache)
    CheckBox symptomCboxHeadache;
    @BindView(R.id.symptom_cbox_weak)
    CheckBox symptomCboxWeak;
    @BindView(R.id.symptom_cbox_sore_muscle)
    CheckBox symptomCboxSoreMuscle;
    @BindView(R.id.symptom_cbox_joint_sore)
    CheckBox symptomCboxJointSore;
    @BindView(R.id.symptom_cbox_anhelation)
    CheckBox symptomCboxAnhelation;
    @BindView(R.id.symptom_cbox_dyspnea)
    CheckBox symptomCboxDyspnea;
    @BindView(R.id.symptom_cbox_chest_distress)
    CheckBox symptomCboxChestDistress;
    @BindView(R.id.symptom_cbox_stethalgia)
    CheckBox symptomCboxStethalgia;
    @BindView(R.id.symptom_cbox_conjunctiva)
    CheckBox symptomCboxConjunctiva;
    @BindView(R.id.symptom_cbox_sick)
    CheckBox symptomCboxSick;
    @BindView(R.id.symptom_cbox_emesis)
    CheckBox symptomCboxEmesis;
    @BindView(R.id.symptom_cbox_diarrhea)
    CheckBox symptomCboxDiarrhea;
    @BindView(R.id.symptom_cbox_stomachache)
    CheckBox symptomCboxStomachache;
    @BindView(R.id.symptom_tv_content)
    TextView symptomTvContent;
    @BindView(R.id.symptom_et_other)
    EditText symptomEtOther;
    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.all_title)
    TextView allTitle;
    private StringBuilder sb = new StringBuilder();
    String rbSignleData=null;
    private int linkStrId;
    private int position;
    private int titleStrId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_symptom;
    }

    @Override
    public void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sb.toString().isEmpty()) {
                    Intent intent = new Intent();
                    intent.putExtra("linkStrId", linkStrId);
                    intent.putExtra("position", position);
                    intent.putExtra("titleStrId", titleStrId);
                    intent.putExtra("fever", rbSignleData);//发热
                    intent.putExtra("heat", symptomEt.getText().toString());//体温
                    intent.putExtra("symptom", sb.toString());//症状
                    intent.putExtra("other", symptomEtOther.getText().toString());//其他
                    //设置返回数据
                    setResult(RESULT_OK, intent);
                }
                //关闭Activity
                finish();
            }
        });
    }

    @Override
    public void initData() {
        linkStrId = getIntent().getIntExtra("linkStrId", 0);
        position = getIntent().getIntExtra("position", 0);
        titleStrId = getIntent().getIntExtra("titleStrId", 0);
        allTitle.setText("症状和体征");
        symptomRbYes.setChecked(false);
        symptomRbNo.setChecked(false);
        //发热
        symptomRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.symptom_rb_yes:
                        symptomRbYes.setChecked(true);
                        symptomRbNo.setChecked(false);
                        rbSignleData = symptomRbYes.getText().toString();
                        break;
                    case R.id.symptom_rb_no:
                        symptomRbYes.setChecked(false);
                        symptomRbNo.setChecked(true);
                        rbSignleData = symptomRbNo.getText().toString();
                        break;
                }
            }
        });
    }


    @OnClick({R.id.symptom_cbox_chill, R.id.symptom_cbox_hoose, R.id.symptom_cbox_cough, R.id.symptom_cbox_rhinobyon,
            R.id.symptom_cbox_running_nose, R.id.symptom_cbox_running_pharyngalgia, R.id.symptom_cbox_headache, R.id.symptom_cbox_weak,
            R.id.symptom_cbox_sore_muscle, R.id.symptom_cbox_joint_sore, R.id.symptom_cbox_anhelation, R.id.symptom_cbox_dyspnea,
            R.id.symptom_cbox_chest_distress, R.id.symptom_cbox_stethalgia, R.id.symptom_cbox_conjunctiva, R.id.symptom_cbox_sick,
            R.id.symptom_cbox_emesis, R.id.symptom_cbox_diarrhea, R.id.symptom_cbox_stomachache, R.id.symptom_tv_content})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.symptom_cbox_chill://寒战
                String chill = symptomCboxChill.getText().toString();
                addData(chill);
                break;
            case R.id.symptom_cbox_hoose://干咳
                String hoose = symptomCboxHoose.getText().toString();
                addData(hoose);
                break;
            case R.id.symptom_cbox_cough://咳痰
                String cough = symptomCboxCough.getText().toString();
                addData(cough);
                break;
            case R.id.symptom_cbox_rhinobyon://鼻塞
                String rhinobyon = symptomCboxRhinobyon.getText().toString();
                addData(rhinobyon);
                break;
            case R.id.symptom_cbox_running_nose://流涕
                String nose = symptomCboxRunningNose.getText().toString();
                addData(nose);
                break;
            case R.id.symptom_cbox_running_pharyngalgia://咽痛
                String pharyngalgia = symptomCboxRunningPharyngalgia.getText().toString();
                addData(pharyngalgia);
                break;
            case R.id.symptom_cbox_headache://头痛
                String headache = symptomCboxHeadache.getText().toString();
                addData(headache);
                break;
            case R.id.symptom_cbox_weak://乏力
                String weak = symptomCboxWeak.getText().toString();
                addData(weak);
                break;
            case R.id.symptom_cbox_sore_muscle://肌肉酸痛
                String muscle = symptomCboxSoreMuscle.getText().toString();
                addData(muscle);
                break;
            case R.id.symptom_cbox_joint_sore://关节酸痛
                String sore = symptomCboxJointSore.getText().toString();
                addData(sore);
                break;
            case R.id.symptom_cbox_anhelation://气促
                String anhelation = symptomCboxAnhelation.getText().toString();
                addData(anhelation);
                break;
            case R.id.symptom_cbox_dyspnea://呼吸困难
                String dyspnea = symptomCboxDyspnea.getText().toString();
                addData(dyspnea);
                break;
            case R.id.symptom_cbox_stethalgia://胸痛
                String stethalgia = symptomCboxStethalgia.getText().toString();
                addData(stethalgia);
                break;
            case R.id.symptom_cbox_conjunctiva://结膜充血
                String conjunctiva = symptomCboxConjunctiva.getText().toString();
                addData(conjunctiva);
                break;
            case R.id.symptom_cbox_sick://恶心
                String CboxHoose = symptomCboxSick.getText().toString();
                addData(CboxHoose);
                break;
            case R.id.symptom_cbox_emesis://呕吐
                String emesis = symptomCboxEmesis.getText().toString();
                addData(emesis);
                break;
            case R.id.symptom_cbox_diarrhea://腹泻
                String diarrhea = symptomCboxDiarrhea.getText().toString();
                addData(diarrhea);
                break;
            case R.id.symptom_cbox_stomachache://腹痛
                String stomachache = symptomCboxStomachache.getText().toString();
                addData(stomachache);
                break;
            default:
                break;
        }
    }


    private void addData(String str) {
        if (!TextUtils.isEmpty(str) && !sb.toString().contains(str)) {
            sb.append(str);
        }
    }

}
