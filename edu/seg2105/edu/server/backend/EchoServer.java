package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import edu.seg2105.client.common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  private ChatIF serverUI; 
  
  //Instance variables ***********************************************
  private int numMessagesFromClient = 0;
  
  private String loginKey;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) 
  {
    super(port);
    this.serverUI = serverUI;
  
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client){
	  serverUI.display("Message received: " + msg + " from " + client.getInfo("loginID"));
	  String message = (String) msg;
	  if (message.startsWith("#login")) { // adds loginID to hashmap
		  if (numMessagesFromClient == 0 ) {
			  String[] msgSplit = message.split(" ");
			  loginKey= msgSplit[1];
			  client.setInfo("loginID", loginKey);
			  System.out.println(client.getInfo("loginID") + " has logged in!");
		  } else { // not the first message sent 
			  try {
				client.close();
			} catch (IOException e) {
				serverUI.display("IOException.");
				e.printStackTrace();
			}
		  }
		  
	  } else { // sends to all clients with ID in front 
		  Object clientID = client.getInfo("loginID");
		  String idAndMsg = (String) clientID +": " + (String) msg;
		  this.sendToAllClients(idAndMsg);
	  }
	  numMessagesFromClient++;
	  
	  
  }
  
  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromServerUI(String message){
    if (message.startsWith("#")) {
		handleCommand(message);
	} else {
		this.sendToAllClients(message);
	}
  }
  
  /*
   * If message from server UI starts with #, method handles it depending on the command.
   * 
   * @param command specifies the type of command to be run.
   */
  private void handleCommand(String command) {
	
	  try { 
		  if (command.equals("#quit")) { 
				System.exit(0);
		  } else if (command.equals("#stop")) {
				stopListening();
		  } else if (command.equals("#close")) {
				close();
		  } else if (command.contains("#setport")) {
				String[] msgSplit = command.split(" ");
				setPort(Integer.parseInt(msgSplit[1]));
		  } else if (command.equals("#start")) {
				if (isListening()) {
					serverUI.display("Already started the server.");
				} else {
					listen();
				}
		  } else if (command.equals("#getport")) {
				serverUI.display(Integer.toString(getPort()));
		  } else {
				serverUI.display("Invalid command.");
			}
		 
	  } catch (IndexOutOfBoundsException e) {
		  serverUI.display("IndexOutOfBoundsException, enter port value after #setport");
		  e.printStackTrace();
	  } catch (IOException e) {
		  serverUI.display("IOException");
		  e.printStackTrace();
	  }
  }

    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
	  System.out.println
      ("Server has stopped listening for connections.");
  }
  
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("New client has connected.");
  }
  
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println(client.getInfo("loginID")+ " has disconnected. ");
  }
  
  
}
//End of EchoServer class
