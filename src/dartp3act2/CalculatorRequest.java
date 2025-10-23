package dartp3act2;

import java.io.Serializable;

public class CalculatorRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    public enum Op { ADD, SUB, MUL, DIV, POW, MOD }

    private Op operation;
    private double a;
    private double b;

    public CalculatorRequest(Op operation, double a, double b) {
        this.operation = operation;
        this.a = a;
        this.b = b;
    }

    public Op getOperation() { return operation; }
    public double getA() { return a; }
    public double getB() { return b; }
}

