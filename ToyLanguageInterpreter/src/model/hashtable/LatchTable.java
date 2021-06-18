package model.hashtable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LatchTable implements IHeapTable<Integer>{
    private int nextAddress;
    private final Map<Integer, Integer> table;

    public LatchTable()
    {
        table = new HashMap<>();
        nextAddress = 1;
    }


    @Override
    public Map<Integer, String> getFormattedHeap()
    {
        return table.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
    }

    @Override
    public synchronized Integer get(Integer address)
    {
        return table.get(address);
    }

    @Override
    public synchronized void remove(Integer address)
    {
        table.remove(address);
    }

    @Override
    public int insert(Integer value)
    {
        table.put(nextAddress, value);
        return nextAddress++;
    }

    @Override
    public Set<Map.Entry<Integer, Integer>> entrySet()
    {
        return table.entrySet();
    }

    @Override
    public synchronized void set(int address, Integer value)
    {
        table.put(address, value);
    }

    @Override
    public synchronized void setContent(Map<Integer, Integer> table)
    {
        this.table.clear();
        this.table.putAll(table);
    }

    @Override
    public String toString()
    {
        StringBuilder text = new StringBuilder();
        for (HashMap.Entry<Integer, Integer> entry : this.table.entrySet()) {
            text.append(entry.getKey().toString()).append(" -> ").append(entry.getValue().toString()).append("; ");
        }

        return text.toString();
    }
}
