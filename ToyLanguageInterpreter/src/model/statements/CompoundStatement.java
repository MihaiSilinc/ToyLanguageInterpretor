package model.statements;

import exception.TypeCheckerException;
import model.ProgramState;
import model.hashtable.IHashTable;
import model.procedures.Procedure;
import model.stack.IExecutionStack;
import model.types.IType;

public class CompoundStatement implements IStatement{
    private final IStatement first;
    private final IStatement second;

    public CompoundStatement(IStatement first, IStatement second)
    {
        this.first = first;
        this.second = second;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        IExecutionStack<IStatement> stack = state.getStack();
        stack.push(second);
        stack.push(first);
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        return second.typeCheck(first.typeCheck(typeEnvironment, proceduresTable), proceduresTable);
    }

    @Override
    public String toString()
    {
        return first.toString() + second.toString();
    }
}
