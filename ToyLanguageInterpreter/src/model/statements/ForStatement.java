package model.statements;

import exception.TypeCheckerException;
import model.ProgramState;
import model.expressions.IExpression;
import model.expressions.RelationalExpression;
import model.expressions.VariableExpression;
import model.hashtable.IHashTable;
import model.hashtable.TypeEnvironment;
import model.procedures.Procedure;
import model.types.IType;
import model.types.IntType;

import java.io.IOException;

public class ForStatement implements IStatement{
    String variableId;
    IExpression expression1, expression2, expression3;
    IStatement statement;

    public ForStatement(String variableId, IExpression expression1, IExpression expression2, IExpression expression3, IStatement statement) {
        this.variableId = variableId;
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws IOException {

        IStatement forStatement = new WhileStatement(new RelationalExpression(new VariableExpression(variableId),
            expression2, "<"), new CompoundStatement(statement, new AssignStatement(variableId,
            expression3)));
        state.getStack().push(forStatement);
        state.getStack().push(new AssignStatement(variableId, expression1));
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        IType expression1Type = expression1.typeCheck(typeEnvironment);
        if(expression1Type.equals(new IntType()))
        {
            IType expression2Type = expression2.typeCheck(typeEnvironment);
            if(expression2Type.equals(new IntType())) {
                IType expression3Type = expression3.typeCheck(typeEnvironment);
                if(expression3Type.equals(new IntType())) {
                    typeEnvironment.insert(variableId, new IntType());
                    statement.typeCheck(new TypeEnvironment((TypeEnvironment)typeEnvironment), proceduresTable);
                    return typeEnvironment;
                }
                else
                    throw new TypeCheckerException("ForStatement: You cannot assign an expressions of type other then IntType");
            }
            else
                throw new TypeCheckerException("ForStatement: You cannot assign an expressions of type other then IntType");
        }
        else
            throw new TypeCheckerException("ForStatement: You cannot assign an expressions of type other then IntType");

    }

    @Override
    public String toString() {
        return "for(" + variableId + " = " + expression1.toString() + "; " + variableId + " < " + expression2.toString()+
                "; " + variableId + " = " + expression3.toString() + ") { " + statement.toString() + " }; ";
    }
}
