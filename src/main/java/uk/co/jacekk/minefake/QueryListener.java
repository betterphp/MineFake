package uk.co.jacekk.minefake;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class QueryListener extends Thread {
	
	private DatagramSocket socket;
	
	private byte[] data;
	private DatagramPacket packet;
	
	public QueryListener(String host, int port){
		try{
			this.socket = new DatagramSocket(port, InetAddress.getByName(host));
			this.socket.setSoTimeout(500);
		}catch (Exception e){
			e.printStackTrace();
		}
		
		this.data = new byte[1460];
		this.packet = new DatagramPacket(this.data, this.data.length);
	}
	
	private void send(byte[] abyte, DatagramPacket packet){
		try{
			this.socket.send(new DatagramPacket(abyte, abyte.length, packet.getSocketAddress()));
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private byte[] getFullReply(DatagramPacket datagrampacket) throws IOException {
		StringBuilder pluginList = new StringBuilder();
		
		for (String plugin : MineFake.PLUGINS){
			pluginList.append("; ");
			pluginList.append(plugin);
		}
		
		QueryReply reply = new QueryReply(this.data.length);
		
		reply.write((int) 0);
		reply.write(new byte[]{0, 0, 0, 1});
		reply.write("splitnum");
		reply.write((int) 128);
		reply.write((int) 0);
		reply.write("hostname");
		reply.write(MineFake.MOTD);
		reply.write("gametype");
		reply.write("SMP");
		reply.write("game_id");
		reply.write("MINECRAFT");
		reply.write("version");
		reply.write(MineFake.GAME_VERSION);
		reply.write("plugins");
		reply.write("CraftBukkit on Bukkit " + MineFake.GAME_VERSION + "-R1.0: " + pluginList.substring(2).toString());
		reply.write("map");
		reply.write(MineFake.WORLD_NAME);
		reply.write("numplayers");
		reply.write(MineFake.PLAYER_COUNT);
		reply.write("maxplayers");
		reply.write(MineFake.PLAYER_MAX);
		reply.write("hostport");
		reply.write(Integer.toString(MineFake.PORT));
		reply.write("hostip");
		reply.write(MineFake.ADDRESS);
		reply.write((int) 0);
		reply.write((int) 1);
		reply.write("player_");
		reply.write((int) 0);
		
		for (String player : MineFake.PLAYERS){
			reply.write(player);
		}
		
		reply.write((int) 0);
		
		return reply.getBytes();
	}
	
	private void parsePacket(DatagramPacket packet) throws IOException {
		byte[] data = packet.getData();
		int length = packet.getLength();
		SocketAddress address = packet.getSocketAddress();
		
		if (3 <= length && -2 == data[0] && -3 == data[1]){
			switch (data[2]){
				case 0:
					if (15 == length){
						MineFake.log("Full Stat [" + address + "]");
						this.send(this.getFullReply(packet), packet);
					}else{
						MineFake.log("Stat [" + address + "]");
						QueryReply reply = new QueryReply(this.data.length);
						
						reply.write((int) 0);
						reply.write(new byte[]{0, 0, 0, 1});
						reply.write(MineFake.MOTD);
						reply.write("SMP");
						reply.write(MineFake.WORLD_NAME);
						reply.write(MineFake.PLAYER_COUNT);
						reply.write(MineFake.PLAYER_MAX);
						reply.write((short) MineFake.PORT);
						reply.write(MineFake.ADDRESS);
						
						this.send(reply.getBytes(), packet);
					}
					
				case 9:
					MineFake.log("Challenge [" + address + "]");
					this.send((new QueryChallenge(packet)).getChallengeResponse(), packet);
			}
		}else{
			MineFake.log("Invalid packet [" + address + "]");
		}
	}
	
	@Override
	public void run(){
		while (!this.socket.isClosed()){
			try{
				this.socket.receive(this.packet);
				this.parsePacket(this.packet);
			}catch (SocketTimeoutException e){
				
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
