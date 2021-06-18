package model.hashtable;

import model.types.IType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TypeEnvironment implements IHashTable<String, IType>{
    private final Map<String, IType> table;

    public TypeEnvironment()
    {
        table = new HashMap<>();
    }

    public TypeEnvironment(TypeEnvironment typeEnvironment)
    {
        table = new HashMap<>(typeEnvironment.table);
    }

    @Override
    public Map<String, String> getFormattedTable() {
        return null;
    }

    @Override
    public IType get(String key) {
        return table.get(key);
    }

    @Override
    public void remove(String key) {
        table.remove(key);
    }

    @Override
    public void insert(String key, IType value) {
        table.put(key, value);
    }

    @Override
    public Set<Map.Entry<String, IType>> entrySet() {
        return table.entrySet();
    }

    @Override
    public Collection<IType> getValues() {
        return table.values();
    }
}
