package model.statements;

import exception.TypeCheckerException;
import model.ProgramState;
import model.hashtable.IHashTable;
import model.procedures.Procedure;
import model.types.IType;

import java.io.IOException;

public class LeaveScopeStatement implements IStatement {

    public LeaveScopeStatement(){};

    @Override
    public ProgramState execute(ProgramState state) throws IOException {
        state.popSymbolsStack();
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return " }";
    }
}
