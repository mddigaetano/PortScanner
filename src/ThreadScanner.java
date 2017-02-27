
import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Amedeo Di Gaetano <digaetano.amedeo at gmail.com>
 */
public class ThreadScanner implements Runnable {
    
    private Socket sock;
    private static DatagramSocket dsock;
    private DatagramPacket in;
    private DatagramPacket out;
    private final InetAddress hostname;
    private final int port;
    private final String MESSAGE = "Hello, world!";
    private String errmessageTCP;
    private String errmessageUDP;
    
    public ThreadScanner(InetAddress hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.errmessageTCP = null;
        this.errmessageUDP = null;
        if(dsock == null){
            try {
                ThreadScanner.dsock = new DatagramSocket();
            } catch (SocketException ex) {
                System.err.println("Couldn't open socket");
            }
        }
    }

    @Override
    public void run() {
        try {
            sock = new Socket(hostname, port);
            System.out.println(sock.getRemoteSocketAddress() + " listening on TCP port: " + port);
        } catch (IllegalArgumentException iae) {
            errmessageTCP = "Invalid port " + port;
        } catch (ConnectException ce) {
            errmessageTCP = "No server listening on TCP port: " + port;
        } catch (SecurityException se) {
            errmessageTCP = "No permission to connect to TCP port: " + port;
        } catch (IOException ioe) {
            errmessageTCP = "Couldn't connect on TCP port "+ port +": Unknown reason";
        } finally{
            if(sock != null){
                try {
                    sock.close();
                } catch (IOException ex) {
                    System.err.println("Couldn't close TCP socket");
                }
            }
        }
        
        try{
            out = new DatagramPacket(MESSAGE.getBytes(), MESSAGE.getBytes().length, hostname, port);
            in = new DatagramPacket(new byte[256], 256);
            
            if(dsock != null){
                dsock.send(out);
                dsock.setSoTimeout(10000);
                dsock.receive(in);
                System.out.println(hostname.getHostName() + in.getSocketAddress() + " listening on UDP port: " + port);
            }
        } catch (IllegalArgumentException iae) {
            errmessageUDP = "Invalid port " + port;
        } catch (SecurityException se) {
            errmessageUDP = "No permission to connect to UDP port: " + port;
        } catch (SocketTimeoutException ste) {
            errmessageUDP = "No server listening on UDP port: " + port;
        } catch (IOException ioe){
            errmessageUDP = "Couldn't connect on UDP port "+ port +": Unknown reason";
        }
    }

    public String getErrmessageTCP() {
        return errmessageTCP;
    }
    
    public String getErrmessageUDP() {
        return errmessageUDP;
    }
    
}
