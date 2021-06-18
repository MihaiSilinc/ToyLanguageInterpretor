package model.values;

import model.types.IType;
import model.types.ReferenceType;

public class ReferenceValue implements IValue{
    private int address;
    private final IType locationType;

    public ReferenceValue(IType locationType)
    {
        this.address = 0;
        this.locationType = locationType;
    }

    public ReferenceValue(int address, IType locationType)
    {
        this.address = address;
        this.locationType = locationType;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getAddress() {
        return address;
    }

    public IType getLocationType()
    {
        return locationType;
    }

    @Override
    public IType getType()
    {
        return new ReferenceType(locationType);
    }

    @Override
    public IValue clone() {
        return new ReferenceValue(address, locationType);
    }

    @Override
    public String toString()
    {
        if(address == 0)
            return "(null, " + locationType.toString() + ")";
        return "(" + address + ", " + locationType.toString() + ")";
    }
}
