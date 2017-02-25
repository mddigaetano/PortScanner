
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Amedeo Di Gaetano <digaetano.amedeo at gmail.com>
 */
public class ThreadScanner extends Thread {
    
    private Socket sock;
    private final InetAddress hostname;
    private final int port;
    private String message;
    private String errmessage;
    
    public ThreadScanner(InetAddress hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.message = null;
        this.errmessage = null;
    }

    @Override
    public void run() {
        try {
            sock = new Socket(hostname, port);
            message = sock.getRemoteSocketAddress() + " listening on TCP port: " + port;
        } catch (IllegalArgumentException iae) {
            errmessage = "Invalid port " + port;
        } catch (ConnectException ce) {
            errmessage = "No server listening on TCP port: " + port;
        } catch (SecurityException se) {
            errmessage = "No permission to connect to port: " + port;
        } catch (IOException ioe) {
            errmessage = "Couldn't connect on port "+ port +": Unknown reason";
        }
    }

    public String getMessage() {
        return message;
    }

    public String getErrmessage() {
        return errmessage;
    }
    
}
