package model.values;

import model.types.IType;
import model.types.IntType;

public class IntValue implements IValue {

    private final int value;

    public IntValue(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    @Override
    public IType getType()
    {
        return new IntType();
    }

    @Override
    public boolean equals(Object other)
    {
        return other instanceof IntValue;
    }

    @Override
    public IValue clone() {
        return new IntValue(value);
    }

    @Override
    public String toString()
    {
        return Integer.toString(value);
    }
}
