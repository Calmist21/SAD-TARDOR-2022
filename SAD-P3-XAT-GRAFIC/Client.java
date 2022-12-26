
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private static final String localhost = "localhost";
    private static final int port = 5555;

    private static String userName;
    private static Chat chatFrame;
    private static MySocket mySocket;

    public static void main(String[] args) {
        chatFrame = new Chat();
        setUpUserName();
    }

    public static void setUpUserName() {
        chatFrame.getLoginPanel().getNicknameField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chatFrame.getLoginPanel().getJoinButton().doClick();
            }
        });

        chatFrame.getLoginPanel().getJoinButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                userName = chatFrame.getLoginPanel().getNicknameField().getText();
                if (userName.isEmpty()) {
                    JOptionPane.showMessageDialog(null,"Se debe poner un nombre de usuario","No hay username",JOptionPane.ERROR_MESSAGE);
                } else {
                    connectToServer();
                }

            }
        });
    }

    public static void setUpChat(String connection) {
        chatFrame.setupChatPanel(userName);
        chatFrame.getChatPanel().getChatText().append(connection);
        chatFrame.getChatPanel().getSendButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String message = chatFrame.getChatPanel().getMessageField().getText();
                if (message.isEmpty()) {
                    JOptionPane.showMessageDialog(null,"Mensaje sin contenido","Empty Message", JOptionPane.ERROR_MESSAGE);
                } else {
                    sendMessage(message);
                    chatFrame.getChatPanel().getMessageField().setText("");
                }
            }
        });

        chatFrame.getChatPanel().getDisconnectButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                disconnect();
            }
        });
    }
     public static void connectToServer() {
        mySocket = new MySocket(userName, localhost, port);

        //Thread que escucha y procesa la info del ServerProtocol
        Thread outputThread = new Thread(() -> {
            String line;
            try {
                while ((line = mySocket.readLine()) != null) {
                    if (line.contains("[HELLO CLIENT]")) {
                        String str = line.substring(line.indexOf("]") + 2);
                        setUpChat(str);
                        
                        line = mySocket.readLine();
                        str = line.substring(line.indexOf("]") + 1);
                        while (str.contains(";")) {
                            String user;
                            if (str.indexOf(";", 1) != -1) {
                                user = str.substring(1, str.indexOf(";", 1));
                                str = str.substring(str.indexOf(";", 1));
                            } else {
                                user = str.substring(1);
                                str = "";
                            }
                            chatFrame.getChatPanel().getUsersList().addElement(user);
                            chatFrame.revalidate();
                            chatFrame.repaint();
                        }
                        
                    } else if (line.contains("[CLIENTE CONECTADO]")) {
                        String str = line.substring(line.indexOf("]") + 2);
                        chatFrame.getChatPanel().getUsersList().addElement(str);
                        
                    } else if (line.contains("[CLIENTE DESCONECTADO]")) {
                        String str = line.substring(line.indexOf("]") + 2);
                        chatFrame.getChatPanel().getUsersList().removeElement(str);
                        
                    } else {
                        writeInChat(line);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        outputThread.start();
    }

    public static void sendMessage(String message) {
        mySocket.printLine(message);        
        writeInChat( " ["+ userName + "]: " + message);
    }
    
   
    public static void writeInChat(String text) {
        chatFrame.getChatPanel().getChatText().append("\n" + text);
    }

    public static void disconnect() {
        mySocket.close();
        chatFrame.dispose();
        System.exit(0);
    }
}