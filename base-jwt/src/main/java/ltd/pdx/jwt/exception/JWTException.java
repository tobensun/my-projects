package ltd.pdx.jwt.exception;

import lombok.Data;

/**
 * 权限异常
 */
@Data
public class JWTException extends Exception{



    private int code;
    private String message;


    public JWTException(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
