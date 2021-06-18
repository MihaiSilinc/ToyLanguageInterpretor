package model.statements;

import exception.*;
import model.ProgramState;
import model.expressions.IExpression;
import model.hashtable.IHashTable;
import model.hashtable.IHeapTable;
import model.procedures.Procedure;
import model.types.IType;
import model.types.ReferenceType;
import model.values.IValue;
import model.values.ReferenceValue;

public class WriteHeapStatement implements IStatement{
    String id;
    IExpression expression;

    public WriteHeapStatement(String id, IExpression expression)
    {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        IHashTable<String, IValue> table = state.getSymbolTable();
        IHeapTable<IValue> heap = state.getHeapTable();

        IValue variableValue = table.get(id);
        if(variableValue != null)
        {
            IType variableType = variableValue.getType();
            if(variableType instanceof ReferenceType)
            {
                ReferenceValue referenceValue = (ReferenceValue) variableValue;
                int address = referenceValue.getAddress();
                IValue referencedValue = heap.get(address);
                if(referencedValue != null){
                    IValue value = expression.evaluate(table, heap);
                    IType type = value.getType();
                    if(type.equals(referenceValue.getLocationType())) {
                        heap.set(address, value);
                    }
                    else
                        throw new TypeNotMatch("In WriteHeapStatement ,the expression shall evaluate to a value " +
                                "of the same type as the type of the variable in the HeapTable!");
                }
                else
                    throw new UndefinedKey("The key '"+ address +"' is not defined in the HeapTable!");
            }
            else
                throw new TypeNotMatch("WriteHeapStatement needs a ReferenceType variable. You provided " + variableType.toString());
        }
        else
            throw new UndefinedKey("The variable '" + id + " is not defined in SymbolsTable!");

        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        IType variableType = typeEnvironment.get(id);
        IType expressionType = expression.typeCheck(typeEnvironment);
        if(variableType.equals(new ReferenceType(expressionType)))
        {
            return typeEnvironment;
        }
        throw new TypeCheckerException("Incompatible types: " + variableType.toString() + " and " + expressionType.toString());
    }

    @Override
    public String toString()
    {
        return "writeHeap(" + id + ", " + expression.toString() + "); ";
    }
}

