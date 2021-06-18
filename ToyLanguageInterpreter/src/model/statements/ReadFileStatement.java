package model.statements;

import exception.*;
import model.ProgramState;
import model.expressions.IExpression;
import model.hashtable.IHashTable;
import model.hashtable.IHeapTable;
import model.procedures.Procedure;
import model.types.IType;
import model.types.IntType;
import model.types.StringType;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadFileStatement implements IStatement {
    IExpression expression;
    String id;

    public ReadFileStatement(IExpression expression, String id)
    {
        this.expression = expression;
        this.id = id;
    }

    @Override
    public ProgramState execute(ProgramState state) throws IOException {
        IHashTable<String, IValue> symbolsTable = state.getSymbolTable();
        IHeapTable heap = state.getHeapTable();
        IValue value = symbolsTable.get(id);
        if(value != null)
        {
            IType type = value.getType();
            if(type.equals(new IntType()))
            {
                IValue expressionValue = expression.evaluate(symbolsTable, heap);
                IType expressionType = expressionValue.getType();
                if(expressionType.equals(new StringType()))
                {
                    StringValue filename = (StringValue)expressionValue;
                    IHashTable<StringValue, BufferedReader> fileTable = state.getFileTable();
                    BufferedReader reader = fileTable.get(filename);

                    IValue readValue;
                    if(reader != null)
                    {
                        String line = reader.readLine();

                        if(line == null) // file is empty
                        {
                            readValue = new IntValue(0);
                        }
                        else
                        {
                            int intValue = Integer.parseInt(line);
                            readValue = new IntValue(intValue);
                        }

                        symbolsTable.insert(id, readValue);
                    }
                    else
                        throw new FileNotFoundException("The file " + filename.toString() + " cannot be found!");

                }
                else
                    throw new TypeNotMatch("The expression " + expression.toString() +
                            " does not evaluate to a StringValue ( " + expressionType.toString() + ")!");
            }
            else
                throw new TypeNotMatch("The variable " + id + " is not an integer ( " + type.toString() + ")!");
        }
        else
            throw new InexistentVariable("The variable " + id + " does not exist!");
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        IType variableType = typeEnvironment.get(id);
        if (variableType.equals(new IntType())) {
            IType expressionType = expression.typeCheck(typeEnvironment);
            if(expressionType.equals(new StringType()))
            {
                return typeEnvironment;
            }
            throw new TypeCheckerException("ReadFileStatement: expression shall evaluate of StringType. It is " + expressionType.toString() );
        }
        else
            throw new TypeCheckerException("ReadFileStatement: variable shall be of intType. It is " + variableType.toString() );
    }

    @Override
    public String toString()
    {
        return "read( " + expression.toString() + ", " + id + " ); ";
    }
}
