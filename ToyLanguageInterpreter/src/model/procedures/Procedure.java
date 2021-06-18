package model.procedures;

import model.statements.IStatement;
import model.statements.VariableDeclarationStatement;

import java.util.Arrays;
import java.util.List;

public class Procedure {
    private String id;
    private IStatement body;
    private List<VariableDeclarationStatement> parameters;

    public Procedure(String id, IStatement body, VariableDeclarationStatement ... parameters)
    {
        this.id = id;
        this.body = body;
        this.parameters = Arrays.asList(parameters.clone());
    }

    public String getId() {
        return id;
    }

    public IStatement getBody() {
        return body;
    }

    public List<VariableDeclarationStatement> getParameters() {
        return parameters;
    }
}
