package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Channel extends Thread {
	JUDGE judge = new JUDGE();
	Server server;
	Socket socket = null;
	BufferedReader input;
	OutputStreamWriter output;
	String handle;
	String playertype;
	String roomnumber;
	String mynumber;
	String tekinumber;
	boolean turn;

	String ex = "926";//AIの最初の予測値
	String ca ="0123456789";//candidate number　最初は０～９まで
	String ex_number;
	List<String> old_list = new ArrayList<>();

	final char controlChar = (char)05;
	final char separateChar = (char)06;

	Channel(Socket s,Server cs){
		this.server = cs;
		this.socket = s;
		this.start();
	}

	synchronized void write(String s){
		try{
			output.write(s + "\r\n");
			output.flush();
		}catch(IOException e){
			System.out.println("Write Err");
			close();
		}
	}

	public void run(){
		List<String> s_list = new ArrayList<>();//クライアントからの入力を受け取り、ためておくリスト
		String s;
		String opponent = null;
		try{
			input = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			output = new OutputStreamWriter(socket.getOutputStream());
			write("# Welcome to Numr0n Game");
			write("# Please input your name");
			handle = input.readLine();
			System.out.println("new user: "+ handle);
			while(true){//HOST or GUEST入力待ち
				write("INPUT YOUR TYPE(HOST or GUEST or AI)");
				playertype = input.readLine();
				if(playertype.equals("HOST")){
					Random rnd = new Random();
					s = String.valueOf(rnd.nextInt(100)+100);
					write("[HOST]ルーム番号: "+s);
					break;
				}else if(playertype.equals("GUEST")){
					write("[GUEST]ルーム番号を入力してください");
					s = input.readLine();
					write("[GUEST]ルーム番号: "+s);
					break;
				}else if(playertype.equals("AI")){
					write("[vs AIモード]");
					Random rnd = new Random();
					s = String.valueOf(rnd.nextInt(100)+100);
					write("[HOST]ルーム番号: "+s);
					break;
				}else{
					write("ルーム番号入力でエラー");
				}
			}

			roomnumber  = s;	//roomnumberの決定
			System.out.println(roomnumber);
			write("対戦相手待ち");

			if(playertype.equals("AI")){
				//AIとの対戦
				write("自分の数字を決めてください（*3桁の数値*0～9まで*数字の被りなし）");

				boolean firstnum = false;
				while(firstnum == false){//最初の自分の数字が上記の条件を満たしているか
					mynumber = input.readLine();
					firstnum = judge.isNumber(mynumber);
				}
				write("自分の数字: "+ mynumber);
				write(handle + "からスタートです");
				tekinumber="864";
				NumeronAI numeron = new NumeronAI();
				while(true){
					//ゲームスタート
					boolean finish = false;

					s = input.readLine();

					if(s == null){
						close();
					}else{
						System.out.println(s);
						boolean numsuccess = judge.isNumber(s);//数字が定義内
						if (numsuccess) {
							JUDGE eatbite = judge.EatBite(s, tekinumber);
							finish = judge.Finish(eatbite.eat);//3eatになったかどうか
							write("["+ s +"] eat: " +String.valueOf(eatbite.eat) +" bite: "+ String.valueOf(eatbite.bite));

							//ここからAIのターン
							JUDGE AIeatbite = judge.EatBite(ex, mynumber);

							NumeronAI squeeze = numeron.Squeeze(AIeatbite.eat,AIeatbite.bite,ex,ca,old_list);
							if(squeeze.new_can_list.size()<300){
								//System.out.println(Arrays.toString(squeeze.new_can_list.toArray()));
								//System.out.println(squeeze.can_num);
								ex_number = numeron.choice(squeeze.new_can_list,squeeze.can_num);
							}else{
								Random rnd = new Random();
								int index = rnd.nextInt(100);
								ex_number = squeeze.new_can_list.get(index);
							}
							old_list = new ArrayList<>(squeeze.new_can_list);
							//System.out.println("残り候補数: " + String.valueOf(old_list.size()));
							write("AIの予測値:" + ex + "		[残り候補数: " + String.valueOf(old_list.size())+"個]");
							//System.out.println("AIの予測値: "+ ex);
							if(mynumber.equals(ex)){
								write("#################you lose#################");
							}
							ex = ex_number;

								//ここまでがAIのターン
						} else {
							write(" did not send such a number");
						}
					}
					if(finish){
						write("#################you win#################");
					}
				}

			}else{//vs人間
				while(opponent == null){//対戦相手待ち
					opponent = server.findopponent(handle,roomnumber);
				}
				//write("対戦相手が決まりました");
				write("自分の数字を決めてください（*3桁の数値*0～9まで*数字の被りなし）");

				boolean firstnum = false;
				while(firstnum == false){//最初の自分の数字が上記の条件を満たしているか
					mynumber = input.readLine();
					firstnum = judge.isNumber(mynumber);
				}
				write("自分の数字: "+ mynumber);

				while(tekinumber == null){//敵の数値を取得するまで待つ
					tekinumber = server.findopponentnumber(handle, roomnumber);
				}

				if(playertype.equals("HOST")){
					turn = true;
				}else{
					turn =false;
				}

				write("HOSTプレイヤーからスタートです");
				while(true){
					//ゲームスタート
					boolean finish = false;
					while(true){
						//ターンの確認
						s_list.add(input.readLine());//入力を入れておく
						turn = server.isTurn(handle);//turnの確認
						if(turn == true){
							break;
						}
					}

					s = s_list.get(s_list.size()-1);
					s_list.clear();

					if(s == null){
						close();
					}else{
						System.out.println(s);
						boolean numsuccess = judge.isNumber(s);//数字が定義内
						if (numsuccess) {
							//write("judge ok");
							boolean connectsuccess = server.singleSend(opponent,"[相手の予測] "+s);//相手がいる
							if(connectsuccess){
								//write("相手が存在する");
								JUDGE eatbite = judge.EatBite(s, tekinumber);
								finish = judge.Finish(eatbite.eat);//3eatになったかどうか
								write("		[自分の予測]"+ s +" eat: " +String.valueOf(eatbite.eat) +" bite: "+ String.valueOf(eatbite.bite));
								server.ChangeTurn(handle, opponent);//ターンの切り替え
							}else{
								write("did not find opponent");
							}
						} else {
							write(" did not send such a number");
						}


						}
					if(finish){
						write("#################you win#################");
						server.singleSend(opponent, "#################you lose#################");
					}

					}
			}



		}catch(IOException e){
			System.out.println("Exception occurs in Channel: "+handle);
		}
	}
	public void close(){
		try{
			input.close();
			output.close();
			socket.close();
			socket = null;
			//server.broadcast("回線切断 : " + handle);
		}catch(IOException e){
			System.out.println("Close Err");
		}
	}



}
