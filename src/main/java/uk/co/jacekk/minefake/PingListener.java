package uk.co.jacekk.minefake;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class PingListener extends Thread {
	
	private ServerSocket socket;
	
	public PingListener(String host, int port){
		try{
			this.socket = new ServerSocket();
			this.socket.bind(new InetSocketAddress(host, port));
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		while (!this.socket.isClosed()){
			try{
				Socket socket = this.socket.accept();
				
				socket.setSoTimeout(250);
				socket.setSoLinger(true, 60);
				
				DataInputStream input = new DataInputStream(socket.getInputStream());
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				
				int packetID = input.read();
				
				if (packetID == 254){
					MineFake.log("Server ping from " + socket.getRemoteSocketAddress().toString());
					
					String ping = "\u00a7" + "\u0031" + "\0" + MineFake.PROTOCOL_VERSION + "\0" + MineFake.GAME_VERSION + "\0" + MineFake.MOTD + "\0" + MineFake.PLAYER_COUNT + "\0" + MineFake.PLAYER_MAX;
					
					output.writeByte(0xff);
					output.writeShort(ping.length());
					output.writeChars(ping);
					
					output.flush();
				}
				
				Thread.sleep(100);
				
				socket.shutdownOutput();
				socket.shutdownInput();
				socket.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
