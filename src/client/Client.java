package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
	Socket socket = null;
	BufferedReader input = null;
	OutputStreamWriter output = null;
	Thread thread = null;


	public Client(String host,int port){
		try{
			socket = new Socket(host,port);

			input = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			output = new OutputStreamWriter(socket.getOutputStream());
		}catch(IOException e){
			socket =null;
			System.out.println("Client Err!!!");
		}
	}

	public boolean write(String Message){
		try{
			output.write(Message + "\r\n");
			output.flush();
		}catch(IOException e){
			System.out.println("Miss Send");
			return false;
		}
		return true;
	}

	public String read(){
		String readString = null;
		try{
			readString = input.readLine();

			System.out.println(readString);

		}catch(IOException e){
			System.out.println("Can't read message!!!");
		}
		return readString;
	}


	public void close(){
		try{
			System.out.println("Client#Close()");
			socket.close();
			socket = null;
		}catch(IOException e){
			e.printStackTrace(System.err);
		}
	}

}
