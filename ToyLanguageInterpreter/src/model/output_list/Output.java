package model.output_list;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Output<T> implements IOutput<T> {
    List<T> list;

    public Output()
    {
        this.list = new ArrayList<>();
    }

    @Override
    public List<String> getOutput() {
        return list.stream().map(Object::toString).collect(Collectors.toList());
    }

    @Override
    public void push_back(T message)
    {
        list.add(message);
    }

    @Override
    public String toString()
    {
        StringBuilder text = new StringBuilder();
        for(T element : list)
        {
            text.append(element.toString()).append("\n");
        }
        return text.toString();
    }
}
