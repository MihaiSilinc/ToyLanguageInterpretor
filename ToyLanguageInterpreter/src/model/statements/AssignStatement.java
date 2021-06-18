package model.statements;

import exception.*;
import model.ProgramState;
import model.expressions.IExpression;
import model.hashtable.IHashTable;
import model.hashtable.IHeapTable;
import model.procedures.Procedure;
import model.types.IType;
import model.values.IValue;

public class AssignStatement implements IStatement{
    private final String id;
    private final IExpression expression;

    public AssignStatement(String id, IExpression expression)
    {
        this.expression = expression;
        this.id = id;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        IHashTable<String, IValue> table = state.getSymbolTable();
        IHeapTable heap = state.getHeapTable();
        IValue value = table.get(id);
        if(value != null)
        {
            IValue expressionValue = expression.evaluate(table, heap);
            IType expressionType = expressionValue.getType();
            IType idType = value.getType();
            if(expressionType.equals(idType))
                table.insert(id, expressionValue);
            else
                throw new TypeNotMatch("Type of expression (" + expressionType.toString() + ") " +
                        "and type of variable (" + idType.toString() + ") do not match!");
        }
        else throw new VariableNotDeclared("Variable was not declared!");
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        IType variableType = typeEnvironment.get(id);
        IType expressionType = expression.typeCheck(typeEnvironment);

        if(variableType.equals(expressionType))
        {
            return typeEnvironment;
        }
        throw new TypeCheckerException("AssignStatement: You cannot assign an expression of type " + expressionType.toString()
                                        + " to a variable of type " + variableType.toString());
    }

    @Override
    public String toString()
    {
        return id + " = " + expression.toString() + "; ";
    }

}
