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

public class NewStatement implements IStatement{
    String id;
    IExpression expression;

    public NewStatement(String id, IExpression expression)
    {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        IHashTable<String, IValue> table = state.getSymbolTable();
        IHeapTable heap = state.getHeapTable();

        IValue value = table.get(id);
        IType type = value.getType();

        if(type instanceof ReferenceType)
        {
            IValue expressionValue = expression.evaluate(table, heap);
            IType expressionType = expressionValue.getType();
            if(expressionType.equals(((ReferenceType) type).getInnerType()))
            {
                int address = heap.insert(expressionValue);
                table.insert(id, new ReferenceValue(address, ((ReferenceType) type).getInnerType()));//(ReferenceValue)value;
                //referencedValue.setAddress(address);
            }
            else
                throw new TypeNotMatch("The expression in a new statement shall evaluate to a value of the same type as the inner type of the variable that references it! It is " + type.toString() + ".\n");
        }
        else
            throw new TypeNotMatch("The variable in a new statement shall evaluate to a value of type ReferenceType! It is " + type.toString() + ".\n");

        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        IType variableType = typeEnvironment.get(id);
        IType expressionType = expression.typeCheck(typeEnvironment);
        if (variableType.equals(new ReferenceType(expressionType)))
            return typeEnvironment;
        else
            throw new TypeCheckerException("Incompatible types: " + variableType.toString() + " and " + expressionType.toString());
    }

    @Override
    public String toString()
    {
        return "new(" + id + ", " + expression.toString() + "); ";
    }
}
