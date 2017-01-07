import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client extends JFrame{
    private static JButton bSend;
    private static JTextArea taChat;
    private static JTextField tfChatInput;

    private static Socket socket;
    private static DataInputStream din;
    private static DataOutputStream dout;

    private static int port = 2222;

    public Client() {
        initializeComponents();
    }

    private void initializeComponents() {
        bSend = new JButton();
        taChat = new JTextArea();
        tfChatInput = new JTextField();

        int screenWidth = 600;
        int screenHeight = 500;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Common Chat Client - CCC");
        setName("client");
        setResizable(false);
        setMinimumSize(new Dimension(screenWidth, screenHeight));

        bSend.setText("Send");
        bSend.setFont(new Font("Arial", Font.BOLD, 12));
        bSend.addActionListener(e -> bSendActionPerformed(e));
        taChat.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        taChat.setEditable(false);
        tfChatInput.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(taChat)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(tfChatInput)
                        .addComponent(bSend, GroupLayout.DEFAULT_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(taChat)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(tfChatInput, GroupLayout.DEFAULT_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                        .addComponent(bSend, GroupLayout.DEFAULT_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                )
        );
    }

    public void bSendActionPerformed(ActionEvent ae) {
        try {
            String msgout;
            msgout = tfChatInput.getText().trim();
            dout.writeUTF(msgout);
        } catch (Exception e) {

        }
    }

    public static void main (String[] args) {
        Client client = new Client();
        EventQueue.invokeLater(() -> client.setVisible(true));

        try {
            socket = new Socket("localhost", port);
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            String msgin;
            while (true)
            {
                msgin = din.readUTF();
                taChat.append("Server: \t" + msgin + "\n");
            }
        } catch (Exception e) {

        }
    }
}