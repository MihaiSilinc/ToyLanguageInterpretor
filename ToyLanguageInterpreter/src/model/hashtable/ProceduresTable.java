package model.hashtable;

import model.procedures.Procedure;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProceduresTable implements IHashTable<String, Procedure>{
    Map<String, Procedure> table;

    public ProceduresTable()
    {
        this.table = new HashMap<>();
    }

    @Override
    public Map<String, String> getFormattedTable() {
        return table.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getParameters().toString()));
    }

    @Override
    public Procedure get(String key) {
        return table.get(key);
    }

    @Override
    public void remove(String key) {
        table.remove(key);
    }

    @Override
    public void insert(String key, Procedure value) {
        table.put(key, value);
    }

    @Override
    public Set<Map.Entry<String, Procedure>> entrySet() {
        return table.entrySet();
    }

    @Override
    public Collection<Procedure> getValues() {
        return table.values();
    }


    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (HashMap.Entry<String, Procedure> entry : this.table.entrySet()) {
            text.append(entry.getKey().toString()).append(" -> ").append(entry.getValue().getParameters().toString()).append("; ");
        }

        return text.toString();
    }
}
