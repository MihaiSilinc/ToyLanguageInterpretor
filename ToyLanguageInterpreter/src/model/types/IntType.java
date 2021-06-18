package model.types;

import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;

public class IntType implements IType{

    @Override
    public boolean equals(Object other)
    {
        return other instanceof IntType;
    }

    @Override
    public IValue defaultValue()
    {
        return new IntValue(0);
    }

    @Override
    public String toString()
    {
        return "int";
    }
}
