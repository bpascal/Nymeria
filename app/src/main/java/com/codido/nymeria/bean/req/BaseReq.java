package com.codido.nymeria.bean.req;

import com.codido.nymeria.util.Global;

import java.io.Serializable;

/**
 * 请求对象
 */
public class BaseReq implements Serializable {
	private static final long serialVersionUID = 1L;
	private String key; // 本类请求的交易码
	private BaseReqData reqData; // 生成请求参数的对象
	private String encoding; // 本类请求使用的字符集，一般情况下无需设置
	private boolean isLog = Global.NET_REQ_DEBUG_FALG; // 本类请求是否需要打印日志的标识

	public BaseReqData getReqData() {
		return reqData;
	}

	public BaseReq() {

	}

	public BaseReq(String key) {
		super();
		this.key = key;
	}

	public BaseReq(String key, BaseReqData reqData) {
		super();
		this.key = key;
		this.reqData = reqData;
	}

	public void setReqData(BaseReqData reqData) {
		this.reqData = reqData;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public boolean isLog() {
		return isLog;
	}

	public void setLog(boolean isLog) {
		this.isLog = isLog;
	}
}
