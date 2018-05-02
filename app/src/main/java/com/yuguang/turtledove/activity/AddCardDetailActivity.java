package com.codido.nymeria.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codido.nymeria.R;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.SendSmsCodeReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.SendSmsCodeResp;
import com.codido.nymeria.util.Constants;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;
import com.codido.nymeria.util.ValidateUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 完善卡信息
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class AddCardDetailActivity extends BaseActivity {


    String realName;
    String idCard;
    String bankCard;
    String bankName;
    String bankIcon;
    String cardType;
    String serialCode;
    String mobile;
    String cvv2;
    String expireDate;

    @BindView(R.id.editTextMbl)
    EditText editTextMbl;

    @BindView(R.id.editTextCVV2)
    EditText editTextCVV2;

    @BindView(R.id.editTextDate)
    EditText editTextDate;

    @BindView(R.id.textViewBankName)
    TextView textViewBankName;

    @BindView(R.id.textViewBankCardType)
    TextView textViewBankCardType;


    @BindView(R.id.textViewBankCardNo)
    TextView textViewBankCardNo;

    @BindView(R.id.imageViewBankIcon)
    ImageView imageViewBankIcon;


    @BindView(R.id.creditCarInfo)
    View creditCarInfo;


    public static void start(Activity content,
                             String realName, String idCard, String bankCard, String bankName, String bankIcon, String cardType) {

        Intent intent = new Intent(content, AddCardDetailActivity.class);
        intent.putExtra("realName", realName);
        intent.putExtra("idCard", idCard);
        intent.putExtra("bankCard", bankCard);
        intent.putExtra("bankName", bankName);
        intent.putExtra("bankIcon", bankIcon);
        intent.putExtra("cardType", cardType);

        content.startActivityForResult(intent, REQ_FOR_ADD_CRAD);
    }


    @Override
    protected void initData(Bundle savedInstanceState) {

        this.realName = getIntent().getStringExtra("realName");
        this.idCard = getIntent().getStringExtra("idCard");
        this.bankCard = getIntent().getStringExtra("bankCard");
        this.bankName = getIntent().getStringExtra("bankName");
        this.bankIcon = getIntent().getStringExtra("bankIcon");
        this.cardType = getIntent().getStringExtra("cardType");


        textViewBankCardNo.setText(this.bankCard);
        textViewBankCardType.setText(Constants.CRADS_TYPES.get(this.cardType));
        textViewBankName.setText(this.bankName);

        if ("0".equals(this.cardType)) {
            creditCarInfo.setVisibility(View.GONE);
        }

        Glide.with(this).load(this.bankIcon).dontAnimate().
                placeholder(R.mipmap.bankcard_icon_default).error(R.mipmap.bankcard_icon_default)
                .diskCacheStrategy(DiskCacheStrategy.RESULT).into(this.imageViewBankIcon);

    }


    @OnClick(R.id.buttonOk)
    void ok() {

        ValidateUtils.Result result;

        result = ValidateUtils.checkRealName(mobile = getTextFromEditText(editTextMbl));

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }

        if ("1".equals(this.cardType)) {
            result = ValidateUtils.checkIDCard(cvv2 = getTextFromEditText(editTextCVV2));

            if (!result.isOk()) {
                showToastText(result.getResult());
                return;
            }

            result = ValidateUtils.checkBankCard(expireDate = getTextFromEditText(editTextDate));

            if (!result.isOk()) {
                showToastText(result.getResult());
                return;
            }
        }


        showProgressDialogCanCancel("请稍后...", 0);

        SendSmsCodeReqData reqData = new SendSmsCodeReqData();
        reqData.setCardNo(bankCard);
        reqData.setMobile(mobile);
        //TODO 接口有修改，如果用户没有实名认证，下发短信验证码的时候就需要增加userName和idNo
        reqData.setUserName(realName);
        reqData.setIdNo(idCard);

        BaseReq baseReq = new BaseReq(Global.key_sendSmsCode, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }


    @OnClick(R.id.textViewWhatIsCVV2)
    void whatIsCVV2() {

        showToastText("去CVV2的介绍页面，这里需要做一个H5");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_card_detail;
    }

    @Override
    public void addAction() {

        addBackAction();
    }


    @Override
    public boolean onDone(BaseResp responseBean) {

        if (Global.key_sendSmsCode.equals(responseBean.getKey())) {
            if (responseBean.isOk()) {
                if (responseBean != null) {

                    runCallFunctionInHandler(CALL_QUERY_SUCCESS, responseBean);
                }
            } else {
                runCallFunctionInHandler(CALL_QUERY_FAILD, responseBean);
            }
        }
        return false;
    }

    @Override
    public void call(int callID, Object... args) {
        if (CALL_QUERY_SUCCESS == callID) {

            cancelProgressDialog();

            SendSmsCodeResp resp = (SendSmsCodeResp) args[0];
            serialCode = resp.getSerialCode();
            SmsCodeActivity.start(this, realName, idCard, bankCard, mobile, serialCode, expireDate, cvv2);

        } else if (CALL_QUERY_FAILD == callID) {

            cancelProgressDialog();

            showToastText(((BaseResp) args[0]).getRespMsg());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_FOR_ADD_CRAD) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
