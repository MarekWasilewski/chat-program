import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends JFrame{
    private static JButton bStart;
    private static JButton bStop;
    private static JTextArea taServerStatus;

    private static ArrayList<PrintWriter> clientWriters;

    public Server() {
        initializeComponents();
    }

    private void initializeComponents() {
        bStart = new JButton();
        bStop = new JButton();
        taServerStatus = new JTextArea();

        int screenWidth = 600;
        int screenHeight = 500;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Common Chat Client (Server) - CCC");
        setName("server");
        setResizable(false);
        setMinimumSize(new Dimension(screenWidth, screenHeight));

        bStart.setText("Start server");
        bStart.setFont(new Font("Arial", Font.BOLD, 12));
        bStart.addActionListener(e -> bStartActionPerformed(e));
        bStop.setText("Stop server");
        bStop.setFont(new Font("Arial", Font.BOLD, 12));
        bStop.addActionListener(e -> bStopActionPerformed(e));
        taServerStatus.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        taServerStatus.setEditable(false);

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(taServerStatus)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(bStart, GroupLayout.DEFAULT_SIZE, screenWidth / 2, GroupLayout.PREFERRED_SIZE)
                        .addComponent(bStop, GroupLayout.DEFAULT_SIZE, screenWidth / 2, GroupLayout.PREFERRED_SIZE)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(taServerStatus)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(bStart)
                        .addComponent(bStop)
                )
        );
    }

    private class ClientHandler implements Runnable {
        BufferedReader reader;
        PrintWriter client;

        ClientHandler(Socket clientSocket, PrintWriter user) {
            client = user;
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
                reader = new BufferedReader(inputStreamReader);
            } catch (Exception e) {
                taServerStatus.append("An unexpected error occurred...\n");
            }
        }

        @Override
        public void run() {
            String message, connected = "CONNECTED", disconnected = "DISCONNECTED", chat = "CHAT";
            String[] messageInformation;

            try {
                while ((message = reader.readLine()) != null) {
                    taServerStatus.append(message + "\n");
                    messageInformation = message.split(";");
                    if (messageInformation[2].trim().equals(connected)) {
                        printMessage(messageInformation[0] + ";" + messageInformation[1] + ";" + connected);
                    } else if (messageInformation[2].trim().equals(disconnected)) {
                        printMessage(messageInformation[0] + ";" + messageInformation[1] + ";" + disconnected);
                    } else if (messageInformation[2].trim().equals(chat)) {
                        printMessage(message);
                    } else {
                        taServerStatus.append("Message contained errors...\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void printMessage(String message) {
        for (PrintWriter writer : clientWriters) {
            try {
                writer.println(message);
                taServerStatus.append("Sending " + message + "\n");
                writer.flush();
//                taServerStatus.setCaretPosition(taServerStatus.getDocument().getLength());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ServerStart implements Runnable {
        @Override
        public void run() {
            clientWriters = new ArrayList();
            try {
                ServerSocket serverSocket = new ServerSocket(2222);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                    clientWriters.add(writer);
                    Thread listener = new Thread(new ClientHandler(clientSocket, writer));
                    listener.start();
                    taServerStatus.append("Got a client connection...\n");
                }
            } catch (Exception e) {
                taServerStatus.append("Some exception occurred...\n");
            }
        }
    }

    private void bStartActionPerformed(ActionEvent ae) {
        Thread starter = new Thread(new ServerStart());
        starter.start();
        taServerStatus.append("Server started...\n");
    }

    private void bStopActionPerformed(ActionEvent ae) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) {
        Server server = new Server();
        EventQueue.invokeLater(() -> server.setVisible(true));
    }
}