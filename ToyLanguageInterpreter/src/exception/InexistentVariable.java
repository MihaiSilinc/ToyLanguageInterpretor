package exception;

public class InexistentVariable extends RuntimeException{
    public InexistentVariable(){}
    public InexistentVariable(String message){ super(message);}
}
