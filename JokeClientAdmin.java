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

//import statements: used to retrieve specified libraries/packages
import java.io.*;
import java.net.*;

public class JokeClientAdmin {
    //this program is dedicated to allowing the server to switch between 'joke mode' and 'proverb mode'.
    //NOTE: you can run JokeServer and JokeClient without this program.
    //the program connects through a seperate port [5050], as per the assignment requirements.
    //as per the assignment requirements, you can start and stop this program at anytime.
    public static void main(String args[]) {
        String serverName;
        //if/else statement for fetching the command line args[]
        if (args.length < 1) serverName = "localhost";
        else serverName = args[0];
        System.out.println("Admin Log: Welcome to Kyle Arick Kassen's JokeClientAdmin!");//print to ClientAdmin terminal
        System.out.println("Admin Log: Server=" + serverName + ", Port=5050"); //print to ClientAdmin terminal
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            String modus;
            do {
                //print to ClientAdmin terminal. Requesting user input.  The input determines 'mode' of operation.
                System.out.println("*** Administrative Options Menu ***\ntype any of the following options " +
                        "to update the Server's mode of operation:\n[0]='joke' | [1]='proverb' | [2]='quit'");
                System.out.flush(); //cleaning/clearing the buffer.
                modus = in.readLine(); //reading a line from the buffer.
                if (modus.indexOf("quit") < 0){ //checking condition: if the user has NOT typed 'quit'.
                    //call the method [clientAdminComs()] to send 'mode' of operation to the server.
                    clientAdminComs(modus,serverName);
                }
            } while (modus.indexOf("quit") < 0); //print message if user types 'quit'
                System.out.println("Admin Log: Cancelled by user request.");
            } catch(IOException x){
                x.printStackTrace();
            }
        }
        //method/function for sending and/or receiving communications with the server
        //this method/function is called when the user types a 'mode of operation'. The mode is sent to the server.
        static void clientAdminComs (String modus, String serverName) {
            //variable declarations
            Socket sock; //Socket type representing the 'socket' mechanism.
            BufferedReader fromServer; //for recieving communications from the server.
            PrintStream toServer; //for sending communications to the server.
            String textFromServer;
            //as required by the assignment: port number of JokeClientAdmin and different than JokeClient port number.
            int portNum = 5050;
            //try/catch exceptions block. Used for catching errors/exceptions.
            try {
                //a new ServerSocket type Object which takes port number and int type arguments
                sock = new Socket(serverName, portNum);
                // for receiving communications
                fromServer = new BufferedReader (new InputStreamReader (sock.getInputStream()));
                toServer = new PrintStream(sock.getOutputStream()); //for sending communications
                toServer.println(modus); //sending to the server
                toServer.flush(); //cleaning/clearing the buffer. Used during outgoing coms for data/memory integrity.
                textFromServer = fromServer.readLine(); //reading line from the buffer.
                //if not null, print message sent from the server to the clientAdmin's terminal.
                if (textFromServer != null) System.out.println(textFromServer);
                sock.close(); //closing the 'socket' mechanism.
            } catch (IOException x) {
                System.out.println("Socket error.");
                x.printStackTrace();
            }
    }
}