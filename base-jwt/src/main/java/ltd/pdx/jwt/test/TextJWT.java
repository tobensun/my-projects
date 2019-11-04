package ltd.pdx.jwt.test;

import ltd.pdx.jwt.api.TokenMgr;
import ltd.pdx.jwt.config.JWTConstant;

public class TextJWT {

	public static void main(String[] args) {
		try {
			System.out.println(TokenMgr.createJWT("78sawdff5", JWTConstant.JWT_ISS,"xiaohuahua", 1 * 60 * 10000));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis());
		
		String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3OHNhd2RmZjUiLCJpc3MiOiJqd3QtZGVtbyIsInN1YiI6InhpYW9odWFodWEiLCJpYXQiOjE1MDU4ODE4OTMsImV4cCI6MTUwNTg4MjQ5M30.0IUzDZJ8YarfoVymcfqDIBmXUlUJgRXll-d2mRlRyIY";
		try {
			System.out.println(TokenMgr.parseJWT(jwt));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
