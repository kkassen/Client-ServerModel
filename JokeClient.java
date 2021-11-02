/*--------------------------------------------------------

1. Kyle Arick Kassen / Date: June 20, 2021 - June 27, 2021

2. Java version used (java -version), if not the official version for the class:
C:\Users\IQ1006\JavaPrograms\CSC435\JokeServerAssignment>java -version
openjdk version "11.0.4" 2019-07-16 LTS
OpenJDK Runtime Environment Corretto-11.0.4.11.1 (build 11.0.4+11-LTS)
OpenJDK 64-Bit Server VM Corretto-11.0.4.11.1 (build 11.0.4+11-LTS, mixed mode)

3. Instructions for Compiling:
C:\Users\IQ1006\JavaPrograms\CSC435\JokeServerAssignment>javac JokeServer.java
C:\Users\IQ1006\JavaPrograms\CSC435\JokeServerAssignment>javac JokeClient.java
C:\Users\IQ1006\JavaPrograms\CSC435\JokeServerAssignment>javac JokeClientAdmin.java

4. Instructions and Details for Running the Program:
Once complied [see above: #3], use separate terminal windows:
C:\Users\IQ1006\JavaPrograms\CSC435\JokeServerAssignment>java JokeServer
C:\Users\IQ1006\JavaPrograms\CSC435\JokeServerAssignment>java JokeClient
C:\Users\IQ1006\JavaPrograms\CSC435\JokeServerAssignment>java JokeClientAdmin
• You can start JokeServer, JokeClient, and JokeClientAdmin in any order.
  — NOTE: the JokeServer must be running to successfully issue commands from the JokeClient and JokeClientAdmin.
  — All six possible start permutations orderings where tested:
     1) {JokeServer, JokeClient, JokeClientAdmin}
     2) {JokeServer, JokeClientAdmin, JokeClient}
     3) {JokeClient, JokeServer, JokeClientAdmin}
     4) {JokeClient, JokeClientAdmin, JokeServer}
     5) {JokeClientAdmin, JokeServer, JokeClient}
     6) {JokeClientAdmin, JokeServer, JokeClient}
• Operates in 'joke mode' when staring the program.  This can be confirmed by observing a portion of the start-up...
  ...message "Server Log: Operating in 'joke mode' by default."
  — Use JokeClientAdmin to switch between 'joke mode' and 'proverb mode'.
  — Type 'joke' in JokeClientAdmin terminal to switch to 'joke mode'. [note: this is case sensitive]
  — Type 'proverb' in JokeClientAdmin terminal to switch to 'proverb mode'. [note: this is case sensitive]
  — The JokeServer terminal window will log updates as you switch between 'joke mode' [or] 'proverb mode'....
    ...(for example, "Server Log: Operating in joke mode." [or] "Server Log: Operating in proverb mode.".
  — You can start/stop the JokeClientAdmin at anytime.
  — You can run JokeServer and JokeClient without JokeClientAdmin.
• Type a username once in the JokeClient terminal window.
  — Continue to press enter to receive four random jokes [or] four random proverbs [depending on current mode].
    — NOTE: you can swtich modes before a cycle ends and the data states would be maintained for each user and mode.
  — The JokeClient terminal Window will either display a joke or proverb [depending on current mode] before asking you...
    ...to press enter again [or] directly asks you to press enter again if the joke or proverb was a repeat within the...
    ...current cycle.
  — The cycle resets after four random jokes [or] four random proverbs [depending on current mode]...
    ...and aligns with the appropriate user.
    NOTE: The JokeServer terminal window will log Updates
    		— "Server Log: JOKE CYCLE COMPLETED."
  	  		— "Server Log: PROVERB CYCLE COMPLETED."
  — After cycle resets, jokes are re-randomized.
  — The JokeServer terminal window will log updates:
  	  — "Server Log: attempting retrieval of <JOKE> [or] <PROVERB> [depending on current mode] for <USERNAME>..."
  	  — "Server Log: JOKE CYCLE COMPLETED."
  	  — "Server Log: PROVERB CYCLE COMPLETED."
  	  — "Server Log: Operating in joke mode."
  	  — "Server Log: Operating in proverb mode."
• Maintains dialogue/states when running multiple clients and interleaving modes.
• Recognizes user and maintains dialogue/states accross terminal windows.
• Type 'quit' to exit the JokeClient. [note: this is case sensitive].
• Type 'quit to exit the JokeClientAdmin. [note: this is case sensitive].
• Type 'ctrl + c' to exit the JokeServer.

5. List of files needed for running the program:
[1] JokeServer.java
[2] JokeClient.java
[3] JokeClientAdmin.java
• Not required for running the progam, but good for reference on functionality and expected output:
	— JokeChecklistl.html
	— JokeLog.txt

5. Notes:
• Successful testing with five simultaneous clients
• I did NOT complete 'MULTIPLE JOKESERVERS (TEN PERCENT OF GRADE)' [OR] 'BRAGGING RIGHTS BELOW—WE LOVE THESE!'.
  — NOTE: start-up message indicating that I did not complete the entire program [see JokeServer terminal window].
• All other requirements were met—in accordance with my understanding of the instructions.
• I used the Inet files as a basis for all files: [JokeServer.java, JokeClient.java, and JokeClientAdmin.java].
----------------------------------------------------------*/

//import statements: used to retrieve specified libraries/packages
import java.io.*;
import java.net.*;


public class JokeClient {
	//declare static variable permanentUserId. static = available at the class level.
	//used to 'remember' the username which also doubles as a 'unique identifier' for retrieiving the correct state...
	//...on the server side. For example, I can log into termianl window one under user-name="Kyle" and then log into...
	//...terminal window two under user-name='Kyle' and the program will maintain the state as I switch back and forth.
	//NOTE: I realize this is less than an optimal from a security point-of-view. In case there are more than one...
	//..."Kyle"'s in the world.  Further development of this program would address these secutiry concerns by using...
	//...a random number generator to issue a UUID.  I also noted that 'real-world' applications usually require a...
	// unique user-name when registering for a new account so it seemed like a good starting point for this program.
	static String permanentUserId;

	public static void main (String args []) {
		String serverName;
		//if/else statement for fetching the command line args[]
		if (args.length < 1) serverName = "localhost";
		else serverName = args[0];
		System.out.println("Client Log: Welcome to Kyle Arick Kassen's JokeClient!"); //print to client terminal
		System.out.println("Client Log: Server=" + serverName + ", Port=4545"); //print to client terminal
			//try/catch exceptions block. Used for catching errors/exceptions.
			try {
				//as per the assignment requirements, only get the user name once
				System.out.print("Please enter your name: "); //print to client terminal, requesting user input
				System.out.flush(); //cleaning/clearing the buffer
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				String name;
				name = in.readLine();  //reading a line from the buffer.
				try {
					String nextJokeOrProverb;
					do {
						//keep pressing 'enter' to receive jokes and proverbs [depending on mode]
						System.out.print("press enter [or] type 'quit' to end: ");
						System.out.flush(); //cleaning/clearing the buffer
						nextJokeOrProverb = in.readLine(); //reading a line from the buffer
						//checking condition: if the user has NOT typed 'quit'
						if (nextJokeOrProverb.indexOf("quit") < 0) {
							clientComs(name, serverName); //calling the below method [clientComs()]
						}
					} while (nextJokeOrProverb.indexOf("quit") < 0); //print message if user types 'quit'
					System.out.println("Client Log: Cancelled by user request.");
				} catch (IOException x) {
					x.printStackTrace(); }
			} catch (IOException ioe) {
				System.out.println(ioe); }

	}
	//method/function for sending and/or receiving communications with the server.
	// This method/function is called when the user presses enter.
	static void clientComs(String name, String serverName) {
		//variable delcartions
		permanentUserId = name; //set static variable permanentUserId equal to name. static = available at the class level.
		Socket sock; //Socket type representing the 'socket' mechanism.
		BufferedReader fromServer; //for recieving communications from the server.
		PrintStream toServer; //for sending communications to the server.
		String textFromServer;
		//as required by the assignment: port number of JokeClient and different than JokeClientAdmin port number.
		int portNum = 4545;
		//try/catch exceptions block. Used for catching errors/exceptions.
		try {
			//a new ServerSocket type Object which takes port number and int type arguments
			sock = new Socket(serverName, portNum);
			// for receiving communications
			fromServer = new BufferedReader (new InputStreamReader (sock.getInputStream()));
			toServer = new PrintStream(sock.getOutputStream()); //for sending communications
			toServer.println(name); //sending to the server
			toServer.flush();  //cleaning/clearing the buffer. Used during outgoing coms for data/memory integrity.
			textFromServer = fromServer.readLine(); //reading line from the buffer.
			//if not null, print message sent from the server to the client's terminal.
			if (textFromServer != null) System.out.println(textFromServer);
			sock.close(); //closing the 'socket' mechanism.
		} catch (IOException x) {
			System.out.println ("Socket error."); 
			x.printStackTrace(); 
		}
	}
}