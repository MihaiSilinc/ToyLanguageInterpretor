package exception;

public class UndefinedKey extends RuntimeException{
    public UndefinedKey(){}
    public UndefinedKey(String message){ super(message);}
}
