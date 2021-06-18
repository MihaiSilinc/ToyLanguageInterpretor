package model.statements;

import exception.LatchException;
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
import model.values.StringValue;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountDownStatement implements IStatement{
    String variableId;
    Lock latchTableLock;

    public CountDownStatement(String variableId) {
        this.variableId = variableId;
        latchTableLock = new ReentrantLock();
    }

    @Override
    public ProgramState execute(ProgramState state) throws IOException {
        IValue value = state.getSymbolTable().get(variableId);
        if(value == null)
            throw new VariableNotDeclared("Variable " + variableId + " is not declared");

        if(!value.getType().equals(new IntType()))
            throw new TypeNotMatch("The variable in a newLatchStatement should evaluate to an IntType. It is " + value.getType());

        int index = ((IntValue)value).getValue();

        latchTableLock.lock();

        Integer val = state.getLatchTable().get(index);
        if(val == null)
            throw new LatchException("The Latch with index " + index + " is not created");

        if(val > 0)
        {
            state.getLatchTable().set(index, val-1);
        }
        state.getOutput().push_back(new IntValue(state.getId()));

        latchTableLock.unlock();
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        IType varType = typeEnvironment.get(variableId);
        if(varType == null)
            throw new TypeCheckerException("Variable " + variableId + " is not declared");

        if(!varType.equals(new IntType()))
            throw new TypeCheckerException("The variable in a newLatchStatement should evaluate to an IntType. It is " + varType);
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "countDown( " + variableId + "); ";
    }
}
