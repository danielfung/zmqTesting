package main.java.zmq;
import java.io.IOException;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import main.java.zmqauth.rrserverAuth;
import main.java.zmqcrms.rrserverCRMS;
import main.java.zmqirb.rrserverIRB;
/**
* Server
* Connects REP socket to tcp://*:5560
* Expects id from client, replies with list of studies
*/
public abstract class rrserver{	
	protected String database;
	protected String collection;
	protected String studiesString;
	protected String id;
	protected String resultString;
	protected String inputListName;
	
	abstract public String convertString(String item);
	abstract public String start(String request) throws InterruptedException, IOException;
	abstract public void reset();
	
	public static void main (String[] args) throws IOException {
		String respond = args[0];//which port responder on
		String server = args[1];//what store is the request using: irb/crms/auth
		rrserverAuth auth = new rrserverAuth();
		rrserverCRMS crms = new rrserverCRMS();
		rrserverIRB irb = new rrserverIRB();
        Context context = ZMQ.context(1);       
        //  Socket to talk to clients
        Socket responder  = context.socket(ZMQ.REP);
        responder.connect(respond);     
        System.out.println("launch and connect server.");

        while (!Thread.currentThread().isInterrupted()) {
            //  Wait for next request from client
            byte[] request = responder.recv(0);
            String string = new String(request);        
            String reply = null;
            System.out.println("Received request: ["+string+"].");
            try {
            	if(server.equals("irb")){//if irb
                	reply = irb.start(string);
                    irb.reset();
            	}
            	if(server.equals("crms")){//if crms
                    reply = crms.start(string);
                    crms.reset();
            	}
            	if(server.equals("auth")){//if auth
                    reply = auth.start(string);
                    auth.reset();  
            	}   	
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //  Send reply back to client
            responder.send(reply.getBytes(), 0);           
        }
        //  We never get here but clean up anyhow
        responder.close();
        context.term();
    }
}