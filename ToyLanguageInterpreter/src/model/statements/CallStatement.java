package model.statements;

import exception.CallingProcedureException;
import exception.InexistentProcedure;
import exception.TypeCheckerException;
import model.ProgramState;
import model.expressions.IExpression;
import model.hashtable.IHashTable;
import model.hashtable.SymbolsTable;
import model.procedures.Procedure;
import model.stack.IExecutionStack;
import model.types.IType;
import model.values.IValue;

import java.util.Arrays;
import java.util.List;

public class CallStatement implements IStatement{
    String procedureId;
    List<IExpression> values;

    public CallStatement(String procedureId, IExpression ... values) {
        this.procedureId = procedureId;
        this.values = Arrays.asList(values.clone());
    }

    @Override
    public ProgramState execute(ProgramState state) {
        Procedure procedure = state.getProceduresTable().get(procedureId);
        if(procedure == null)
            throw new InexistentProcedure("The procedure " + procedureId + " is not declared!");
        if(procedure.getParameters().size() != values.size())
            throw new CallingProcedureException("The procedure needs " + procedure.getParameters().size() + " parameters." + values.size() + " provided.");

        IHashTable<String, IValue> newSymbolsTable = new SymbolsTable<>();
        for(int i = 0; i <  values.size(); i++)
        {
            VariableDeclarationStatement parameter = procedure.getParameters().get(i);
            IValue value = values.get(i).evaluate(state.getSymbolTable(), state.getHeapTable());
            if(value.getType().equals(parameter.getType()) && newSymbolsTable.get(parameter.getId()) == null)
                newSymbolsTable.insert(parameter.getId(), value);
            else
                throw new CallingProcedureException("Parameters don't match!");
        }
        state.pushSymbolsStack(newSymbolsTable);

        IExecutionStack<IStatement> stack = state.getStack();
        stack.push(new LeaveScopeStatement());
        stack.push(procedure.getBody());

        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException{

        if(proceduresTable.get(procedureId) == null)
            throw new TypeCheckerException("Procedure " + procedureId + " does not exist");

        List<VariableDeclarationStatement> parameters = proceduresTable.get(procedureId).getParameters();
        for(int i = 0; i < parameters.size(); i ++)
        {
            if(!parameters.get(i).getType().equals(values.get(i).typeCheck(typeEnvironment)))
                throw new TypeCheckerException("Parameters to procedure " + procedureId + " don't match!");
        }

        return typeEnvironment;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(procedureId).append("( ");
        for(var value : values)
            s.append(value).append(", ");
        s.delete(s.length()-1, s.length());
        s.append(")");
        return s.toString();
    }
}
