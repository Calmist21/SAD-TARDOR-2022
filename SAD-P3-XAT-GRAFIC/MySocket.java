
import java.io.*;
import java.net.Socket;

public class MySocket extends Socket {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public MySocket(String userName, String host, int port) {
        try {
            this.socket = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            printLine(userName);

        } catch (IOException e) {
            System.err.println("No se ha podido crear Socket");
            e.printStackTrace();
        }
    }


    public String readLine() throws IOException {
        String string = null;
        
        string = input.readLine();
        
        return string;
    }

    public void printLine(String string) {
        output.println(string);
        output.flush();
    }

    @Override
    public void close() {
        try {
            printLine("EXIT");
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("No se ha podido cerrar el Socket");
        }
    }
}