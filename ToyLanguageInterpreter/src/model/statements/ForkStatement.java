package model.statements;

import exception.TypeCheckerException;
import model.ProgramState;
import model.hashtable.IHashTable;
import model.hashtable.SymbolsTable;
import model.hashtable.TypeEnvironment;
import model.procedures.Procedure;
import model.types.IType;
import model.values.IValue;

import java.util.Stack;

public class ForkStatement implements IStatement{
    IStatement statement;

    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        ProgramState newProgram = new ProgramState(statement, state.getHeapTable());
        newProgram.setFileTable(state.getFileTable());
        newProgram.setOutput(state.getOutput());
        newProgram.setProceduresTable(state.getProceduresTable());
        newProgram.setLockTable(state.getLockTable());
        newProgram.setLatchTable(state.getLatchTable());
        newProgram.setBarrierTable(state.getBarrierTable());

        Stack<IHashTable<String, IValue>> newSymbolStack = new Stack<>();
        for(var table : state.getSymbolStack()) {
            IHashTable<String, IValue> newSymbolsTable = new SymbolsTable<>();
            for (var entry : table.entrySet()) {
                newSymbolsTable.insert(entry.getKey(), entry.getValue().clone());
            }
            newSymbolStack.add(newSymbolsTable);
        }
        newProgram.setSymbolStack(newSymbolStack);
        return newProgram;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        return statement.typeCheck(new TypeEnvironment((TypeEnvironment)typeEnvironment), proceduresTable);
    }

    @Override
    public String toString()
    {
        return "fork(" + statement.toString() + "); ";
    }
}
