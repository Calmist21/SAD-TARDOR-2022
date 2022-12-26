
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;


public class Server {
    private static final int port = 5555;
    private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public static void main(String[] args) {
        try {
            MySelector mySelector = new MySelector(port);
            new Thread(mySelector).start();
            System.out.println(dateFormat.format(new Date()) + " Server listening on port " + port + ".");
        } catch (IOException e) {
            System.err.println(dateFormat.format(new Date()) + " Couldn't open the server, consider trying another port.");
        }
    }

    static class MySelector implements Runnable {

        final Selector selector;
        final ServerSocketChannel serverSocketChannel;

     
        public MySelector(int port) throws IOException {

            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            SelectionKey mySelectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            mySelectionKey.attach(new Acceptor());
        }

        //Utilizamos el selector para hacer el run en el ServletProtocol thread para ver la que la conexion con el client sea correcta
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    selector.select();
                    Set<SelectionKey> selected = selector.selectedKeys();
                    Iterator<SelectionKey> it = selected.iterator();
                    while (it.hasNext()) {
                        dispatch(it.next());
                    }
                    selected.clear();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        
        void dispatch(SelectionKey k) {
            Runnable run = (Runnable) (k.attachment());
            if (run != null) {
                run.run();
            }
        }

       
        class Acceptor implements Runnable {
            public void run() {
                try {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    if (socketChannel != null) {
                        new ServerProtocol(selector, socketChannel);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}