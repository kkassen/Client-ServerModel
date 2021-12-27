/*--------------------------------------------------------
1. Kyle Arick Kassen / Date: June 20, 2021 - June 27, 2021

2. Java version used (java -version):
C:\Users\IQ1006\JavaPrograms>java -version
openjdk version "11.0.4" 2019-07-16 LTS
OpenJDK Runtime Environment Corretto-11.0.4.11.1 (build 11.0.4+11-LTS)
OpenJDK 64-Bit Server VM Corretto-11.0.4.11.1 (build 11.0.4+11-LTS, mixed mode)

3. Instructions for Compiling:
C:\Users\IQ1006\JavaPrograms>javac JokeServer.java
C:\Users\IQ1006\JavaPrograms>javac JokeClient.java
C:\Users\IQ1006\JavaPrograms>javac JokeClientAdmin.java

4. Notes:
• Citations/References: Professor Clark Elliott, DePaul University
----------------------------------------------------------*/


import java.io.*;
import java.net.*;


public class JokeClient {
	static String permanentUserId;

	public static void main (String args []) {
		String serverName;
		if (args.length < 1) serverName = "localhost";
		else serverName = args[0];
		System.out.println("Client Log: Welcome to Kyle Arick Kassen's JokeClient!"); 
		System.out.println("Client Log: Server=" + serverName + ", Port=4545"); 
			try {
				System.out.print("Please enter your name: "); 
				System.out.flush(); 
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				String name;
				name = in.readLine(); 
				try {
					String nextJokeOrProverb;
					do {
						System.out.print("press enter [or] type 'quit' to end: ");
						System.out.flush(); 
						nextJokeOrProverb = in.readLine(); 
						if (nextJokeOrProverb.indexOf("quit") < 0) {
							clientComs(name, serverName); 
						}
					} while (nextJokeOrProverb.indexOf("quit") < 0); 
					System.out.println("Client Log: Cancelled by user request.");
				} catch (IOException x) {
					x.printStackTrace(); }
			} catch (IOException ioe) {
				System.out.println(ioe); }

	}
	static void clientComs(String name, String serverName) {
		permanentUserId = name; 
		Socket sock; 
		BufferedReader fromServer; 
		PrintStream toServer; 
		String textFromServer;
		int portNum = 4545;
		try {
			sock = new Socket(serverName, portNum);
			fromServer = new BufferedReader (new InputStreamReader (sock.getInputStream()));
			toServer = new PrintStream(sock.getOutputStream()); 
			toServer.println(name); 
			toServer.flush();  
			textFromServer = fromServer.readLine(); 
			if (textFromServer != null) System.out.println(textFromServer);
			sock.close(); 
		} catch (IOException x) {
			System.out.println ("Socket error."); 
			x.printStackTrace(); 
		}
	}
}
