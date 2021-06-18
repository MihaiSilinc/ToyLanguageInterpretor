package model.statements;

import model.ProgramState;
import model.hashtable.IHashTable;
import model.procedures.Procedure;
import model.types.IType;

public class NOPStatement implements IStatement{
    @Override
    public ProgramState execute(ProgramState state) {
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) {
        return typeEnvironment;
    }

    @Override
    public String toString()
    {
        return "nop";
    }
}
