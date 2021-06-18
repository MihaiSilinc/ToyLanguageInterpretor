package model.output_list;

import java.util.List;

public interface IOutput<T> {

    void push_back(T message);

    List<String> getOutput();
}
