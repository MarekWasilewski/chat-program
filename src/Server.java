import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends JFrame{
    private static JButton bStart;
    private static JButton bStop;
    private static JTextArea taChat;

    private static ServerSocket ssocket;
    private static Socket socket;
    private static DataInputStream din;

    private ArrayList clientOS;

    public Server() {
        initializeComponents();
    }

    private void initializeComponents() {
        bStart = new JButton();
        bStop = new JButton();
        taChat = new JTextArea();

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
        taChat.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        taChat.setEditable(false);

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(taChat)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(bStart, GroupLayout.DEFAULT_SIZE, screenWidth / 2, GroupLayout.PREFERRED_SIZE)
                        .addComponent(bStop, GroupLayout.DEFAULT_SIZE, screenWidth / 2, GroupLayout.PREFERRED_SIZE)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(taChat)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(bStart)
                        .addComponent(bStop)
                )
        );
    }

    private class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket socket;
        PrintWriter writer;

        @Override
        public void run() {

        }
    }

    private class ServerStart implements Runnable {
        @Override
        public void run() {
            try {
                ssocket = new ServerSocket(2222);
                while (true) {
                    socket = ssocket.accept();
                    PrintWriter writer = new PrintWriter(socket.getOutputStream());
                    clientOS.add(writer);

                    //Thread für Client
                    taChat.append("Got a client connection...\n");
                }
            } catch (Exception e) {
                taChat.append("Error, Server did not start...\n");
            }
        }
    }

    private void bStartActionPerformed(ActionEvent ae) {
        Thread starter = new Thread(new ServerStart());
        starter.start();
        taChat.append("Server started...\n");
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