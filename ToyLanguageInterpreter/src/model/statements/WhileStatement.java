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


public class WhileStatement implements IStatement{
    IExpression expression;
    IStatement statement;

    public WhileStatement(IExpression expression,  IStatement statement)
    {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        IValue value = expression.evaluate(state.getSymbolTable(), state.getHeapTable());
        IType type = value.getType();

        if(type.equals(new BoolType()))
        {
            boolean result = ((BoolValue) value).getValue();
            if(result == true)
            {
                IExecutionStack<IStatement> stack = state.getStack();
                stack.push(new WhileStatement(expression, statement));
                stack.push(statement);
            }
        }
        else
            throw new TypeNotMatch("The condition in a WhileStatement must evaluate to a BooleanType value! It is " + type.toString());

        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {

        IType expressionType = expression.typeCheck(typeEnvironment);
        if(expressionType.equals(new BoolType()))
        {
            return statement.typeCheck(new TypeEnvironment((TypeEnvironment)typeEnvironment), proceduresTable);
        }
        throw new TypeCheckerException("Conditional expression is not a boolean! (" + expressionType.toString() + ")");
    }

    @Override
    public String toString()
    {
        return "while(" + expression.toString() + ") { " + statement.toString() + " }; ";
    }
}
