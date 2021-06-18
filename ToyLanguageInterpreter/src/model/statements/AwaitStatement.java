package model.statements;

import exception.LatchException;
import exception.TypeCheckerException;
import exception.TypeNotMatch;
import exception.VariableNotDeclared;
import model.ProgramState;
import model.expressions.ValueExpression;
import model.hashtable.IHashTable;
import model.hashtable.LatchTable;
import model.procedures.Procedure;
import model.types.IType;
import model.types.IntType;
import model.values.IValue;
import model.values.IntValue;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitStatement implements IStatement{
    String variableId;
    Lock latchTableLock;

    public AwaitStatement(String variableId) {
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
            state.getStack().push(new AwaitStatement(variableId));

        latchTableLock.unlock();
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        IType varType = typeEnvironment.get(variableId);
        if(varType == null)
            throw new TypeCheckerException("Variable " + variableId + " is not declared");

        if(!varType.equals(new IntType()))
            throw new TypeCheckerException("The variable in a awaitStatement should evaluate to an IntType. It is " + varType);
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "await( " + variableId + "); ";
    }
}
