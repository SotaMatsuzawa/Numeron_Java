package client;

/* チャットクライアント用GUIプログラム */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class NumeronGUI extends JPanel implements Runnable, ActionListener {
	JTextField inputArea; // 入力用テキストフィールド
	JTextArea freeArea; // 出力用テキストエリア
	Client client = null; // Client クラス
	String host = "localhost";
	JTextField hostField, portField;
	JButton connectBut ,closeBut, quitBut;
	int port = 28000;
	Thread thread = null;
	JScrollPane scrollpane = null;
	final char controlChar = (char)05;
	final char separateChar = (char)06;

	//panelの定義
	public String[] PanelNames = {"Numeron","Register","Play","Result"};

	public static void main(String args[]) {
		JFrame f = new JFrame("Numer0n");
		NumeronGUI numeron = new NumeronGUI();
		numeron.setBackground(Color.white);
		numeron.setPreferredSize(new Dimension(600, 400));
		f.add("Center", numeron);
		f.setTitle("Numer0nGUI");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(true);
		f.pack();
		f.setVisible(true);

	}

	public NumeronGUI() {
		// アプレットのレイアウト
		setLayout(new BorderLayout());
		JPanel p = new JPanel();
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		p.setLayout(new GridLayout());
		p1.setLayout(new GridLayout(1, 5, 10, 10));
		p1.add(new JLabel("Host = ", JLabel.RIGHT));
		p1.add(hostField = new JTextField(host));
		p1.add(new JLabel("Port = ", JLabel.RIGHT));
		p1.add(portField = new JTextField("" + port));
		p2.add(connectBut = new JButton("Connect"));
		p2.add(quitBut = new JButton("Quit"));
		p.add("North", p1);
		p.add("South", p2);

		JPanel handlename = new JPanel();	//名前入力欄
		handlename.setLayout(new BorderLayout());
		handlename.add("West", new JLabel("Input form:"));
		handlename.add("Center", inputArea = new JTextField("", 30));

		freeArea = new JTextArea();	//サーバからの文字表示欄
		freeArea.setFont(new Font("SansSerif", Font.BOLD, 24));
		JScrollPane scrollpane = new JScrollPane(freeArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollpane.setVisible(true);


		freeArea.setEditable(false);
		add("North", handlename);
		add("Center", scrollpane);
		add("South", p);

		// イベント処理の登録
		connectBut.addActionListener(this);
		quitBut.addActionListener(this);
		inputArea.addActionListener(this);

			}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop() {
		if (thread != null) {
			thread = null;
		}
	}

	// クライアントのメインルーチン
	public void run() {
		String s;
		while (thread != null) {
			s = client.read(); // メッセージの読みとり
			if (s == null) {
				clientClose();
			} else {
				//appendTextArea(s); // テキストエリアへ出力
				freeArea.append(s + "\n"); // テキストエリアへ出力
			}
		}
	}

	// 回線の接続を行なう
	public boolean clientOpen() {
		if (client == null) {
			host = hostField.getText();
			port = Integer.valueOf(portField.getText()).intValue();
			client = new Client(host, port); // Clientクラスの呼び出し
			if (client.socket == null) { // 接続失敗？
				System.out.println("Connect Err");
				client = null;
				return false;
			}
			else
				return true;
		}
		else
			return false;
	}

	// 接続の切断を行なう
	public void clientClose() {
		if (client != null) {
			client.close();
			client = null;
			thread = null;
		}
	}

	// メッセージの送信を行う
	public void writeMes(String mes) {
		try {
			client.write(mes);
		} catch (Exception e) {
			System.out.println("Detected an exception while sending a message");
			e.printStackTrace(System.err);
		}
	}

	// 特定の相手のみにメッセージを送信するプロセス
	// Process to send a message to a specific person
	public void sendMessageProcessToPerson(String person, String message) {
		String mes = String.valueOf(controlChar) + "sendPerson"
				+ String.valueOf(separateChar) + person + String.valueOf(separateChar) + message;
		System.out.println("To server: "+mes);
		writeMes(mes);
	}

	public void change(JPanel panel) {
		//ContentPaneにはめ込まれたパネルを削除
		this.removeAll();

		super.add(panel);//パネルの追加
		validate();//更新
		repaint();//再描画


	}


	// イベント処理
	// Event handling
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == connectBut) { // 回線接続を実行
			if (clientOpen())
				start();
		} else if (e.getSource() == quitBut) { // アプレットの終了
			stop();
			clientClose();
			System.exit(1);
		} else if (e.getSource() == inputArea && client != null) {
			// テキストフィールド内の文字列をサーバーへ送信する
			writeMes(inputArea.getText());
			inputArea.setText("");
		}


		}
	}


