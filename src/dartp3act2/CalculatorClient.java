package dartp3act2;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class CalculatorClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;
        if (args.length >= 1) host = args[0];
        if (args.length >= 2) {
            try { port = Integer.parseInt(args[1]); } catch (NumberFormatException ignored) {}
        }

        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to server " + host + ":" + port);
            while (true) {
                System.out.print("Enter operation (ADD SUB MUL DIV POW MOD) or QUIT: ");
                String opStr = scanner.next().trim().toUpperCase();
                if (opStr.equals("QUIT")) break;
                CalculatorRequest.Op op;
                try {
                    op = CalculatorRequest.Op.valueOf(opStr);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid operation. Try again.");
                    continue;
                }
                System.out.print("a = ");
                double a = scanner.nextDouble();
                System.out.print("b = ");
                double b = scanner.nextDouble();

                CalculatorRequest req = new CalculatorRequest(op, a, b);
                out.writeObject(req);
                out.flush();

                Object respObj = in.readObject();
                if (respObj instanceof CalculatorResponse) {
                    CalculatorResponse resp = (CalculatorResponse) respObj;
                    if (resp.isOk()) {
                        System.out.println("Result = " + resp.getResult());
                    } else {
                        System.out.println("Error: " + resp.getMessage());
                    }
                } else {
                    System.out.println("Invalid response from server.");
                }
            }
            System.out.println("Client closing.");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
