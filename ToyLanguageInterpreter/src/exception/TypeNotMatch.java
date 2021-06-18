package exception;

public class TypeNotMatch extends RuntimeException {
    public TypeNotMatch(){}
    public TypeNotMatch(String message) {
        super(message);
    }
}
