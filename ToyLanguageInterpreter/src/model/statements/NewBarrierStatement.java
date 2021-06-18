package model.statements;

import exception.TypeCheckerException;
import exception.TypeNotMatch;
import exception.VariableNotDeclared;
import javafx.util.Pair;
import model.ProgramState;
import model.expressions.IExpression;
import model.hashtable.IHashTable;
import model.procedures.Procedure;
import model.types.IType;
import model.types.IntType;
import model.values.IValue;
import model.values.IntValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewBarrierStatement implements IStatement {
    String variableId;
    IExpression expression;
    Lock barrierTableLock;

    public NewBarrierStatement(String variableId, IExpression expression) {
        this.variableId = variableId;
        this.expression = expression;
        barrierTableLock = new ReentrantLock();
    }

    @Override
    public ProgramState execute(ProgramState state) throws IOException {
        IValue value = expression.evaluate(state.getSymbolTable(), state.getHeapTable());
        if(!value.getType().equals(new IntType()))
            throw new TypeNotMatch("The expression in a newBarrierStatement should evaluate to an IntType. It is " + value.getType());

        barrierTableLock.lock();
        int currentAddress = state.getBarrierTable().insert(new Pair<>(((IntValue)value).getValue(), new ArrayList<>()));

        IValue variable = state.getSymbolTable().get(variableId);

        if(variable != null && !variable.getType().equals(new IntType()))
            throw new TypeNotMatch("The variable in a newLatchStatement should evaluate to an IntType. It is " + variable.getType());

        state.getSymbolTable().insert(variableId, new IntValue(currentAddress));

        barrierTableLock.unlock();

        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        IType type = expression.typeCheck(typeEnvironment);
        if(type == null)
            throw new TypeCheckerException();
        if(!type.equals(new IntType()))
            throw new TypeCheckerException("The expression in a newBarrierStatement should evaluate to an IntType. It is " + type);

        IType varType = typeEnvironment.get(variableId);
        if(varType == null)
            throw new TypeCheckerException("Variable " + variableId + " is not declared");

        if(!varType.equals(new IntType()))
            throw new TypeCheckerException("The variable in a newBarrierStatement should evaluate to an IntType. It is " + varType);
        return typeEnvironment;

    }

    @Override
    public String toString() {
        return "newBarrier( " + variableId + ", " + expression.toString() + "); ";
    }
}
