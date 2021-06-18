package model.hashtable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FileTable<K, V> implements IHashTable<K, V> {
    private final Map<K, V> table;

    public FileTable(){
        this.table = new HashMap<>();
    }


    @Override
    public Map<K, String> getFormattedTable() {
        return table.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return table.entrySet();
    }

    @Override
    public void remove(K key)
    {
        table.remove(key);
    }

    @Override
    public void insert(K key, V value) {
        table.put(key, value);
    }

    @Override
    public V get(K key) {
        return table.get(key);
    }

    @Override
    public Collection<V> getValues()
    {
        return table.values();
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (HashMap.Entry<K, V> entry : this.table.entrySet()) {
            text.append(entry.getKey().toString()).append(" -> ").append(entry.getValue().toString()).append("; ");
        }

        return text.toString();
    }
}
