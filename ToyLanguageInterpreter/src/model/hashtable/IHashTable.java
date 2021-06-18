package model.hashtable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface IHashTable<K, V> {
    V get(K key);
    void remove(K key);
    void insert(K key, V value);
    Set<Map.Entry<K, V>> entrySet();
    Collection<V> getValues();

    Map<K, String> getFormattedTable();
}
