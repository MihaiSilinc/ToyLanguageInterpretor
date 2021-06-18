package exception;

public class ExistingVariable extends RuntimeException{
    public ExistingVariable(){}
    public ExistingVariable(String message){ super(message);}
}
