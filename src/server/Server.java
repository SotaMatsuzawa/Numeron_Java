package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Thread {
	final int MAX_CHANNELS = 256; //最大接続数
	Channel channel[] = new Channel[MAX_CHANNELS];
	ServerSocket serversocket;
	int port;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		int p = 28000;
		if(args.length > 0){
			p = Integer.parseInt(args[0]);
		}
		new Server(p);
	}

	public Server(int port){
		this.port = port;
		this.start();
	}

	public void serverClose(){
		try{
			System.out.println("Server#Close()");
			serversocket.close();
			serversocket = null;
		}catch(IOException e){
			e.printStackTrace(System.err);
		}
	}

	public void clientClose(){
		System.out.println("Server#Close "+ channel.length);
		for(int i = 0; i < channel.length; i++){
			System.out.println("Server#Close(" + i + ")");
			channel[i].close();
			channel[i] = null;
		}
	}

	public void quit(){
		clientClose();
		serverClose();
		System.exit(1);
	}

	public void run(){
		int i;
		try{
			serversocket = new ServerSocket(port);
			System.out.println("Server Start: the port number is "+ port);
			while(true){
				for(i = 0; i < MAX_CHANNELS; i++){
					if(channel[i] == null){
						break;
					}
				}
				if(i == MAX_CHANNELS){
					return;
				}
				Socket socket = serversocket.accept();
				channel[i] = new Channel(socket,this);
			}
		}catch(IOException e){
			System.out.println("Server Err!");
			return;
		}
	}



	//全チャネルへメッセージを送信
	synchronized void broadcast(String message){
		for(int i = 0; i < MAX_CHANNELS; i++){
			if(channel[i] != null && channel[i].socket != null){
				channel[i].write(message);
			}
		}
	}

	// 特定の相手へ送信
	// send a message to a specific person
	synchronized boolean singleSend(String person, String message) {
		boolean ret = false;
		System.out.println("To "+person + ": " + message);
		for (int i = 0; i < MAX_CHANNELS; i++) {
			if (channel[i] != null && channel[i].socket != null) {
				if (person.equals(channel[i].handle)) {
					channel[i].write(message);
					ret = true;
				}
			}
		}
		return ret;
	}

	//対戦相手を見つける、nameは呼び出した側のハンドルネーム
	public String findopponent(String name,String roomnum){
		String opponent;
		for (int i = 0; i < MAX_CHANNELS; i++) {
			if (channel[i] != null && channel[i].socket != null) {
				if (roomnum.equals(channel[i].roomnumber) && name!=channel[i].handle) {
					opponent = channel[i].handle;
					channel[i].write("対戦相手は "+name);
					try{
					Thread.sleep(100);
					}catch(Exception e){
						System.out.println(e);
					}
					return opponent;
				}
			}
		}
		return null;
	}

	//敵の数字を取得する nameは呼び出した側のハンドルネーム
	public String findopponentnumber(String name,String roomnum){
		String opponentnumber;
		for (int i = 0; i < MAX_CHANNELS; i++) {
			if (channel[i] != null && channel[i].socket != null) {
				if (roomnum.equals(channel[i].roomnumber) && name!=channel[i].handle) {
					opponentnumber = channel[i].mynumber;
					try{
					Thread.sleep(100);
					}catch(Exception e){
						System.out.println(e);
					}
					return opponentnumber;
				}
			}
		}
		return null;
	}

	//channelのturnフィールドを変更する
	public void ChangeTurn(String myname,String tekiname){
		//System.out.println("呼び出せてはいる");
		for(int i =0;i < MAX_CHANNELS; i++){
			if (channel[i] != null && channel[i].socket != null) {
				if(channel[i].handle.equals(myname)){//自分のターンフラグを変更
					//System.out.println("自分のターン変更");
					if(channel[i].turn == true){
						channel[i].turn = false;
					}else{
						channel[i].turn = true;
					}
				}else if(channel[i].handle.equals(tekiname)){//相手のターンフラグを変更
					if(channel[i].turn == true){
						channel[i].turn = false;
					}else{
						//System.out.println("相手のターン変更");
						channel[i].turn = true;
					}
				}
			}
		}
	}

	//turnを更新する
	public boolean isTurn(String myname){
		for(int i =0;i < MAX_CHANNELS; i++){
			if (channel[i] != null && channel[i].socket != null) {
				if(channel[i].handle.equals(myname)){
					return channel[i].turn;
				}
			}
		}
		return false;
	}
}


