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
	  System.out.println("Message received: " + msg + " from " + client);
	  this.sendToAllClients(msg);
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
	  /*
	  try { 
		  if (command.equals("#quit")) { // will i need to remove spaces?
				quit();
			} else if (command.equals("#logoff")) {
				closeConnection();
				
			} else if (command.contains("#sethost")) {
				String[] msgSplit = command.split(" ");
				setHost(msgSplit[1]);
			} else if (command.contains("#setport")) {
				String[] msgSplit = command.split(" ");
				setPort(Integer.parseInt(msgSplit[1]));
			} else if (command.equals("#login")) {
				if (isConnected()) {
					clientUI.display("You are already connected. Cannot connect again.");
				} else {
					openConnection();
				}
			} else if (command.equals("#gethost")) {
				clientUI.display(getHost());
			} else if (command.equals("#getport")) {
				clientUI.display(Integer.toString(getPort()));
			} else {
				clientUI.display("Invalid command.");
			}
		 
	  } catch (IOException e) {
		  clientUI.display("IOException");
		  e.printStackTrace();
	  } catch (IndexOutOfBoundsException e) {
		  clientUI.display("IndexOutOfBoundsException");
		  e.printStackTrace();
	  }*/
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
	  System.out.println("Client has connected!");
  }
  
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("Client has disconnected. ");
  }
  
}
//End of EchoServer class
