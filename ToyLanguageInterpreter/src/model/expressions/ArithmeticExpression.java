package model.expressions;

import exception.*;
import model.hashtable.IHashTable;
import model.hashtable.IHeapTable;
import model.types.IType;
import model.types.IntType;
import model.values.IValue;
import model.values.IntValue;

public class ArithmeticExpression implements IExpression{
    private final IExpression expressionLHS;
    private final IExpression expressionRHS;
    private enum Operator{addition, subtraction, division, multiplication}
    private Operator operator;

    public ArithmeticExpression(char operator, IExpression expressionLHS, IExpression expressionRHS)
    {
        this.expressionLHS = expressionLHS;
        this.expressionRHS = expressionRHS;
        switch (operator) {
            case ('+') -> this.operator = Operator.addition;
            case ('-') -> this.operator = Operator.subtraction;
            case ('*') -> this.operator = Operator.multiplication;
            case ('/') -> this.operator = Operator.division;
        }
    }

    private int ArithmeticEvaluation(int numberLHS, int numberRHS) throws DivisionByZero, ProgramException
    {
        switch (operator)
        {
            case addition -> { return numberLHS + numberRHS;}
            case subtraction -> { return numberLHS - numberRHS;}
            case multiplication -> { return numberLHS * numberRHS;}
            case division -> {
                if(numberRHS == 0)
                    throw new DivisionByZero();
                return numberLHS / numberRHS;}
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
                IntValue integerLHS = (IntValue)valueLHS;
                IntValue integerRHS = (IntValue)valueRHS;

                int numberLHS = integerLHS.getValue();
                int numberRHS = integerRHS.getValue();

                int result = ArithmeticEvaluation(numberLHS, numberRHS);

                return new IntValue(result);
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
                return new IntType();
            }
            else
                throw new TypeCheckerException("Second operand does not evaluate to IntType. It is " + typeRHS.toString());
        }
        throw new TypeCheckerException("First operand does not evaluate to IntType. It is " + typeLHS.toString());

    }

    @Override
    public String toString()
    {
        String text = "";
        text += expressionLHS.toString();
        switch (operator) {
            case addition -> text += " + ";
            case subtraction -> text += " - ";
            case multiplication -> text += " * ";
            case division -> text += " / ";
        }
        text += expressionRHS.toString();

        return text;
    }
}
