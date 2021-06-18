package model.values;

import model.types.IType;
import model.types.StringType;

public class StringValue implements IValue{
    private final String value;

    public StringValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    @Override
    public IType getType()
    {
        return new StringType();
    }

    @Override
    public boolean equals(Object other)
    {
        return other instanceof StringValue;
    }

    @Override
    public IValue clone() {
        return new StringValue(value);
    }

    @Override
    public String toString()
    {
        return "\"" + value + "\"";
    }
}
