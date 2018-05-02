package com.codido.nymeria.bean.vo;

import java.io.Serializable;

/**
 * 刷卡记录对象
 */
public class CardRecord implements Serializable {

	/**
	 * 无参构造方法
	 */
	public CardRecord(){

	}

	/*
	 * 卡号
	 */
	private String cardId;
	/*
	 * 描述信息
	 */
	private String describe;
	/*
	 * 如离园状态
	 */
	private String state;
	/*
	 * 截图文件路径
	 */
	private String picPath;

	/*
	 * 打卡时间
	 */
	private long dateTime;

	public CardRecord(String cardId, String describe, String state, String picPath,long dateTime) {
		super();
		this.cardId = cardId;
		this.describe = describe;
		this.state = state;
		this.picPath = picPath;
		this.dateTime = dateTime;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}


}
