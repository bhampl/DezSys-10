package at.ac.tgm.hit.dezsys.hamplwortha.net;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This class handles socket connections.
 *
 * @author Burkhard Hampl [bhampl@student.tgm.ac.at]
 * @version 1.0
 */
public class Connection implements Closeable, AutoCloseable {


    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private int port = 0;
    private String host = "";

    /**
     * Creates a new connection with the given socket.
     *
     * @param socket the socket.
     * @throws IOException if an I/O error occurs when creating the connection.
     */
    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(this.socket.getInputStream());
        this.out = new DataOutputStream(this.socket.getOutputStream());
    }

    /**
     * Creates a new connection with the given host and port.
     *
     * @param host the host.
     * @param port the port.
     * @throws IOException if an I/O error occurs when creating the connection.
     */
    public Connection(String host, int port) throws IOException {
        this(new Socket(host, port));
        this.port = port;
        this.host = host;
    }

    /**
     * Copy constructor.
     *
     * @param connection the old object.
     * @throws IOException if an I/O error occurs when creating the connection.
     */
    public Connection(Connection connection) throws IOException {
        this.socket = connection.getSocket();
        this.in = connection.getIn();
        this.out = connection.getOut();
        this.port = connection.getPort();
        this.host = connection.getHost();
    }


    /**
     * Creates a new socket with the values of the previous one.
     *
     * @throws IOException if an I/O error occurs when creating the connection.
     */
    public void createNewSocket() throws IOException {
        if (this.host == null) this.host = "";
        if (this.host.equals("") && this.port == 0) {
            this.socket = new Socket(this.socket.getInetAddress(), this.socket.getPort());
        } else if (this.host.equals("") && this.port != 0) {
            this.socket = new Socket(this.socket.getInetAddress(), this.port);
        } else if (!this.host.equals("") && this.port == 0) {
            this.socket = new Socket(this.host, this.socket.getPort());
        } else if (!this.host.equals("") && this.port != 0) {
            this.socket = new Socket(this.host, this.port);
        }
        this.in = new DataInputStream(this.socket.getInputStream());
        this.out = new DataOutputStream(this.socket.getOutputStream());
    }

    public Socket getSocket() {
        return this.socket;
    }

    public DataInputStream getIn() {
        return this.in;
    }

    public DataOutputStream getOut() {
        return this.out;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Reads from the socket of the connection.
     *
     * @return the message in bytes.
     * @throws IOException if an I/O error occurs when creating the connection.
     */
    public byte[] read() throws IOException {
        int length = this.in.readInt();
        if (length > 0) {
            byte[] message = new byte[length];
            this.in.readFully(message, 0, message.length);
            return message;
        }
        return null;
    }

    /**
     * Writes to the socket of the connection.
     *
     * @param bytes the message in bytes.
     * @throws IOException if an I/O error occurs when creating the connection.
     */
    public void write(byte[] bytes) throws IOException {
        this.out.writeInt(bytes.length);
        this.out.write(bytes);
    }

    @Override
    public void close() throws IOException {
        this.out.close();
        this.in.close();
        this.socket.close();
    }
}
