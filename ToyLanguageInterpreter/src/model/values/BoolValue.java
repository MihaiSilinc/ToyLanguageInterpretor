package model.values;

import model.types.BoolType;
import model.types.IType;

public class BoolValue implements IValue{
    private final boolean value;

    public BoolValue(boolean value)
    {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public IType getType()
    {
        return new BoolType();
    }

    @Override
    public boolean equals(Object other)
    {
        return other instanceof BoolValue;
    }

    @Override
    public IValue clone() {
        return new BoolValue(value);
    }

    @Override
    public String toString()
    {
        return Boolean.toString(value);
    }
}
