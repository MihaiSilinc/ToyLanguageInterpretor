package exception;

public class DivisionByZero extends RuntimeException{
    public DivisionByZero(){}
    public DivisionByZero(String message){ super(message);}
}
