

import javax.swing.*;
import java.awt.*;

public class ChatVisual extends JPanel {

    private final JTextArea chatText;
    private final JButton send;
    private final JButton desconnect;
    private final JTextField messageField;
    private final DefaultListModel usersListModel;
    private final JList<String> users;
    private final GridBagConstraints color;

    public ChatVisual(String connectionString) {
        super(new GridBagLayout());
        chatText = new JTextArea(connectionString);
        send = new JButton("Enviar");
        desconnect = new JButton("Desconectar");
        messageField = new JTextField();
        color = new GridBagConstraints();
        usersListModel = new DefaultListModel<>();
        users = new JList<>(usersListModel);

        setupGUI();
    }
    
    public JTextArea getChatText() {
        return chatText;
    }
    public JButton getSendButton() {
        return send;
    }

    public JButton getDisconnectButton() {
        return desconnect;
    }
    
    public JTextField getMessageField() {
        return messageField;
    }

    public DefaultListModel getUsersList() {
        return usersListModel;
    }

    private void setupGUI() {
        setBackground(Color.pink);

        color.gridx = 0;
        color.gridy = 0;
        color.gridheight = 2;
        color.weightx = 1.0;
        color.weighty = 1.0;
        color.fill = GridBagConstraints.BOTH;
        color.anchor = GridBagConstraints.CENTER;
        color.insets = new Insets(40, 40, 10, 10);
        chatText.setBackground(Color.WHITE);
        chatText.setEditable(false);
        chatText.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(chatText);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, color);

        color.gridy = 1;
        color.gridx = 1;
        color.gridheight = 1;
        color.weightx = 0.0;
        color.weighty = 0.0;
        color.fill = GridBagConstraints.VERTICAL;
        color.anchor = GridBagConstraints.NORTHEAST;
        color.insets = new Insets(0, 10, 10, 40);
        color.ipadx = 50;
        users.setLayoutOrientation(JList.VERTICAL);
        users.setBackground(Color.white);
        add(new JScrollPane(users), color);

        color.gridx = 0;
        color.gridy = 2;
        color.weightx = 1.0;
        color.weighty = 0.0;
        color.fill = GridBagConstraints.HORIZONTAL;
        color.anchor = GridBagConstraints.NORTH;
        color.insets = new Insets(10, 40, 40, 10);
        color.ipady = 30;
        color.ipadx = 0;
        messageField.setBackground(Color.WHITE);
        add(messageField, color);

        color.gridx = 1;
        color.gridy = 2;
        color.weightx = 0;
        color.weighty = 0;
        color.ipady = 0;
        color.fill = GridBagConstraints.NONE;
        color.anchor = GridBagConstraints.WEST;
        color.insets = new Insets(10, 10, 40, 40);
        color.ipadx = 0;
        add(send, color);

        color.gridx = 1;
        color.gridy = 0;
        color.weightx = 0;
        color.weighty = 0;
        color.fill = GridBagConstraints.NONE;
        color.anchor = GridBagConstraints.NORTHEAST;
        color.insets = new Insets(15, 15, 15, 15);
        add(desconnect, color);
    }
}