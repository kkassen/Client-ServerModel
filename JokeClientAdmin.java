/*--------------------------------------------------------

1. Kyle Arick Kassen / Date: June 20, 2021 - June 27, 2021
2. Citations/References: Professor Clark Elliott, DePaul University
*/

import java.io.*;
import java.net.*;

public class JokeClientAdmin {
    public static void main(String args[]) {
        String serverName;
        if (args.length < 1) serverName = "localhost";
        else serverName = args[0];
        System.out.println("Admin Log: Welcome to Kyle Arick Kassen's JokeClientAdmin!");
        System.out.println("Admin Log: Server=" + serverName + ", Port=5050"); 
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            String modus;
            do {
                System.out.println("*** Administrative Options Menu ***\ntype any of the following options " +
                        "to update the Server's mode of operation:\n[0]='joke' | [1]='proverb' | [2]='quit'");
                System.out.flush(); //cleaning/clearing the buffer.
                modus = in.readLine(); //reading a line from the buffer.
                if (modus.indexOf("quit") < 0){ 
                    clientAdminComs(modus,serverName);
                }
            } while (modus.indexOf("quit") < 0);
                System.out.println("Admin Log: Cancelled by user request.");
            } catch(IOException x){
                x.printStackTrace();
            }
        }
        static void clientAdminComs (String modus, String serverName) {
            Socket sock; 
            BufferedReader fromServer; 
            PrintStream toServer; 
            String textFromServer;
            int portNum = 5050;
            try {
                sock = new Socket(serverName, portNum);
                fromServer = new BufferedReader (new InputStreamReader (sock.getInputStream()));
                toServer = new PrintStream(sock.getOutputStream()); 
                toServer.println(modus); 
                toServer.flush();
                textFromServer = fromServer.readLine(); 
                if (textFromServer != null) System.out.println(textFromServer);
                sock.close(); 
            } catch (IOException x) {
                System.out.println("Socket error.");
                x.printStackTrace();
            }
    }
}
