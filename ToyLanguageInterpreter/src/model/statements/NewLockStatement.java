package model.statements;

import exception.TypeCheckerException;
import exception.TypeNotMatch;
import exception.VariableNotDeclared;
import model.ProgramState;
import model.hashtable.IHashTable;
import model.procedures.Procedure;
import model.types.IType;
import model.types.IntType;
import model.values.IValue;
import model.values.IntValue;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewLockStatement implements IStatement{
    private String variableId;
    private Lock lockTableLock;

    public NewLockStatement(String variableId) {
        this.variableId = variableId;
        this.lockTableLock = new ReentrantLock();
    }

    @Override
    public ProgramState execute(ProgramState state) throws IOException {

        lockTableLock.lock();
        int currentAddress = state.getLockTable().insert(-1);
        IValue value = state.getSymbolTable().get(variableId);
        if(value == null)
            throw new VariableNotDeclared("Lock variable not declared");
        if(!value.getType().equals(new IntType()))
            throw new TypeNotMatch("Lock variables shall be of type IntType. " + variableId + " is " + value.getType());

        state.getSymbolTable().insert(variableId, new IntValue(currentAddress));
        lockTableLock.unlock();
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        IType valueType = typeEnvironment.get(variableId);
        if(valueType == null)
            throw new TypeCheckerException("Lock variable not declared");
        if(!valueType.equals(new IntType()))
            throw new TypeCheckerException("Lock variables shall be of type IntType. " + variableId + " is " + valueType);
        return typeEnvironment;
    }


    @Override
    public String toString() {
        return "newLock(" + variableId + "); ";
    }
}
