package ltd.pdx.jwt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * jwt所面向的用户
 * @author xuxile
 *
 */
@NoArgsConstructor
@Data
public class JWTUser {
	private Integer userId;
	private String nickName;
	private String realName;
	private String openId;
	private String phoneNum;

	public JWTUser(Integer userId, String nickName, String realName, String openId,String phoneNum) {
		this.userId = userId;
		this.nickName = nickName;
		this.realName = realName;
		this.openId = openId;
		this.phoneNum = phoneNum;
	}
}