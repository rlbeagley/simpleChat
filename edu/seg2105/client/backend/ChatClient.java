// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  /*
   * The ID of the person who is a client instance
   */
  String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID,String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message){
    try
    {
    	if (message.startsWith("#")) {
    		handleCommand(message);
    	} else {
    		sendToServer(message);
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /*
   * If message from client UI starts with #, method handles it depending on the command.
   * 
   * @param command specifies the type of command to be run.
   */
  private void handleCommand(String command) {
	  try { 
		  if (command.equals("#quit")) { // will i need to remove spaces?
				System.exit(0);
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
	  }
  }
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  /*
   * Implemented hook method called once the connection is established that sends the message #login with the client's loginID
   * to the server.
   */
  protected void connectionEstablished() {
		try {
			sendToServer("#login "+ loginID);
		} catch (IOException e) {
			clientUI.display("IOException while sending loginID to server");
		}
	}
  
  
  /*
   * Method informs the client that the server has shut down, then terminates the client.
   */
  @Override
  protected void connectionClosed() {
	  clientUI.display("Connection closed.");

	}
  
  
  /*
   * Method informs the client that the server has run into an issue, then terminates the client. 
   */
  @Override
  protected void connectionException(Exception exception) {
	  clientUI.display("Server has unexpectedly shut down due to an error.");
	  System.exit(0);
	}
}
//End of ChatClient class
