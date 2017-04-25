package erika.redux.demo.application;

import erika.core.redux.CloneableObject;

public class BaseState implements CloneableObject {
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
