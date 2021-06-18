package model.statements;

import exception.TypeCheckerException;
import exception.TypeNotMatch;
import exception.VariableNotDeclared;
import model.ProgramState;
import model.expressions.IExpression;
import model.expressions.ValueExpression;
import model.hashtable.IHashTable;
import model.procedures.Procedure;
import model.types.BoolType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;

import java.io.IOException;

public class ConditionalAssignmentStatement implements IStatement
{
    IExpression expression1, expression2, expression3;
    String variableId;

    public ConditionalAssignmentStatement(IExpression expression1, IExpression expression2, IExpression expression3, String variableId) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
        this.variableId = variableId;
    }

    @Override
    public ProgramState execute(ProgramState state) throws IOException {
        IValue variable = state.getSymbolTable().get(variableId);
        if(variable == null)
            throw new VariableNotDeclared("Variable " + variableId + " was not previously declared!");

        IValue value1 = expression1.evaluate(state.getSymbolTable(), state.getHeapTable());
        if(!value1.getType().equals(new BoolType()))
            throw new TypeNotMatch("The first expression in a conditional assignment statement should be boolean!");
        IValue value2 = expression2.evaluate(state.getSymbolTable(), state.getHeapTable());
        if(!value2.getType().equals(variable.getType()))
            throw new TypeNotMatch("The assign expression and the variable types don't match!");
        IValue value3 = expression3.evaluate(state.getSymbolTable(), state.getHeapTable());
        if(!value3.getType().equals(variable.getType()))
            throw new TypeNotMatch("The assign expression and the variable types don't match!");

        IStatement statement = new IfStatement(expression1,
                new AssignStatement(variableId, expression2),
                new AssignStatement(variableId, expression3));

        state.getStack().push(statement);
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        IType variable = typeEnvironment.get(variableId);
        if(variable == null)
            throw new TypeCheckerException("Variable " + variableId + " was not previously declared!");

        IType type1 = expression1.typeCheck(typeEnvironment);
        if(!type1.equals(new BoolType()))
            throw new TypeCheckerException("The first expression in a conditional assignment statement should be boolean!");
        IType type2 = expression2.typeCheck(typeEnvironment);
        if(!type2.equals(variable))
            throw new TypeCheckerException("The assign expression and the variable types don't match!");
        IType type3 = expression3.typeCheck(typeEnvironment);
        if(!type3.equals(variable))
            throw new TypeCheckerException("The assign expression and the variable types don't match!");

        return typeEnvironment;
    }

    @Override
    public String toString() {
        return variableId + " = " + expression1.toString() + " ? " + expression2.toString() + " : " + expression3.toString() + "; ";
    }
}
