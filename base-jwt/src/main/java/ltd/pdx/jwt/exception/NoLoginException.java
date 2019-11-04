package ltd.pdx.jwt.exception;

public class NoLoginException extends JWTException {
    public NoLoginException() {
        super(1003,"未登陆");
    }
}
