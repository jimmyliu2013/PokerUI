package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sound.midi.Receiver;

import interfaces.SocketInterface;

public class SocketServer extends Thread {

	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private SocketInterface socketInterface;
	private String currentInput = null;
	private String currentResponse = null;

	public static final int port = 8080;

	public SocketServer(SocketInterface socketInterface) {
		super();
		this.socketInterface = socketInterface;
	}

	@Override
	public void run() {

		super.run();

		try {
			System.out.println("Opening server socket on port " + port);
			serverSocket = new ServerSocket(port);
			clientSocket = serverSocket.accept();
			System.out.println("accept client");

			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

			while (true) {
				String event = inFromClient.readLine();

//while (true) {
//	int value = 0;
//	value = inFromClient.read();
//	System.out.println("here");
////     char c = (char)value;
//     
//     // prints character
//     System.out.println(value);
//     System.out.println("after");
//}

				if (event != null) {
					System.out.println("event string is : " + event);
					if (socketInterface != null) {
						socketInterface.onMessageReceived(event);
					}
					String userInput = queue.take();
					System.out.println("write to socket: " + userInput + "\0");
					writer.print(userInput + "\0");
					writer.flush();
				} else {
					System.out.println("receive null event");
				}
			}

//			serverSocket.close();
//			System.out.println("Closing server socket");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void pushEvent(String event) {

		if (queue.size() >= 1) {
			throw new RuntimeException("there is a pending message, check the other side");
		} else {
			if (!queue.offer(event)) {
				System.out.println("failed to offer");
			}
		}
	}
}
