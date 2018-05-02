package com.codido.nymeria.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bpascal on 2016/12/11.
 */
public class ValidateUtils {
    public static class Result {
        public Result(boolean ok, String result) {
            super();
            this.ok = ok;
            this.result = result;
        }

        private boolean ok;
        private String result;

        public boolean isOk() {
            return ok;
        }

        public String getResult() {
            return result;
        }

        public void setOk(boolean ok) {
            this.ok = ok;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    public static final String OK = "~OK";
    /**
     * 手机号码号段
     */
    public static final String[] MOBILE_HEADER = new String[]{"133", "153", "180", "181", "189",
            "130", "131", "132", "145", "155", "156", "185", "186", "134", "135", "136", "137",
            "138", "139", "147", "150", "151", "152", "157", "158", "159", "182", "183", "184",
            "187", "188", "170"};


    /**
     * 检查短信流水是否正常获取
     *
     * @param serialCode
     * @return
     */
    public static Result checkSerialCode(String serialCode) {

        if (YUtils.isEmpty(serialCode)) {
            return new Result(false, "请先获取短信验证码");
        }


        return new Result(true, OK);
    }



    /**
     * 检查是否是短信验证码，规则为：6位数字
     *
     * @param code
     * @return
     */
    public static Result checkMblCode(String code) {

        if (YUtils.isEmpty(code)) {
            return new Result(false, "短信验证码");
        }

        Pattern pattern = Pattern.compile("^[0-9]{6}$");
        Matcher matcher = pattern.matcher(code);

        if (!matcher.find()) {
            return new Result(false, "短信验证码格式错误");
        }

        return new Result(true, OK);
    }


    /**
     * 检查身份证
     *
     * @param idCard
     * @return
     */
    public static Result checkIDCard(String idCard) {

        if (YUtils.isEmpty(idCard)) {
            return new Result(false, "身份证号不能为空");
        }


        return new Result(true, OK);
    }


    /**
     * 检查信用卡的还款日和账单日
     *
     * @param date
     * @return
     */
    public static Result checkCreditCardDate(String date) {


        if (YUtils.isEmpty(date)) {
            return new Result(false, "账单日或还款日不能为空");
        }

        Pattern pattern = Pattern.compile("^[0-9]{1,2}$");
        Matcher matcher = pattern.matcher(date);

        if (matcher.find()) {

            if(Integer.parseInt(date) > 31){
                return new Result(false, "账单日或还款日格式错误");
            }
            return new Result(true, OK);
        }

        return new Result(false, "账单日或还款日格式错误");
    }


    /**
     * 检查用户CVV2
     *
     * @param cvv2
     * @return
     */
    public static Result checkCreditCardCVV2(String cvv2) {

        if (YUtils.isEmpty(cvv2)) {
            return new Result(false, "CVV2不能为空");
        }

        Pattern pattern = Pattern.compile("^[0-9]{3}$");
        Matcher matcher = pattern.matcher(cvv2);

        if (matcher.find()) {
            return new Result(true, OK);
        }

        return new Result(false, "CVV2必须是3位数字");
    }


    /**
     * 检查用户真实姓名
     *
     * @param realName
     * @return
     */
    public static Result checkRealName(String realName) {

        if (YUtils.isEmpty(realName)) {
            return new Result(false, "姓名不能为空");
        }


        return new Result(true, OK);
    }

    /**
     * 检查银行卡
     *
     * @param bankCard
     * @return
     */
    public static Result checkBankCard(String bankCard) {

        if (YUtils.isEmpty(bankCard)) {
            return new Result(false, "银行卡号不能为空");
        }


        return new Result(true, OK);
    }


    /**
     * 检查URL
     *
     * @param url
     * @return
     */
    public static Result checkHttpUrl(String url) {

        if (YUtils.isEmpty(url)) {
            return new Result(false, "URL为空");
        }

        Pattern pattern = Pattern
                .compile("^(http[s]{0,1}://)([a-zA-Z0-9\\-]+\\.)+[a-zA-Z0-9\\-]+(/[a-zA-Z0-9\\-./?%_&=#]*)?");
        Matcher matcher = pattern.matcher(url);

        if (!matcher.find()) {
            return new Result(false, "这不是一个http链接地址");
        }

        return new Result(true, OK);
    }


    public static Result checkPass(String passwrod) {
        if (YUtils.isEmpty(passwrod)) {
            return new Result(false, "密码不能为空");
        }
        if (passwrod.length() < 6) {
            return new Result(false, "密码长度不能小于6位");
        }
        return new Result(true, OK);
    }


    /**
     * 校验手机号码是否正确
     *
     * @param mbl
     * @return
     */
    public static Result checkMblNo(String mbl) {
        if (YUtils.isEmpty(mbl)) {
            return new Result(false, "手机号码不能为空");
        }

        Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        Matcher matcher = pattern.matcher(mbl);

        if (matcher.find()) {
            return new Result(true, OK);
        }

        return new Result(false, "手机号码格式错误");
    }

    /**
     * @param money
     * @return
     */
    public static Result checkMoney(String money) {
        if (YUtils.isEmpty(money)) {
            return new Result(false, "金额不能为空");
        }
        Pattern pattern = Pattern.compile("^[0-9]{0,10}.{0,1}[0-9]{0,2}$");
        Matcher matcher = pattern.matcher(money);

        if (matcher.find()) {
            return new Result(true, OK);
        }

        return new Result(false, "金额格式错误");
    }

}
