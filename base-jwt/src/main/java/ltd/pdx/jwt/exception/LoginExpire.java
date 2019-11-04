package ltd.pdx.jwt.exception;

public class LoginExpire extends JWTException {

    public LoginExpire() {
        super(1002,"登录过期");
    }
}
