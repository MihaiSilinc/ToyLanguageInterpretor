package model.expressions;

import exception.*;
import model.hashtable.IHashTable;
import model.hashtable.IHeapTable;
import model.types.BoolType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;

public class LogicalExpression implements IExpression{
    private final IExpression expressionLHS;
    private final IExpression expressionRHS;
    private enum Operator{and, or, equals, not_equals}
    private LogicalExpression.Operator operator;

    public LogicalExpression(IExpression expressionLHS, IExpression expressionRHS, String operator)
    {
        this.expressionLHS = expressionLHS;
        this.expressionRHS = expressionRHS;
        switch (operator) {
            case ("and") -> this.operator = Operator.and;
            case ("or") -> this.operator = Operator.or;
            case ("==") -> this.operator = Operator.equals;
            case ("!=") -> this.operator = Operator.not_equals;
        }
    }

    private boolean LogicalEvaluation(boolean numberLHS, boolean numberRHS) throws ProgramException
    {
        switch (operator)
        {
            case and -> { return numberLHS && numberRHS;}
            case or -> { return numberLHS || numberRHS;}
            case equals -> { return numberLHS == numberRHS;}
            case not_equals -> { return numberLHS != numberRHS;}
        }
        throw new ProgramException();
    }

    @Override
    public IValue evaluate(IHashTable<String, IValue> table, IHeapTable<IValue> heap) {
        IValue valueLHS = expressionLHS.evaluate(table, heap);
        IType typeLHS = valueLHS.getType();
        if(typeLHS.equals(new BoolType()))
        {
            IValue valueRHS = expressionRHS.evaluate(table, heap);
            IType typeRHS = valueRHS.getType();
            if(typeRHS.equals(new BoolType()))
            {
                BoolValue booleanLHS = (BoolValue)valueLHS;
                BoolValue booleanRHS = (BoolValue)valueRHS;

                boolean booleanValueLHS = booleanLHS.getValue();
                boolean booleanValueRHS = booleanRHS.getValue();

                boolean result = LogicalEvaluation(booleanValueLHS, booleanValueRHS);

                return new BoolValue(result);
            }
            throw new TypeNotMatch("Right hand side expression is not an integer! (" + typeRHS.toString() + ") ");
        }
        throw new TypeNotMatch("Right hand side expression is not an integer! (" + typeLHS.toString() + ") ");
    }

    @Override
    public IType typeCheck(IHashTable<String, IType> typeEnvironment) throws TypeCheckerException {
        IType typeLHS = expressionLHS.typeCheck(typeEnvironment);
        IType typeRHS = expressionRHS.typeCheck(typeEnvironment);

        if(typeLHS.equals(new BoolType()))
        {
            if(typeRHS.equals(new BoolType()))
            {
                return new BoolType();
            }
            else
                throw new TypeCheckerException("Second operand does not evaluate to BoolType. It is " + typeRHS.toString());
        }
        throw new TypeCheckerException("First operand does not evaluate to BoolType. It is " + typeLHS.toString());

    }

    @Override
    public String toString()
    {
        String text = "( ";
        text += expressionLHS.toString();
        switch (operator)
        {
            case and -> text += " and ";
            case or -> text += " or ";
            case equals -> text += " == ";
            case not_equals -> text += " != ";
        }
        text += expressionRHS.toString() + ")";
        return text;
    }
}
