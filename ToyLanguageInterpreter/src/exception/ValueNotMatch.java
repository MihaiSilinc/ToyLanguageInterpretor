package exception;

public class ValueNotMatch extends RuntimeException{
    public ValueNotMatch(){}
    public ValueNotMatch(String message)
    {
        super(message);
    }
}
