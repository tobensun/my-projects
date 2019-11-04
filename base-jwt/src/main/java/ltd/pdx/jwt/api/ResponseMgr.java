package ltd.pdx.jwt.api;

import ltd.pdx.jwt.config.JWTConstant;
import ltd.pdx.jwt.model.CommonResult;

public class ResponseMgr {
	
	/**
	 * 成功请求不带数据
	 * @return
	 */
	public static String success() {
		CommonResult commonResult = new CommonResult(JWTConstant.RESCODE_SUCCESS, "success");
		return commonResult.general();
	}
	
	/**
	 * 成功请求带返回数据
	 * @param data
	 * @return
	 */
	public static String successWithData(Object data) {
		CommonResult commonResult = new CommonResult(JWTConstant.RESCODE_SUCCESS, data, "success");
		return commonResult.general();
	}
	
	/**
	 * 服务器异常不带数据
	 * @return
	 */
	public static String err() {
		CommonResult commonResult = new CommonResult(JWTConstant.RESCODE_EXCEPTION, "请稍后再试");
		return commonResult.general();
	}

	/**
	 * 服务器异常带数据
	 * @param data
	 * @return
	 */
	public static String errWhitData(Object data) {
		CommonResult commonResult = new CommonResult(JWTConstant.RESCODE_EXCEPTION, data, "请稍后再试");
		return commonResult.general();
	}
	
	/**
	 * 服务器异常带数据和消息
	 * @param data
	 * @return
	 */
	public static String errWhitData(String msg, Object data) {
		CommonResult commonResult = new CommonResult(JWTConstant.RESCODE_EXCEPTION, data, msg);
		return commonResult.general();
	}
	
	/**
	 * 用户未登录
	 * @return
	 */
	public static String noLogin() {
		CommonResult commonResult = new CommonResult(JWTConstant.RESCODE_NOLOGIN, "用户未登录");
		return commonResult.general();
	}
	

	
	/**
	 * 无操作权限
	 * @return
	 */
	public static String noAuth() {
		CommonResult commonResult = new CommonResult(JWTConstant.RESCODE_NOAUTH, "拒绝授权");
		return commonResult.general();
	}
	
	/**
	 * 登录过期
	 * @return
	 */
	public static String loginExpire() {
		CommonResult commonResult = new CommonResult(JWTConstant.RESCODE_LOGINEXPIRE, "登录过期");
		return commonResult.general();
	}
}