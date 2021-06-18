package model.expressions;

import exception.InexistentVariable;
import model.hashtable.IHashTable;
import model.hashtable.IHeapTable;
import model.types.IType;
import model.values.IValue;

public class VariableExpression implements IExpression{
    String id;

    public VariableExpression(String id)
    {
        this.id = id;
    }

    @Override
    public IValue evaluate(IHashTable<String, IValue> table, IHeapTable<IValue> heap) {
        IValue value = table.get(id);
        if(value == null)
            throw new InexistentVariable("The variable " + id + " is not defined!");
        return value;
    }

    @Override
    public IType typeCheck(IHashTable<String, IType> typeEnvironment){
        return typeEnvironment.get(id);
    }

    @Override
    public String toString()
    {
        return id;
    }
}
