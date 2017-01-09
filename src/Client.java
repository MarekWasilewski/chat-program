import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Client extends JFrame{
    private static JLabel lUser;
    private static JButton bSend;
    private static JButton bConnect;
    private static JTextArea taChat;
    private static JTextField tfChatInput;
    private static JTextField tfUser;

    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;
    private static int port = 2222;

    public Client() {
        initializeComponents();
    }

    private void initializeComponents() {
        lUser = new JLabel();
        bSend = new JButton();
        bConnect = new JButton();
        taChat = new JTextArea();
        tfChatInput = new JTextField();
        tfUser = new JTextField();

        int screenWidth = 600;
        int screenHeight = 500;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Common Chat Client - CCC");
        setName("client");
        setResizable(false);
        setMinimumSize(new Dimension(screenWidth, screenHeight));

        lUser.setText("Username:");
        bSend.setText("Send");
        bSend.setFont(new Font("Arial", Font.BOLD, 12));
        bSend.addActionListener(e -> bSendActionPerformed(e));
        bConnect.setText("Connect");
        bConnect.setFont(new Font("Arial", Font.BOLD, 12));
        bConnect.addActionListener(e -> bConnectActionPerformed(e));
        taChat.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        taChat.setEditable(false);
        tfChatInput.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        tfUser.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        tfUser.setText("");

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(lUser)
                        .addComponent(tfUser)
                )
                .addComponent(bConnect)
                .addComponent(taChat)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(tfChatInput)
                        .addComponent(bSend, GroupLayout.DEFAULT_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lUser)
                        .addComponent(tfUser)
                )
                .addComponent(bConnect)
                .addComponent(taChat)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(tfChatInput, GroupLayout.DEFAULT_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                        .addComponent(bSend, GroupLayout.DEFAULT_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                )
        );
    }

    private void bSendActionPerformed(ActionEvent ae) {
        try {
            writer.println(tfChatInput.getText());
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tfChatInput.setText("");
    }

    private void bConnectActionPerformed(ActionEvent ae) {
        if (Objects.equals(tfUser.getText(), "")) {
            JOptionPane.showMessageDialog(null,"Must choose a username!","Error!",JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                tfUser.setEditable(false);
                socket = new Socket("localhost", port);
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(inputStreamReader);
                writer = new PrintWriter(socket.getOutputStream());
                writer.println("User has connected...");
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main (String[] args) {
        Client client = new Client();
        EventQueue.invokeLater(() -> client.setVisible(true));
    }
}