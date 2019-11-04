package ltd.pdx.jwt.model;

import ltd.pdx.jwt.utils.GsonUtil;

/**
 * 标准数据返回格式	
 * @author xuxile
 *
 */
public class CommonResult {

	private Integer code;//返回码
	
	private Object data;//业务数据
	
	private String msg;//返回描述
	
	private String token;//身份标识
	
	

	public CommonResult(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public CommonResult(Integer code, Object data, String msg) {
		super();
		this.code = code;
		this.data = data;
		this.msg = msg;
	}

	public CommonResult(Integer code, Object data, String msg, String token) {
		super();
		this.code = code;
		this.data = data;
		this.msg = msg;
		this.token = token;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * 请求返回数据处理
	 * @param commonResult
	 * @return
	 */
	public String general() {
		return GsonUtil.objectToJsonStr(this);
	}
}
