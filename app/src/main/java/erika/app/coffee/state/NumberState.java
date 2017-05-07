package erika.app.coffee.state;

import erika.app.coffee.model.InputNumberMode;

public class NumberState implements Cloneable {
    @FunctionalInterface
    public interface Action {
        void apply(double value);
    }
    public InputNumberMode mode = InputNumberMode.INTEGER;
    public String value = "0";
    public Action action;
}
