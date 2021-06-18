package exception;

public class VariableNotDeclared extends RuntimeException{
    public VariableNotDeclared(){}
    public VariableNotDeclared(String message) {
        super(message);
    }
}
