package model.expressions;

import exception.TypeCheckerException;
import model.hashtable.IHashTable;
import model.hashtable.IHeapTable;
import model.types.IType;
import model.values.IValue;

public interface IExpression {
    IValue evaluate(IHashTable<String, IValue> table, IHeapTable<IValue> heap);
    IType typeCheck(IHashTable<String, IType> typeEnvironment) throws TypeCheckerException;
}
