package uk.co.jacekk.minefake;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MineFake {
	
	public static String PROTOCOL_VERSION = "51";
	public static String GAME_VERSION = "1.5.1";
	public static String MOTD = "A Minecraft Server";
	public static String PLAYER_COUNT = "1";
	public static String PLAYER_MAX = "20";
	
	public static String ADDRESS = "192.168.1.20";
	public static int PORT = 25565;
	
	public synchronized static void log(String line){
		System.out.println("[" + (new SimpleDateFormat("dd/mm/yyyy HH:mm:ss")).format(new Date()) + "]: " + line);
	}
	
	public static void main(String[] args){
		MineFake.log("Listening on " + MineFake.ADDRESS + ":" + MineFake.PORT);
		
		(new PingListener(MineFake.ADDRESS, MineFake.PORT)).start();
	}
	
}
