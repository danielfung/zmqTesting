package zeromqTest;
import java.io.IOException;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import zeromqTest.zmqAuth.rrserverAuth;
import zeromqTest.zmqCRMS.rrserverCRMS;
import zeromqTest.zmqIRB.rrserverIRB;
/**
* Server
* Connects REP socket to tcp://*:5560
* Expects id from client, replies with list of studies
*/
public abstract class rrserver{	
	protected String database;
	protected String collection;
	protected String studiesString;
	protected String inputActivityList;
	protected String inputRelatedStudies;
	protected String id;
	protected String resultString;
	
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