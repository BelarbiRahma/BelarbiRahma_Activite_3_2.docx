package dartp3act2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler implements Runnable {
    private Socket socket;
    private AtomicInteger totalOperations;

    public ClientHandler(Socket socket, AtomicInteger totalOperations) {
        this.socket = socket;
        this.totalOperations = totalOperations;
    }

    @Override
    public void run() {
        try (
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {
            Object obj;
            while ((obj = in.readObject()) != null) {
                if (!(obj instanceof CalculatorRequest)) {
                    out.writeObject(new CalculatorResponse(false, "Invalid request type", 0));
                    out.flush();
                    continue;
                }
                CalculatorRequest req = (CalculatorRequest) obj;
                CalculatorResponse resp = process(req);
                out.writeObject(resp);
                out.flush();
            }
        } catch (java.io.EOFException eof) {
        } catch (Exception e) {
            System.err.println("Handler exception for " + socket.getRemoteSocketAddress() + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (Exception ignored) {}
        }
    }

    private CalculatorResponse process(CalculatorRequest req) {
        double a = req.getA();
        double b = req.getB();
        double res = 0;
        String msg = "OK";
        try {
            switch (req.getOperation()) {
                case ADD: res = a + b; break;
                case SUB: res = a - b; break;
                case MUL: res = a * b; break;
                case DIV:
                    if (b == 0) throw new ArithmeticException("Division by zero");
                    res = a / b; break;
                case POW: res = Math.pow(a, b); break;
                case MOD:
                    if (b == 0) throw new ArithmeticException("Modulo by zero");
                    res = a % b; break;
                default: throw new UnsupportedOperationException("Unknown operation");
            }
            int current = totalOperations.incrementAndGet();
            System.out.println("Processed operation: " + req.getOperation() +
                               " (" + a + ", " + b + "). Total operations: " + current);
            return new CalculatorResponse(true, msg, res);
        } catch (Exception e) {
            return new CalculatorResponse(false, e.getMessage(), 0);
        }
    }
}

