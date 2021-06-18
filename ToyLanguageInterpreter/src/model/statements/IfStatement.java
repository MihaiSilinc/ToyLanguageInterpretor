package model.statements;

import exception.*;
import model.ProgramState;
import model.expressions.IExpression;
import model.hashtable.IHashTable;
import model.hashtable.TypeEnvironment;
import model.procedures.Procedure;
import model.stack.IExecutionStack;
import model.types.BoolType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;

public class IfStatement implements IStatement{
    private final IExpression expression;
    private final IStatement thenStatement;
    private final IStatement elseStatement;

    public IfStatement(IExpression expression, IStatement thenStatement, IStatement elseStatement)
    {
        this.expression = expression;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        IExecutionStack<IStatement> stack = state.getStack();

        IValue result = expression.evaluate(state.getSymbolTable(), state.getHeapTable());
        IType resultType = result.getType();

        if(resultType.equals(new BoolType()))
        {
            BoolValue booleanResult = (BoolValue) result;
            boolean resultValue = booleanResult.getValue();
            if(resultValue)
                stack.push(thenStatement);
            else if(elseStatement != null)
                stack.push(elseStatement);
        }
        else
            throw new TypeNotMatch("Conditional expression is not a boolean! (" + resultType.toString() + ") ");

        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {

        IType expressionType = expression.typeCheck(typeEnvironment);
        if(expressionType.equals(new BoolType()))
        {
            thenStatement.typeCheck(new TypeEnvironment((TypeEnvironment)typeEnvironment), proceduresTable );
            elseStatement.typeCheck(new TypeEnvironment((TypeEnvironment)typeEnvironment), proceduresTable);
            return typeEnvironment;
        }
        throw new TypeCheckerException("Conditional expression is not a boolean! (" + expressionType.toString() + ")");
    }

    @Override
    public String toString()
    {
        return "if(" + expression.toString() + ") then: " + thenStatement.toString() + "\telse: " + elseStatement.toString() + "; ";
    }
}
