package com.codido.nymeria.util;

import java.io.File;

/**
 * Created by pc on 2016/11/12.
 * 
 * @author liuzhongjun
 */

public class Urls {

	public static final String RESPONSE_SUCCESS = "200";
	public static final String RESPONSE_ERROR = "500";
	public static final String RESPONSE_ERROR_NODEVICE = "401";

	// 文件夹分配
	public static String separator = File.separator;
	public static final String ROOTPATH = separator + "ystOnBoardSwipe";

	// YMS服务器
	public static final String YMS_HTTP = "http://yms.mingcloud.net/";
	//public static final String YMS_HTTP = "http://csyms.mingcloud.net/yms/";

	// 获取学校信息
	public static final String GET_SCHOOLINFO = YMS_HTTP + "getSchoolInfo";
	// 数据初始化协议
	public static final String UPDATE_OR_NOT = YMS_HTTP + "updateOrNot";
	// 得到学生信息
	public static final String GET_STUDENT_USER = YMS_HTTP + "getStudentUser";
	// 申请开始记录轨迹
	public static final String BEGIN_UPLOAD_LOCATION = YMS_HTTP + "beginTrace";
	// 上报轨迹
	public static final String UPLOAD_LOCATION = YMS_HTTP + "uploadGPS";
	// 申请结束上报轨迹
	public static final String END_UPLOAD_LOCATION = YMS_HTTP + "endTrace";
	// 通过如离园卡号获取学生信息
	public static final String GET_STUDENT_USER_BY_CARD = YMS_HTTP + "getStudentUserByCard";
	// 打卡记录上传
	public static final String UPLOAD_PUSH_CARD_RECORDS = YMS_HTTP + "clockCard";
	// 广告接口协议
	public static final String GET_ADS_LIST = YMS_HTTP + "getAdsList";
	// 学校通知接口协议
	public static final String GET_NOTICE_LIST = YMS_HTTP + "getNoticeList";
	// 上传图片地址
	public static final String UPLOAD_PIC = YMS_HTTP + "";
	// 下载图片地址
	public static final String DOWNLOAD_PIC = YMS_HTTP + "";
	// 获取当前城市的天气地址
	public static final String GET_WEATHER = "http://wthrcdn.etouch.cn/weather_mini";

}
