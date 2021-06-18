package model.expressions;

import model.hashtable.IHashTable;
import model.hashtable.IHeapTable;
import model.types.IType;
import model.values.IValue;

public class ValueExpression implements IExpression{
    IValue value;

    public ValueExpression(IValue value)
    {
        this.value = value;
    }

    @Override
    public IType typeCheck(IHashTable<String, IType> typeEnvironment) {
        return value.getType();
    }

    @Override
    public IValue evaluate(IHashTable<String, IValue> table, IHeapTable<IValue> heap) {
        return this.value;
    }

    @Override
    public String toString()
    {
        return value.toString();
    }
}
