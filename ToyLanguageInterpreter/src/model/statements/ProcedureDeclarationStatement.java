package model.statements;

import exception.TypeCheckerException;
import model.ProgramState;
import model.hashtable.IHashTable;
import model.hashtable.TypeEnvironment;
import model.procedures.Procedure;
import model.types.IType;

import java.io.IOException;

public class ProcedureDeclarationStatement implements IStatement{
    Procedure procedure;

    public ProcedureDeclarationStatement(Procedure procedure)
    {
        this.procedure = procedure;
    }

    @Override
    public ProgramState execute(ProgramState state) throws IOException {
        state.getProceduresTable().insert(procedure.getId(), procedure);
        return null;
    }

    @Override
    public IHashTable<String, IType> typeCheck(IHashTable<String, IType> typeEnvironment, IHashTable<String, Procedure> proceduresTable) throws TypeCheckerException {
        if(proceduresTable.get(procedure.getId()) != null)
            throw new TypeCheckerException("Procedure " + procedure.getId() + " is already declared!");

        proceduresTable.insert(procedure.getId(), procedure);

        TypeEnvironment newTypeEnv = new TypeEnvironment();
        for(var param : procedure.getParameters())
            newTypeEnv.insert(param.getId(), param.getType());
        procedure.getBody().typeCheck(new TypeEnvironment(newTypeEnv), proceduresTable);
        return typeEnvironment;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("procedure ").append(procedure.getId()).append("( ");
        for(var parameter : procedure.getParameters())
            s.append(parameter.getId()).append(":").append(parameter.getType()).append(", ");
        s.delete(s.length()-1, s.length());
        s.append(")");
        s.append("{ ").append(procedure.getBody()).append(" }");
        return s.toString();
    }
}
