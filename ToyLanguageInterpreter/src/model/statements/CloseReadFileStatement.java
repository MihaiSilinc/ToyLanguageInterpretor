package model.statements;

import exception.*;
import model.ProgramState;
import model.expressions.IExpression;
import model.hashtable.IHashTable;
import model.procedures.Procedure;
import model.types.IType;
import model.types.StringType;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CloseReadFileStatement implements IStatement{
    IExpression expression;

    public CloseReadFileStatement(IExpression expression)
    {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws IOException {
        IValue value = expression.evaluate(state.getSymbolTable(), state.getHeapTable());
        IType type = value.getType();
        if(type.equals(new StringType()))
        {
            IHashTable<StringValue, BufferedReader> fileTable = state.getFileTable();

            StringValue stringValue = (StringValue)value;
            BufferedReader reader = fileTable.get(stringValue);
            if (reader != null)
            {
                reader.close();
                fileTable.remove(stringValue);
            }
            else
                throw new FileNotFoundException("The file " + stringValue.toString() + " cannot be found!");
        }
        else
            throw new TypeNotMatch("The filename shall be a String!");

        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        IType type = expression.typeCheck(typeEnvironment);
        if(type.equals(new StringType()))
        {
            return  typeEnvironment;
        }
        throw new TypeCheckerException("The filename shall be a String!");
    }

    @Override
    public String toString()
    {
        return "close( " + expression.toString() + " ); ";
    }
}
