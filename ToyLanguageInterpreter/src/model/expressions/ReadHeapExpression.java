package model.expressions;

import exception.*;
import model.hashtable.IHashTable;
import model.hashtable.IHeapTable;
import model.types.IType;
import model.types.ReferenceType;
import model.values.IValue;
import model.values.ReferenceValue;

public class ReadHeapExpression implements IExpression{
    private final IExpression expression;

    public ReadHeapExpression(IExpression expression)
    {
        this.expression = expression;
    }

    @Override
    public IValue evaluate(IHashTable<String, IValue> table, IHeapTable<IValue> heap) {
        IValue value = expression.evaluate(table, heap);
        IType type = value.getType();
        if (type instanceof ReferenceType) {
            ReferenceValue referenceValue = (ReferenceValue) value;
            int address = referenceValue.getAddress();
            IValue referencedValue = heap.get(address);
            if(referencedValue == null)
                throw new UndefinedKey("The key '"+ address +"' is not defined in the HeapTable!");

            return referencedValue;

        } else
            throw new TypeNotMatch("The expression inside ReadHeapExpression shall evaluate to a value of type ReferenceType." +
                    " It is " + type.toString());
    }

    @Override
    public IType typeCheck(IHashTable<String, IType> typeEnvironment) throws TypeCheckerException {
        IType type = expression.typeCheck(typeEnvironment);
        if(type instanceof ReferenceType)
        {
            return ((ReferenceType) type).getInnerType();
        }
        else
            throw new TypeCheckerException("The expression inside ReadHeapExpression shall evaluate to a value of type ReferenceType." +
                    " It is " + type.toString());
    }

    @Override
    public String toString()
    {
        return "readHeap(" + expression.toString() + ")";
    }
}
