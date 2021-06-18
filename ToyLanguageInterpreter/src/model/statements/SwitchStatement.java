package model.statements;

import exception.SwitchException;
import exception.TypeCheckerException;
import exception.TypeNotMatch;
import model.ProgramState;
import model.expressions.IExpression;
import model.expressions.LogicalExpression;
import model.expressions.RelationalExpression;
import model.hashtable.IHashTable;
import model.procedures.Procedure;
import model.types.BoolType;
import model.types.IType;
import model.values.IValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SwitchStatement implements IStatement{
    IExpression expression;
    IExpression expression1;
    IStatement statement1;
    IExpression expression2;
    IStatement statement2;
    IStatement defaultStatement;

    public SwitchStatement(IExpression expression, IExpression expression1, IStatement statement1, IExpression expression2, IStatement statement2, IStatement defaultStatement) {
        this.expression = expression;
        this.expression1 = expression1;
        this.statement1 = statement1;
        this.expression2 = expression2;
        this.statement2 = statement2;
        this.defaultStatement = defaultStatement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws IOException {
        if(expression1 == null || expression2 == null || expression == null || statement1 == null || statement2 == null || defaultStatement == null)
            throw new SwitchException("Switch should have 3 expressions and 3 statements");
        IStatement statement = new IfStatement(new RelationalExpression(expression, expression1, "=="),statement1,
                new IfStatement(new RelationalExpression(expression, expression2, "=="), statement2, defaultStatement));

        state.getStack().push(statement);
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "switch(" + expression.toString() + ") { case(" + expression1 + "): { " + statement1 + " }; case("
                + expression2 + "): { " + statement2 + " }; default { " + defaultStatement + "} }; ";
    }
}
