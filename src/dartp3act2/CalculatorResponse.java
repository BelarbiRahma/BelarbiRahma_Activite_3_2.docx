package dartp3act2;

import java.io.Serializable;

public class CalculatorResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean ok;
    private String message;
    private double result;

    public CalculatorResponse(boolean ok, String message, double result) {
        this.ok = ok;
        this.message = message;
        this.result = result;
    }

    public boolean isOk() { return ok; }
    public String getMessage() { return message; }
    public double getResult() { return result; }
}
