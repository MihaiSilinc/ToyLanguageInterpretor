package model.statements;

import exception.TypeCheckerException;
import model.ProgramState;
import model.hashtable.IHashTable;
import model.hashtable.SymbolsTable;
import model.procedures.Procedure;
import model.types.IType;
import model.values.IValue;

import java.io.IOException;

public class EnterScopeStatement implements IStatement{

    public EnterScopeStatement(){};

    @Override
    public ProgramState execute(ProgramState state) throws IOException {
        IHashTable<String, IValue> newSymbolsTable = new SymbolsTable<String, IValue>();
        state.pushSymbolsStack(newSymbolsTable);
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "{ ";
    }
}
