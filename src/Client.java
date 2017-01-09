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

    private static PrintWriter writer;
    private static BufferedReader reader;
    private static int port = 2222;

    private static String username;
    private static boolean isConnected = false;

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
        if (isConnected) {
            if (tfChatInput.getText().trim().equals("")) {
                tfChatInput.setText("");
            } else {
                try {
                    username = tfUser.getText();
                    writer.println(username + ";" + tfChatInput.getText() + ";" + "CHAT");
                    writer.flush();
                } catch (Exception e) {
                    taChat.append("Message was not sent...\n");
                }
                tfChatInput.setText("");
            }
        } else {
            taChat.append("Not connected to the server...\n");
            tfChatInput.setText("");
        }
    }

    private void bConnectActionPerformed(ActionEvent ae) {
        if (!isConnected) {
            username = tfUser.getText();
            if (Objects.equals(username, "")) {
                taChat.append("You need to choose a username before you connect to the server...\n");
            } else {
                try {
                    Socket socket = new Socket("localhost", port);
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                    reader = new BufferedReader(inputStreamReader);
                    writer = new PrintWriter(socket.getOutputStream());
                    writer.println(username + ";has connected;CONNECTED");
                    writer.flush();
                    isConnected = true;
                    tfUser.setEditable(false);
                } catch (Exception e) {
                    taChat.append("Could not connect to the server, try again...\n");
                    tfUser.setEditable(true);
                }
                ListenThread();
            }
        } else {
            taChat.append("You are already connected to the server...\n");
        }
    }

    private void ListenThread()
    {
        Thread IncomingReader = new Thread(new IncomingReader());
        IncomingReader.start();
    }

    private class IncomingReader implements Runnable{
        @Override
        public void run() {
            String stream, connected = "CONNECTED", disconnected = "DISCONNECTED", chat = "CHAT";
            String[] streamInformation;

            try {
                while ((stream = reader.readLine()) != null) {
                    streamInformation = stream.split(";");
                    if (streamInformation[2].equals(chat)) {
                        taChat.append(streamInformation[0] + ": " + streamInformation[1] + "\n");
//                        taChat.setCaretPosition(taChat.getDocument().getLength());
                    } else if (streamInformation[2].equals(connected)) {
                        taChat.append(streamInformation[0] + " connected...\n");
//                        taChat.removeAll();
                    } else if (streamInformation[2].equals(disconnected)) {
                        //
                    } else {
                        //
                    }
                }
            } catch (Exception ex) {
                //
            }
        }
    }

    public static void main (String[] args) {
        Client client = new Client();
        EventQueue.invokeLater(() -> client.setVisible(true));
    }
}