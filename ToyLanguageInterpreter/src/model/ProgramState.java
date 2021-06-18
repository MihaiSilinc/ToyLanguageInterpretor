package model;

import exception.*;
import javafx.util.Pair;
import model.hashtable.*;
import model.output_list.IOutput;
import model.output_list.Output;
import model.procedures.Procedure;
import model.stack.ExecutionStack;
import model.stack.IExecutionStack;
import model.statements.IStatement;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class ProgramState {
    private IExecutionStack<IStatement> stack;
    private Stack<IHashTable<String, IValue>> symbolStack;
    private IHashTable<String, IValue> currentSymbolTable;
    private IHashTable<StringValue, BufferedReader> fileTable;
    private IHeapTable<Integer> lockTable;
    private IHeapTable<Integer> latchTable;
    private IHeapTable<Pair<Integer, List<Integer>>> barrierTable;
    private IHeapTable<IValue> heapTable;
    private IOutput<IValue> output;
    private IHashTable<String, Procedure> proceduresTable;
    private int id;
    private static int index = 0;

    public ProgramState(IStatement statement, IHeapTable<IValue> heapTable)
    {
        this.stack = new ExecutionStack<>();
        this.currentSymbolTable = new SymbolsTable<>();
        this.symbolStack = new Stack<>();
        this.fileTable = new FileTable<>();
        this.proceduresTable = new ProceduresTable();
        this.heapTable = heapTable;
        this.output = new Output<>();
        this.lockTable = new LockTable();
        this.latchTable = new LatchTable();
        this.barrierTable = new BarrierTable();
        id = index++;
        stack.push(statement);
    }

    public int getId() {
        return id;
    }

    public void setStack(ExecutionStack<IStatement> newStack)
    {
        this.stack = newStack;
    }

    public void setSymbolTable(IHashTable<String, IValue> newTable)
    {
        this.currentSymbolTable = newTable;
    }

    public IHeapTable<Pair<Integer, List<Integer>>> getBarrierTable() {
        return barrierTable;
    }

    public void setBarrierTable(IHeapTable<Pair<Integer, List<Integer>>> barrierTable) {
        this.barrierTable = barrierTable;
    }

    public IHeapTable<Integer> getLatchTable() {
        return latchTable;
    }

    public void setLatchTable(IHeapTable<Integer> latchTable) {
        this.latchTable = latchTable;
    }

    public IHeapTable<Integer> getLockTable() {
        return lockTable;
    }

    public void setLockTable(IHeapTable<Integer> lockTable) {
        this.lockTable = lockTable;
    }

    public void setFileTable(IHashTable<StringValue, BufferedReader> fileTable)
    {
        this.fileTable = fileTable;
    }

    public void setProceduresTable(IHashTable<String, Procedure> proceduresTable) {
        this.proceduresTable = proceduresTable;
    }

    public void setFileTable(IHeapTable<IValue> heapTable)
    {
        this.heapTable = heapTable;
    }

    public void setSymbolStack(Stack<IHashTable<String, IValue>> symbolStack) {
        this.symbolStack = symbolStack;
        this.currentSymbolTable = this.symbolStack.peek();
    }

    public IHashTable<String, Procedure> getProceduresTable() {
        return proceduresTable;
    }

    public void setOutput(IOutput<IValue> newOutput)
    {
        this.output = newOutput;
    }

    public synchronized void index()
    {
        id = index;
    }

    public ProgramState oneStep() throws IOException, ValueNotMatch, EmptyExecutionStack, DivisionByZero, TypeNotMatch, VariableNotDeclared, InexistentVariable, ProgramException, ExistingVariable, UndefinedKey
    {
       if(stack.isEmpty())
            throw new EmptyExecutionStack();

        IStatement currentStatement = stack.pop();
        return currentStatement.execute(this);

    }

    public boolean isNotCompleted()
    {
        return !stack.isEmpty();
    }

    public IExecutionStack<IStatement> getStack()
    {
        return stack;
    }

    public IHashTable<String, IValue> getSymbolTable() {
        return currentSymbolTable;
    }

    public Stack<IHashTable<String, IValue>> getSymbolStack() {return symbolStack;}

    public void popSymbolsStack() {
        symbolStack.pop();
        if(symbolStack.size() > 0)
            currentSymbolTable = symbolStack.peek();
    }

    public void pushSymbolsStack(IHashTable<String, IValue> newSymbolsTable)
    {
        symbolStack.push(newSymbolsTable);
        currentSymbolTable = symbolStack.peek();
    }

    public IHashTable<StringValue, BufferedReader> getFileTable()
    {
        return fileTable;
    }

    public IHeapTable<IValue> getHeapTable()
    {
        return heapTable;
    }

    public IOutput<IValue> getOutput()
    {
        return output;
    }

    @Override
    public String toString()
    {
        return "Thread: " + id + "\n" + "stack:\n" + stack.toString() +
                "\n\nsymbols:\n" + currentSymbolTable.toString() +
                "\n\nprocedures:\n" + proceduresTable.toString() +
                "\n\nfiles:\n" + fileTable.toString() +
                "\n\nheap:\n" + heapTable.toString() +
                "\n\nlocks:\n" + lockTable.toString() +
                "\n\nlatch:\n" + latchTable.toString() +
                "\n\nbarrier:\n" + barrierTable.toString() +
                "\n\noutput:\n" + output.toString() + "\n\n";
    }

}
