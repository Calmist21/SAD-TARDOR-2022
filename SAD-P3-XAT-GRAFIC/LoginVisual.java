
import javax.swing.*;
import java.awt.*;

public class LoginVisual extends JPanel {

    private JLabel nickLabel;
    private JLabel info, welcome;
    private JTextField nickField;
    private JButton enter;
    private GridBagConstraints color;

    public LoginVisual() {
        super(new GridBagLayout());
        setupGUI();
    }
    public JTextField getNicknameField(){
        return nickField;
    }

    public JButton getJoinButton(){
        return enter;
    }
    public void setupGUI() {

        setBackground(Color.LIGHT_GRAY);

        color = new GridBagConstraints();

        // Introducir 
        info = new JLabel("Introduce tu usuario:");
        color.weightx = 1.0;
        color.weighty = 1.0;
        color.fill = GridBagConstraints.HORIZONTAL;
        color.anchor = GridBagConstraints.SOUTHWEST;
        color.insets = new Insets(40, 0, 0, 40);
        color.gridx = 2;
        color.gridy = 0;
        add(info,color);

        //nick
        nickLabel = new JLabel("Nombre:");
        nickLabel.setFont(new Font("", Font.PLAIN, 15));
        color.weightx = 1.0;
        color.weighty = 0.0;
        color.fill = GridBagConstraints.HORIZONTAL;
        color.anchor = GridBagConstraints.EAST;
        color.insets = new Insets(0, 40, 0, 0);
        color.gridx = 1;
        color.gridy = 1;
        add(nickLabel,color);

        //Bienvenido
        welcome = new JLabel("BIENVENIDO");
        welcome.setFont(new Font("", Font.BOLD, 25));
        color.weightx = 1.0;
        color.weighty = 2.0;
        color.fill = GridBagConstraints.HORIZONTAL;
        color.anchor = GridBagConstraints.CENTER;
        color.insets = new Insets(10, 0, 0, 40);
        color.gridx = 2;
        color.gridy = 0;
        add(welcome,color);
        
   
        nickField = new JTextField();
        color.weightx = 1.0;
        color.weighty = 0;
        color.fill = GridBagConstraints.HORIZONTAL;
        color.anchor = GridBagConstraints.WEST;
        color.insets = new Insets(5, 0, 0, 40);
        color.gridx = 2;
        color.gridy = 1;
        add(nickField,color);

      
        enter = new JButton("Entrar");
        color.weightx = 0.0;
        color.weighty = 0.0;
        color.fill = GridBagConstraints.NONE;
        color.anchor = GridBagConstraints.NORTH;
        color.gridx = 2;
        color.gridy = 2;
        color.insets = new Insets(15, 0, 40, 40);
        add(enter,color);
    }
}