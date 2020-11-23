package com.example.T24Transactions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class T24TestClient {
    Socket socket;

    PrintWriter t24Writer; // sends to t24 server
    BufferedReader t24Reader; // Reads from t24 server

    // connect to t24 server
    public T24TestClient(String host, int port) {
        try{
            // t24 server socket connection
            socket = new Socket(host, port);

            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            t24Reader = new BufferedReader(inputStreamReader);
            t24Writer = new PrintWriter(socket.getOutputStream());

        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    // start server for listening to terminal requests
    public void run() {
        System.out.println("Listening on port 9000....");
        try {
            // create server port
            ServerSocket serverSocket = new ServerSocket(9000);

            // listen to client requests continuously
            while (true) {
                // get socket connection from current client
                Socket terminalSocket = serverSocket.accept();

                // send message asynchronously to t24 server
                new TerminalHandler(terminalSocket).sendToT24();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public class TerminalHandler {
        BufferedReader reader;
        Socket sock;

        public TerminalHandler(Socket terminalSocket) {
            try {
                sock = terminalSocket;
                // reader for reading msg from terminal
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Async
        public void sendToT24() {
            try {
                // read message from terminal
                String message = reader.readLine();

                // send message to t24 server
                t24Writer.println(message);

                // response from t24 server
                String t24resp = null;

                try {
                    while ((t24resp = t24Reader.readLine()) != null) {
                        t24resp += t24resp;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if (t24resp == null) {
                    System.out.println(false);
                } else {
                    System.out.println(t24resp.equals(message));
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
