package model.expressions;

import exception.*;
import model.hashtable.IHashTable;
import model.hashtable.IHeapTable;
import model.types.BoolType;
import model.types.IType;
import model.types.IntType;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;

public class RelationalExpression implements IExpression {
    private final IExpression expressionLHS;
    private final IExpression expressionRHS;
    private enum Operator{lt, lte, gt , gte, equals, not_equals}
    private RelationalExpression.Operator operator;

    public RelationalExpression(IExpression expressionLHS, IExpression expressionRHS, String operator)
    {
        this.expressionLHS = expressionLHS;
        this.expressionRHS = expressionRHS;
        switch (operator) {
            case ("<=") -> this.operator = RelationalExpression.Operator.lte;
            case ("<") -> this.operator = RelationalExpression.Operator.lt;
            case (">") -> this.operator = RelationalExpression.Operator.gt;
            case (">=") -> this.operator = RelationalExpression.Operator.gte;
            case ("==") -> this.operator = RelationalExpression.Operator.equals;
            case ("!=") -> this.operator = RelationalExpression.Operator.not_equals;
        }
    }

    private boolean compare(int numberLHS, int numberRHS) throws ProgramException
    {
        switch (operator)
        {
            case lt -> { return numberLHS < numberRHS;}
            case lte -> { return numberLHS <= numberRHS;}
            case gt -> { return numberLHS > numberRHS;}
            case gte -> { return numberLHS >= numberRHS;}
            case equals -> { return numberLHS == numberRHS;}
            case not_equals -> { return numberLHS != numberRHS;}
        }
        throw new ProgramException();
    }

    @Override
    public IValue evaluate(IHashTable<String, IValue> table, IHeapTable<IValue> heap) {
        IValue valueLHS = expressionLHS.evaluate(table, heap);
        IType typeLHS = valueLHS.getType();
        if(typeLHS.equals(new IntType()))
        {
            IValue valueRHS = expressionRHS.evaluate(table, heap);
            IType typeRHS = valueRHS.getType();
            if(typeRHS.equals(new IntType()))
            {
                IntValue intLHS = (IntValue)valueLHS;
                IntValue intRHS = (IntValue)valueRHS;

                int intValueLHS = intLHS.getValue();
                int intValueRHS = intRHS.getValue();

                boolean result = compare(intValueLHS, intValueRHS);

                return new BoolValue(result);
            }
            throw new TypeNotMatch("Right hand side expression is not an integer! (" + typeRHS.toString() + ") ");
        }
        throw new TypeNotMatch("Left hand side expression is not an integer! (" + typeLHS.toString() + ") ");
    }

    @Override
    public IType typeCheck(IHashTable<String, IType> typeEnvironment) throws TypeCheckerException {
        IType typeLHS = expressionLHS.typeCheck(typeEnvironment);
        IType typeRHS = expressionRHS.typeCheck(typeEnvironment);

        if(typeLHS.equals(new IntType()))
        {
            if(typeRHS.equals(new IntType()))
            {
                return new BoolType();
            }
            else
                throw new TypeCheckerException("Second operand does not evaluate to IntType. It is " + typeRHS.toString());
        }
        throw new TypeCheckerException("First operand does not evaluate to IntType. It is " + typeLHS.toString());

    }

    @Override
    public String toString()
    {
        String text = "( ";
        text += expressionLHS.toString();
        switch (operator)
        {
            case lt -> text += " < ";
            case lte -> text += " <= ";
            case gt -> text += " > ";
            case gte -> text += " >= ";
            case equals -> text += " == ";
            case not_equals -> text += " != ";
        }
        text += expressionRHS.toString() + ")";
        return text;
    }
}
