
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Amedeo Di Gaetano <digaetano.amedeo at gmail.com>
 */
public class PortScan {

    public static void main(String[] args) {

        int port;
        int startport;
        int endport;
        ThreadScanner[] list;
        InetAddress hostname = null;

        Scanner keyboard = new Scanner(System.in);
        
        String destination;
        if(args.length < 1){
            System.out.print("Type in the IP or hostname to scan: ");
            destination = keyboard.nextLine();
        } else{
            destination = args[0];
        }
        
        try {
            hostname = InetAddress.getByName(destination);
        } catch (UnknownHostException ex) {
            System.err.println("Couldn't resolve hostname");
            System.exit(1);
        }
        
        String range;
        if(args.length < 2){
            System.out.print("Type in the desired port or range to scan: ");
            range = keyboard.nextLine();
        } else{
            range = args[1];
        }
        
        if (range.equals("")) {
            range = "1-1024";
        }

        String[] temp = range.split("-");

        if (temp.length == 1) {
            int get = Integer.parseInt(range);
            startport = get;
            endport = get;
        } else {
            startport = Integer.parseInt(temp[0]);
            endport = Integer.parseInt(temp[1]);
        }

        if (startport < 0) {
            startport = 0;
        }

        if (endport > 65535) {
            endport = 65535;
        }
        
        list = new ThreadScanner[endport - startport + 1];
        
        long time = System.currentTimeMillis();
        
        int i;
        for (port = startport, i = 0; port <= endport; port++, i++) {
            ThreadScanner t = new ThreadScanner(hostname, port);
            t.start();
            list[i] = t;
        }
        
        System.out.println("All threads started in: " + ((System.currentTimeMillis() - time) / 1000.) + " s");
        
        for (i = 0; i < list.length; i++) {
            try {
                list[i].join();
                if (list[i].getMessage() != null) {
                    System.out.println(list[i].getMessage());
                }

            } catch (InterruptedException ex) {
                System.err.println("Couldn't stop main thread");
            }
        }
        

//        for (ThreadScanner t : list) {
//            if (t.getErrmessage() != null) {
//                System.err.println(t.getErrmessage());
//            }
//        }

    }

}
