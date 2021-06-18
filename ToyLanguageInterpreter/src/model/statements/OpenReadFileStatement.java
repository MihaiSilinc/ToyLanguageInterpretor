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
import java.io.FileReader;
import java.io.IOException;

public class OpenReadFileStatement implements IStatement {
    IExpression expression;

    public OpenReadFileStatement(IExpression expression)
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
            if (reader == null)
            {
                String filename = stringValue.getValue();
                reader = new BufferedReader(new FileReader(filename));
                fileTable.insert(stringValue, reader);
            }
            else
                throw new ExistingVariable("A file named " + stringValue + " already exists!");
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
        return "openRead( " + expression.toString() + " ); ";
    }
}
