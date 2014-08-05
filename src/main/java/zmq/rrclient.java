package main.java.zmq;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

/**
*Client
*Connects REQ socket to tcp://localhost:5559
*Sends id to server, expects list of studies back
*/
public abstract class rrclient{
    public static void main (String[] args) {
    	String request = args[0];//which port come from
    	String testId = args[1];//id passed in via argument
        Context context = ZMQ.context(1); 
        //  Socket to talk to server       
        Socket requester = context.socket(ZMQ.REQ);
        requester.connect(request);
        System.out.println("launch and connect client.");
        //auth - com.webridge.account.Person[OID[04703945D4509F42837E8561B0556F18]]
        //crms - c11-00220
        //irb - i13-00747, i1444
        for (int request_nbr = 0; request_nbr < 1; request_nbr++) {
        	requester.send(testId, 0);
            byte[] reply = requester.recv(0);
            System.out.println("Received reply " + request_nbr + " [" + new String(reply) + "]");
        }
        //  We never get here but clean up anyhow
        requester.close();
        context.term();
    }
}