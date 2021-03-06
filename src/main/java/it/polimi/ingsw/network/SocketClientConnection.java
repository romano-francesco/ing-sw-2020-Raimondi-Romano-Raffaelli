package it.polimi.ingsw.network;

import it.polimi.ingsw.message.MessageToClient;
import it.polimi.ingsw.message.MessageToServer;
import it.polimi.ingsw.message.PingMessage;
import it.polimi.ingsw.message.TypeMessage;
import it.polimi.ingsw.model.player.PlayerIndex;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;


public class SocketClientConnection extends Observable<MessageToServer> implements ClientConnection, Runnable {

    private ObjectOutputStream out;
    private final Socket socket;
    private final Server server;
    private boolean active = true;
    private ObjectInputStream in;

    private transient final BlockingQueue<MessageToServer> inputMessageQueue = new ArrayBlockingQueue<>(10);
    private PlayerIndex clientIndex;

    public SocketClientConnection(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;
    }

    private synchronized void setIsActiveFalse() {
        this.active = false;
    }

    @Override
    public synchronized boolean isConnected() {
        return active;
    }


    /**
     * @param message to write on socket server -> client
     */
    private synchronized void send(MessageToClient message) throws IOException {
        out.reset();
        out.writeObject(message);
        out.flush();
    }



     /**
      * Close socket, input-stream and output-stream
     */

    public synchronized void closeConnection() {

        //notify(new CloseConnectionMessage(this.clientIndex));

        try {
            out.close();
            in.close();
            socket.close();
            System.out.println("Socket connection closed");
        } catch (IOException e) {
            System.out.println(this.clientIndex + " isn't connected");
        }
        setIsActiveFalse();
    }

    @Override
    public void asyncSend(final MessageToClient message) {
        new Thread(() -> {
            try {
                send(message);
                if (message.getType().equals(TypeMessage.CLOSE_CONNECTION)
                        && message.getClient().equals(this.clientIndex))
                    this.closeConnection();
            } catch (IOException e) {
                closeConnection();
            }
        }).start();
    }

    /**
     * Create the input and output stream, insert the connection in server lobby and remain active to read message input
     * from clients
     */
    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            server.lobby(this);
            new Thread(() -> {
                try {
                    while (isConnected()) {
                        MessageToServer input = inputMessageQueue.take();
                        notify(input);
                    }
                } catch (InterruptedException e) {
                    setIsActiveFalse();
                }
            }).start();
            while (isConnected()) {
                try {
                    MessageToServer inputMessage = (MessageToServer) in.readObject();
                    if (inputMessage != null && inputMessage.getType() != TypeMessage.PONG) {
                        try {
                            inputMessageQueue.put(inputMessage);
                        } catch (InterruptedException e) {
                            System.err.println("Error!" + e.getMessage());
                            Logger.getAnonymousLogger().severe(e.getMessage());
                            setIsActiveFalse();
                        }
                    }
                } catch (ClassNotFoundException e) {
                    Logger.getAnonymousLogger().severe(e.getMessage());
                    setIsActiveFalse();
                }
            }

        } catch (IOException | NoSuchElementException e) {
            System.out.println(this.clientIndex + " isn't connected");
        }
    }

    @Override
    public void ping(PlayerIndex player) throws IOException {
        send(new PingMessage(player));
    }

    @Override
    public void forceDisconnection() {
        //notify(new CloseConnectionMessage(this.clientIndex));
        setIsActiveFalse();
    }

    public void setClientIndex(PlayerIndex clientIndex) {
        this.clientIndex = clientIndex;
    }
}