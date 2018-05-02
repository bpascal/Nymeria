package com.codido.nymeria.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.codido.nymeria.R;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.BaseReqData;
import com.codido.nymeria.bean.req.QueryBankBybinReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.QueryBankBybinResp;
import com.codido.nymeria.bean.resp.QueryMyUserInfoResp;
import com.codido.nymeria.bean.vo.UserInfoVo;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;
import com.codido.nymeria.util.ValidateUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 卡详情页面
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class AddCardActivity extends BaseActivity {


    @BindView(R.id.editTextUserName)
    EditText editTextUserName;

    @BindView(R.id.editTextIDCard)
    EditText editTextIDCard;

    @BindView(R.id.editTextBankCard)
    EditText editTextBankCard;


    String realName;
    String idCard;
    String bankCard;

    @OnClick(R.id.buttonNext)
    void next() {

        ValidateUtils.Result result;

        result = ValidateUtils.checkRealName(realName = getTextFromEditText(editTextUserName));

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }

        result = ValidateUtils.checkIDCard(idCard = getTextFromEditText(editTextIDCard));

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }

        result = ValidateUtils.checkBankCard(bankCard = getTextFromEditText(editTextBankCard));

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }


        showProgressDialogCanCancel("请稍后...", 0);

        QueryBankBybinReqData reqData = new QueryBankBybinReqData();
        reqData.setCardNo(bankCard);

        BaseReq baseReq = new BaseReq(Global.key_queryBankBybin, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_card;
    }

    @Override
    public void addAction() {

        addBackAction();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        sendQueryMyUserInfoRequest();
    }

    /**
     * 发送获取我的用户信息请求
     */
    private void sendQueryMyUserInfoRequest() {
        BaseReq baseReq = new BaseReq(Global.key_queryMyUserInfo, new BaseReqData());
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }


    @Override
    public boolean onDone(BaseResp responseBean) {
        if (Global.key_queryBankBybin.equals(responseBean.getKey())) {
            //获取订单列表接口响应
            if (responseBean.isOk()) {
                QueryBankBybinResp resp = (QueryBankBybinResp) responseBean;
                runCallFunctionInHandler(CALL_QUERY_SUCCESS, responseBean);
            } else {
                if("2037".equals(responseBean.getRespCode())){
                    //特殊处理，退回主界面
                    runCallFunctionInHandler(CALL_FUNCTION, responseBean);
                }else{
                    runCallFunctionInHandler(CALL_QUERY_FAILD, responseBean);
                }
            }
        } else if (Global.key_queryMyUserInfo.equals(responseBean.getKey())) {
            //查询用户信息
            if (responseBean.isOk()) {
                QueryMyUserInfoResp resp = (QueryMyUserInfoResp) responseBean;

                runCallFunctionInHandler(CALL_UPLOAD_SUCCESS, resp);
            } else {
                runCallFunctionInHandler(CALL_UPLOAD_FAILD);
            }
        }
        return false;
    }

    @Override
    public void call(int callID, Object... args) {
        if (CALL_QUERY_SUCCESS == callID) {
            cancelProgressDialog();
            QueryBankBybinResp resp = (QueryBankBybinResp) args[0];

            AddCardDetailActivity.start(this, realName, idCard, bankCard, resp.getBankName(), resp.getBankIcon(), resp.getCardType());
        } else if (CALL_QUERY_FAILD == callID) {
            cancelProgressDialog();

            showToastText(((BaseResp) args[0]).getRespMsg());
        } else if (CALL_UPLOAD_SUCCESS == callID) {
            //查询用户信息成功，回填信息并禁用控件
            QueryMyUserInfoResp resp = (QueryMyUserInfoResp) args[0];
            if (resp != null) {
                UserInfoVo userInfoVo = resp.getUserInfo();
                if (userInfoVo.getUserName() != null && !"".equals(userInfoVo.getUserName())) {
                    editTextUserName.setText(userInfoVo.getUserName());
                    editTextUserName.setEnabled(false);
                } else {
                    editTextUserName.setEnabled(true);
                }
                if (userInfoVo.getIdNo() != null && !"".equals(userInfoVo.getIdNo())) {
                    editTextIDCard.setText(userInfoVo.getIdNo());
                    editTextIDCard.setEnabled(false);
                } else {
                    editTextIDCard.setEnabled(true);
                }
            } else {
                editTextUserName.setEnabled(true);
                editTextIDCard.setEnabled(true);
            }
        } else if (CALL_UPLOAD_FAILD == callID) {
            //查询用户信息失败
            showToastText(((BaseResp) args[0]).getRespMsg());
        } else if(CALL_FUNCTION == callID){
            //特殊处理，退回主界面
            setResult(RESULT_OK);
            finish();
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
