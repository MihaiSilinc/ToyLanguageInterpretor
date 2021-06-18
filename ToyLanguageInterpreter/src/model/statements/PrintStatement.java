package model.statements;

import exception.TypeCheckerException;
import model.ProgramState;
import model.expressions.IExpression;
import model.hashtable.IHashTable;
import model.procedures.Procedure;
import model.types.IType;

public class PrintStatement implements IStatement{
    private final IExpression expression;

    public PrintStatement(IExpression expression)
    {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        state.getOutput().push_back(expression.evaluate(state.getSymbolTable(), state.getHeapTable()));
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        expression.typeCheck(typeEnvironment);
        return typeEnvironment;
    }

    @Override
    public String toString()
    {
        return("write(" + expression.toString() + "); ");
    }
}
