package model.types;

import model.values.IValue;
import model.values.ReferenceValue;

public class ReferenceType implements  IType{
    IType innerType;

    public ReferenceType(IType innerType)
    {
        this.innerType = innerType;
    }

    public IType getInnerType()
    {
        return innerType;
    }

    @Override
    public IValue defaultValue()
    {
        return new ReferenceValue(0, innerType);
    }

    @Override
    public boolean equals(Object other)
    {
        if(other instanceof ReferenceType)
            return innerType.equals(((ReferenceType) other).getInnerType());

        return false;
    }

    @Override
    public String toString()
    {
        return "ref(" + innerType.toString() + ")";
    }
}
