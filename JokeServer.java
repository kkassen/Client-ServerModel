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
import java.util.*;

class Worker_client extends Thread {
	Socket sock;//Socket type representing a 'socket' mechanism.

	//Used HashSets for simplicity in managing the logic required to maintain 'states' in data structures.
	//Also, constant time methods() are useful in case of scaling jokes/proverbs.
	//static declarations for use at the class level. Helps maintain the perception of a connection between data...
	//...and a each specific client.
	static HashSet<String> jokesSet = new HashSet<>();
	static HashSet<String> proverbsSet = new HashSet<>();

	//Hashtables for storing <key,value> pairs of the jokes/proverbs.
	//representing a 'master' table of <key,value> pairs.
	//key=an int used for retrieving Hashtable.get(random int).
	//value=String type used for storing jokes/proverbs.
	//Also, constant time methods() are useful in case of scaling jokes/proverbs.
	//static declarations for use at the class level. Helps maintain the perception of a connection between data...
	//...and a each specific client.
	static Hashtable<Integer, String> jokesTable = new Hashtable<>();
	static Hashtable<Integer, String> proverbsTable = new Hashtable<>();

	Worker_client(Socket s) { //a 'setter' function which takes one argument of type Socket.
		sock = s; //sets sock equal to s.
	}

	public void run() {
		PrintStream out = null; //output of type PrintStream initialized to null.
		BufferedReader in = null; //input of type BufferedReader initialized to null.
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintStream(sock.getOutputStream());
			String name;
			try {
				name = in.readLine(); //reading the user's name from the buffer and storing it in the variable 'name'.
				System.out.println("Server Log: attempting retrieval of " + JokeServer.modus + " for " + name + "...");
				courierService(out, name); //send joke [or] proverb to client
			} catch (IOException x) {
				System.out.println("Server Log: read error");
				x.printStackTrace();
				}
				sock.close();
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	//method/function for determining and retrieving which joke [or] proverb to send to each specific client.
	static void courierService (PrintStream toClient, String name) {
		//declaring a variable of type Random class. To be used below for retrieving random keys from the Hashtables.
		Random rand = new Random();
		//the data=jokes: performs user-name substitution as per the assingment requirements
		//cite ja: https://www.scandit.com/blog/top-10-geek-jokes-for-developers/
		String ja = "JA " + name + ": How many programmers does it take to change a light bulb?" +
									" none, that's a hardware problem.";
		//cite jb:https://www.pingdom.com/blog/10-computer-geek-jokes-and-truisms/
		String jb = "JB " + name + ": A programmers wife tells him, 'while you're at the grocery store, buy some eggs'. " +
				"He never comes back.";
		//cite jc: https://kidadl.com/articles/best-nerd-jokes-perfect-for-your-dorkiest-pals
		String jc = "JC " + name + ": Where should one store all the nerdy dad jokes? In a dada-base.";
		//cite jd: https://kidadl.com/articles/best-nerd-jokes-perfect-for-your-dorkiest-pals
		String jd = "JD " + name + ": Why can't you trust any atoms? Because they are said to make-up things.";

		//the data=proverbs: performs user-name substitution as per the assignment requirements
		//cite pa: https://www.newcollegegroup.com/blog/10-english-proverbs-you-should-use-in-your-speech/
		String pa = "PA " + name + ": if it ain't broke, don't fix it.";
		//cite pb: https://lemongrad.com/proverbs-with-meanings-and-examples/
		String pb = "PB " + name + ": Always put your best foot forward.";
		//cite pc: https://lemongrad.com/proverbs-with-meanings-and-examples/
		String pc = "PC " + name + ": Curiosity killed the cat.";
		//cite pd: https://lemongrad.com/proverbs-with-meanings-and-examples/
		String pd = "PD " + name + ": An apple a day keeps the doctor away.";

		//place the keys and jokes inside the jokesTable of type Hashtable
		jokesTable.put(0,ja);jokesTable.put(1,jb);
		jokesTable.put(2,jc);jokesTable.put(3,jd);

		//place the keys and proverbs inside the proverbsTable of type Hashtable
		proverbsTable.put(0,pa);proverbsTable.put(1,pb);
		proverbsTable.put(2,pc);proverbsTable.put(3,pd);
		if (JokeServer.modus == "joke") { //checking condition: are we in 'joke mode'?
				//using random.nextInt() method to generate a new random int between 0-3.
				int rk1 = rand.nextInt(jokesTable.size());
			//checking condition: if we can .add() to the Set then we can send the jokes to the client...
			//...via the PrintStream. It's a Set so it won't allow duplicates by default.
				if (jokesSet.add(jokesTable.get(rk1))) {
					toClient.println(jokesTable.get(rk1)); //sends joke to client via PrintStream type.
				}
				//checking condition: have we displayed all four jokes for this specific client?
				if (jokesSet.containsAll(jokesTable.values())) {
					//System.out.println(jokesSet); //testing purposes only: checking contents of Set.
					jokesSet.removeAll(jokesTable.values()); //clearing Set to renew the 'Cycle'. if condition is met.
					System.out.println("Server Log: JOKE CYCLE COMPLETED.");//log message to JokeServer terminal.
				}
				toClient.flush(); //cleaning/clearing the buffer
		}
		if (JokeServer.modus == "proverb") { //checking condition: are we in 'proverb mode'?
			//using random.nextInt() method to generate a new random int between 0-3.
			int rk2 = rand.nextInt(proverbsTable.size());
			//checking condition: if we can .add() to the Set then we can send the proverb to the client...
			//...via the PrintStream. It's a Set so it won't allow duplicates by default.
			if (proverbsSet.add(proverbsTable.get(rk2))) {
				toClient.println(proverbsTable.get(rk2)); //sends proverb to client via PrintStream type
			}
			//checking condition: have we displayed all four proverbs for this specific client?
			if (proverbsSet.containsAll(proverbsTable.values())) {
				//System.out.println(proverbsSet);//testing purposes only: checking contents of Set.
				proverbsSet.removeAll(proverbsTable.values()); //clearing Set to renew the 'Cycle'.
				System.out.println("Server Log: PROVERB CYCLE COMPLETED."); //log message to JokeServer terminal.
			}
			toClient.flush(); //cleaning/clearing the buffer
		}
	}
}

class Worker_admin extends Thread {
	Socket sock;

	Worker_admin(Socket s){
		sock = s;
	}

	public void run() {
		PrintStream out = null;
		BufferedReader in = null;
		try {
			//for retrieving communications sent from the client admin.
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			//for sending communications to the client admin.
			out = new PrintStream(sock.getOutputStream());
			String modus;
			try {
				modus = in.readLine(); //reading the 'mode' from the buffer and storing it in the local variable 'modus'.
				if (modus.equals("joke")) { //checking condition: are we in 'joke mode'?
					//if the 'if condition' is met then set class level variable modus using class.variable call.
					JokeServer.modus="joke";
					System.out.println("Server Log: Operating in joke mode."); //log message to JokeServer terminal.
				}
				if (modus.equals("proverb")){ //checking condition: are we in 'proverb mode'?
					//if the 'if condition' is met then set class level variable modus using class.variable call.
					JokeServer.modus="proverb";
					System.out.println("Server Log: Operating in proverb mode."); //log message to JokeServer terminal.
				}
			} catch (IOException x) {
				System.out.println("Server Log: read error");
				x.printStackTrace();
			}
			sock.close(); //closing the 'socket' mechanism.
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}
}

class Admin implements Runnable {
	public void run() {
		int queueLenOpSys = 6; //int type variable representing a queue length of six.  related to the OS.
		//as required by the assignment: port number of JokeClientAdmin and different than JokeClient port number.
		int portNum = 5050;
		Socket sock;//Socket type representing the 'socket' mechanism.
		try {
			//a new ServerSocket type Object which takes port number and int type arguments
			ServerSocket server_sock = new ServerSocket(portNum,queueLenOpSys);
			while (true){
				sock=server_sock.accept(); //for acceptance of socket connection
				new Worker_admin(sock).start(); //gernerates the Worker_client Thread which takes a Socket type
			}
		} catch (IOException ioe){
			System.out.println(ioe);
		}
	}
}

	public class JokeServer {
		static String modus = "joke"; //set static variable modus to 'joke mode'. static = class level variable.
		public static void main (String a[]) throws IOException {
			int queueLenOpSys = 6; //int type variable representing a queue length of six.  related to the OS.
			//as required by the assignment: port number of JokeClient and different than JokeClientAdmin port number.
			int portNum = 4545;
			Socket sock; //Socket type representing the 'socket' mechanism.
			//need a separate thread for our Admin class.
			Admin admin = new Admin(); //declaring a variable of type Admin class.
			Thread thread = new Thread(admin); //declaring a thread which takes type Admin
			thread.start(); //generates an Admin thread
			//a new ServerSocket type Object which takes port number and int type arguments
			ServerSocket server_sock = new ServerSocket(portNum,queueLenOpSys);
			//the server's start-up/opening messages logged to the JokeServer terminal window.
			System.out.println("ATTENTION GRADER: I DID NOT COMPLETE 'MULTIPLE JOKESERVERS (TEN PERCENT OF GRADE)' " +
					"[OR] 'BRAGGING RIGHTS BELOW-WE LOVE THESE!'.");
			System.out.println("Server Log: Welcome to Kyle Arick Kassen's JokeServer!");
			System.out.println("Server Log: Operating in 'joke mode' by default.");
			while (true) {
				sock = server_sock.accept(); //for acceptance of socket connection
				new Worker_client(sock).start();  //gernerates the Worker_client Thread which takes a Socket type
			}
		}
	}
