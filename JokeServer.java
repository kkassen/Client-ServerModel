/*--------------------------------------------------------
1. Kyle Arick Kassen / Date: June 20, 2021 - June 27, 2021
2. Citations/References: Professor Clark Elliott, DePaul University
----------------------------------------------------------*/

import java.io.*;
import java.net.*;
import java.util.*;

class Worker_client extends Thread {
	Socket sock;
	static HashSet<String> jokesSet = new HashSet<>();
	static HashSet<String> proverbsSet = new HashSet<>();
	static Hashtable<Integer, String> jokesTable = new Hashtable<>();
	static Hashtable<Integer, String> proverbsTable = new Hashtable<>();

	Worker_client(Socket s) { .
		sock = s; 
	}

	public void run() {
		PrintStream out = null; 
		BufferedReader in = null; 
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintStream(sock.getOutputStream());
			String name;
			try {
				name = in.readLine(); 
				System.out.println("Server Log: attempting retrieval of " + JokeServer.modus + " for " + name + "...");
				courierService(out, name); 
			} catch (IOException x) {
				System.out.println("Server Log: read error");
				x.printStackTrace();
				}
				sock.close();
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}


	static void courierService (PrintStream toClient, String name) {
		Random rand = new Random();
		String ja = "JA " + name + ": How many programmers does it take to change a light bulb?" +
									" none, that's a hardware problem.";
		String jb = "JB " + name + ": A programmers wife tells him, 'while you're at the grocery store, buy some eggs'. " +
				"He never comes back.";
		String jc = "JC " + name + ": Where should one store all the nerdy dad jokes? In a dada-base.";
		String jd = "JD " + name + ": Why can't you trust any atoms? Because they are said to make-up things.";

		String pa = "PA " + name + ": if it ain't broke, don't fix it.";
		String pb = "PB " + name + ": Always put your best foot forward.";
		String pc = "PC " + name + ": Curiosity killed the cat.";
		String pd = "PD " + name + ": An apple a day keeps the doctor away.";

		jokesTable.put(0,ja);jokesTable.put(1,jb);
		jokesTable.put(2,jc);jokesTable.put(3,jd);

		proverbsTable.put(0,pa);proverbsTable.put(1,pb);
		proverbsTable.put(2,pc);proverbsTable.put(3,pd);
		if (JokeServer.modus == "joke") { 
				int rk1 = rand.nextInt(jokesTable.size());
				if (jokesSet.add(jokesTable.get(rk1))) {
					toClient.println(jokesTable.get(rk1)); 
				}
				if (jokesSet.containsAll(jokesTable.values())) {
					jokesSet.removeAll(jokesTable.values()); 
					System.out.println("Server Log: JOKE CYCLE COMPLETED.");
				}
				toClient.flush();
		}
		if (JokeServer.modus == "proverb") { 
			int rk2 = rand.nextInt(proverbsTable.size());
			if (proverbsSet.add(proverbsTable.get(rk2))) {
				toClient.println(proverbsTable.get(rk2)); 
			}
			if (proverbsSet.containsAll(proverbsTable.values())) {
				proverbsSet.removeAll(proverbsTable.values());
				System.out.println("Server Log: PROVERB CYCLE COMPLETED."); 
			}
			toClient.flush(); 
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
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintStream(sock.getOutputStream());
			String modus;
			try {
				modus = in.readLine(); 
				if (modus.equals("joke")) { 
					JokeServer.modus="joke";
					System.out.println("Server Log: Operating in joke mode."); 
				}
				if (modus.equals("proverb")){ 
					JokeServer.modus="proverb";
					System.out.println("Server Log: Operating in proverb mode."); 
				}
			} catch (IOException x) {
				System.out.println("Server Log: read error");
				x.printStackTrace();
			}
			sock.close(); 
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}
}

class Admin implements Runnable {
	public void run() {
		int queueLenOpSys = 6; 
		int portNum = 5050;
		Socket sock;
		try {
			ServerSocket server_sock = new ServerSocket(portNum,queueLenOpSys);
			while (true){
				sock=server_sock.accept(); 
				new Worker_admin(sock).start(); 
			}
		} catch (IOException ioe){
			System.out.println(ioe);
		}
	}
}

	public class JokeServer {
		static String modus = "joke"; 
		public static void main (String a[]) throws IOException {
			int queueLenOpSys = 6; 
			int portNum = 4545;
			Socket sock; 
			Admin admin = new Admin(); 
			Thread thread = new Thread(admin); 
			thread.start(); 
			ServerSocket server_sock = new ServerSocket(portNum,queueLenOpSys);
			System.out.println("Server Log: Welcome to Kyle Arick Kassen's JokeServer!");
			System.out.println("Server Log: Operating in 'joke mode' by default.");
			while (true) {
				sock = server_sock.accept(); 
				new Worker_client(sock).start();  
			}
		}
	}
