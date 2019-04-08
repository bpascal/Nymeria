package com.codido.nymeria.util;


import com.codido.nymeria.bean.resp.BaseResp;

/**
 * 接口响应监听器
 */
public interface ProcessListener {
	public boolean onDone(BaseResp responseBean);

}
