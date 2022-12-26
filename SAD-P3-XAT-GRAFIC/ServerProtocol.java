
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;

//Ofrece un control del comportamiento del chat del cliente
public class ServerProtocol implements Runnable {
    static final int READING = 0, SENDING = 1;
    static final int CLIENT_CONNECTED = 0, CLIENT_DISCONNECTED = 1, CLIENT_MESSAGE = 2;
    static final int PROCESSING = 2;

    
    static Map<String, SocketChannel> clients = new TreeMap<>();
    static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    final SocketChannel socketChannel;
    final SelectionKey selectionKey;
    final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    ByteBuffer input = ByteBuffer.allocate(1024);
    int state = READING;
    boolean isFirstTime = true;
    String clientName = null;
    String clientMessage = null;

    ServerProtocol(Selector selector, SocketChannel sc) throws IOException {
        socketChannel = sc ;
        sc.configureBlocking(false);
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }
     boolean isNickUsed(String myNick) {
        boolean used = false;
        for (Map.Entry<String, SocketChannel> entry : clients.entrySet()) {
            if (entry.getKey().equals(myNick)) {
                used = true;
            }
        }
        return used;
    }
    @Override
    public void run() {
        try {
            if (state == READING) {
                read();
            } else if (state == SENDING) {
                sendManager(CLIENT_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void read() throws IOException {
        int readCount = socketChannel.read(input);
        if (readCount > 0) {
            state = PROCESSING;
            threadPool.execute(new Processor(readCount));
        }
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

   
    synchronized void process(int readCount) {
        readProcess(readCount);
        state = SENDING;
    }

    class Processor implements Runnable {
        int read;

        Processor(int readCount) {
            this.read = readCount;
        }

        public void run() {
            process(read);
        }
    }

    synchronized void readProcess(int readCount) {
        StringBuilder stringbuilder = new StringBuilder();
        input.flip();
        byte[] subStringBytes = new byte[readCount];
        byte[] array = input.array();
        System.arraycopy(array, 0, subStringBytes, 0, readCount);
        stringbuilder.append(new String(subStringBytes));
        input.clear();

        
        if (isFirstTime) {
            clientName = stringbuilder.toString().trim();
            if (isNickUsed(clientName)) {
                JOptionPane.showMessageDialog(null,"Usuario duplicado, pruebe con otro nickname","USUARIO DUPLICADO",JOptionPane.ERROR_MESSAGE);
                return;
            }
            clients.put(clientName, socketChannel);
            System.out.println(dateFormat.format(new Date()) + " [" + clientName + "]: se ha unido");
            try {
                sendManager(CLIENT_CONNECTED);
            } catch (IOException e) {
                e.printStackTrace();
            }
            isFirstTime = false;
        } else {
            clientMessage = stringbuilder.toString().trim();
            if (clientMessage.equals("EXIT")) {
                try {
                    sendManager(CLIENT_DISCONNECTED);
                    System.out.println(dateFormat.format(new Date()) + "[" + clientName + "] ha abandonado el chat");
                    socketChannel.close();
                    clients.remove(clientName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    void send(String text, SocketChannel sc) throws IOException {
        ByteBuffer output = ByteBuffer.wrap((text).getBytes());
        sc.write(output);
    }

    void sendManager(int clientOperation) throws IOException {
        String toOtherClients;
        switch (clientOperation) {
            case CLIENT_CONNECTED:
                toOtherClients = "[CLIENT CONNECTED] " + clientName + "\n";
                String toMyClient = "[HELLO CLIENT] Conectado a las " + dateFormat.format(new Date()) + "\n[CLIENT LIST]";
                //Se utiliza para ir actualizando la lista de clientes online
                for (Map.Entry<String, SocketChannel> entry : clients.entrySet()) {
                    toMyClient = toMyClient + ";" + entry.getKey();
                }
                send(toMyClient + "\n", socketChannel);

                for (Map.Entry<String, SocketChannel> entry : clients.entrySet()) {
                    if (entry.getKey() != clientName) {
                        send(toOtherClients, entry.getValue());
                    }
                }
                break;

            case CLIENT_DISCONNECTED:
                toOtherClients = "[CLIENT DISCONNECTED] " + clientName + "\n";

           
                for (Map.Entry<String, SocketChannel> entry : clients.entrySet()) {
                    if (entry.getKey() != clientName && clientMessage != null) {
                        send(toOtherClients, entry.getValue());
                    }
                }
                break;

            case CLIENT_MESSAGE:
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                toOtherClients = dateFormat.format(new Date()) + " [" + clientName + "]: " + clientMessage + "\n";
                for (Map.Entry<String, SocketChannel> entry : clients.entrySet()) {
                    if (entry.getKey() != clientName && clientMessage != null) {
                        send(toOtherClients, entry.getValue());
                    }
                }
                break;
        }
        selectionKey.interestOps(SelectionKey.OP_READ);
        state = READING;
    }
}