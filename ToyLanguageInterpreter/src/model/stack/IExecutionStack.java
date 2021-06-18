package model.stack;

import java.util.List;

public interface IExecutionStack<T> {
    T pop();
    void push(T value);
    boolean isEmpty();
    void clear();
    T peek();
    List<String> getStack();
}
