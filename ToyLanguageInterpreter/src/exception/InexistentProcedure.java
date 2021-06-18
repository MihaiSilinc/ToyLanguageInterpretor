package exception;

public class InexistentProcedure extends RuntimeException {
    public InexistentProcedure(){};
    public InexistentProcedure(String message){super(message);};
}
