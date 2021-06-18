package model.statements;

import exception.TypeCheckerException;
import exception.TypeNotMatch;
import model.ProgramState;
import model.expressions.IExpression;
import model.expressions.ValueExpression;
import model.hashtable.IHashTable;
import model.procedures.Procedure;
import model.types.IType;
import model.types.IntType;
import model.values.IValue;
import model.values.IntValue;

import java.io.IOException;

public class SleepStatement implements IStatement{
    private IExpression expression;

    public SleepStatement(IExpression expression)
    {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws IOException {
        IValue value = expression.evaluate(state.getSymbolTable(), state.getHeapTable());
        if(value.getType().equals(new IntType())) {
            IntValue intValue = (IntValue) value;
            int time = intValue.getValue();
            if (time > 0)
                state.getStack().push(new SleepStatement(new ValueExpression(new IntValue(time - 1))));
        }
        else
            throw new TypeNotMatch("SleepStatement requires an expression that evaluates to an integer value!. It is " + value.getType().toString());
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        if(expression.typeCheck(typeEnvironment).equals(new IntType()))
            return typeEnvironment;
        throw new TypeCheckerException("SleepStatement receives as parameter an expression that evaluates to an IntType value!");
    }

    @Override
    public String toString() {
        return "sleep(" + expression.toString() + "); ";
    }
}
