package model.statements;

import exception.LatchException;
import exception.TypeCheckerException;
import exception.TypeNotMatch;
import exception.VariableNotDeclared;
import javafx.util.Pair;
import model.ProgramState;
import model.hashtable.IHashTable;
import model.procedures.Procedure;
import model.types.IType;
import model.types.IntType;
import model.values.IValue;
import model.values.IntValue;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BarrierAwaitStatement implements IStatement {
    String variableId;
    Lock barrierTableLock;

    public BarrierAwaitStatement(String variableId) {
        this.variableId = variableId;
        barrierTableLock = new ReentrantLock();
    }

    @Override
    public ProgramState execute(ProgramState state) throws IOException {
        IValue value = state.getSymbolTable().get(variableId);
        if(value == null)
            throw new VariableNotDeclared("Variable " + variableId + " is not declared");

        if(!value.getType().equals(new IntType()))
            throw new TypeNotMatch("The variable in a newLatchStatement should evaluate to an IntType. It is " + value.getType());

        int index = ((IntValue)value).getValue();

        barrierTableLock.lock();

        var val = state.getBarrierTable().get(index);
        if(val == null)
            throw new LatchException("The Barrier with index " + index + " is not created");

        int length = val.getValue().size();
        int N1 = val.getKey();
        if(N1 > length)
        {
            if(!val.getValue().contains(state.getId())) {
                val.getValue().add(state.getId());
                state.getBarrierTable().set(index, new Pair<>(N1, val.getValue()));
            }
            state.getStack().push(new BarrierAwaitStatement(variableId));
        }
        barrierTableLock.unlock();
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        IType varType = typeEnvironment.get(variableId);
        if(varType == null)
            throw new TypeCheckerException("Variable " + variableId + " is not declared");

        if(!varType.equals(new IntType()))
            throw new TypeCheckerException("The variable in a BarrierAwaitStatement should evaluate to an IntType. It is " + varType);
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "barrierAwait( " + variableId + "); ";
    }
}
