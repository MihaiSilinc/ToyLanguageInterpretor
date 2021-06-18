package model.statements;

import exception.*;
import model.ProgramState;
import model.hashtable.IHashTable;
import model.procedures.Procedure;
import model.types.IType;
import model.values.IValue;

public class VariableDeclarationStatement implements IStatement{
    private final IType type;
    private final String id;

    public VariableDeclarationStatement(String id, IType type)
    {
        this.type = type;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public IType getType() {
        return type;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        IHashTable<String, IValue> table = state.getSymbolTable();

        if(table.get(id) != null)
            throw new ExistingVariable("Variable " + id + " is already declared!");

        IValue defaultValue = type.defaultValue();
        table.insert(id, defaultValue);

        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        typeEnvironment.insert(id, type);
        return typeEnvironment;
    }

    @Override
    public String toString()
    {
        return type.toString() + " " + id + "; " ;
    }
}
