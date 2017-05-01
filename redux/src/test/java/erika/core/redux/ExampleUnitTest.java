package erika.redux;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

class X implements Cloneable {
    ArrayList<Y> y;

    @Override
    public X clone() throws CloneNotSupportedException {
        return (X) super.clone();
    }
}

class Y implements Cloneable {
    Z z;
}

class Z {
    String me = "adsd";
}

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        X x = new X();
        x.y = new ArrayList<>();
        x.y.add(new Y());
        x.y.get(0).z = new Z();
        X clone = x.clone();
        assertTrue(x.y == clone.y);
    }
}