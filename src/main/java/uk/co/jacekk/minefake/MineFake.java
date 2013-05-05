package uk.co.jacekk.minefake;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MineFake {
	
	public static String PROTOCOL_VERSION = "58";
	public static String GAME_VERSION = "1.5.2";
	public static String MOTD = "9F388EC0-38C2-4FAE-91D9-445FA3A9AFFA";
	public static String WORLD_NAME = "world";
	public static String PLAYER_COUNT = "1";
	public static String PLAYER_MAX = "20";
	public static List<String> PLAYERS = Arrays.asList("Notch", "_jeb");
	public static List<String> PLUGINS = Arrays.asList("WorldEdit 5.4", "CommandBook 2.2");
	
	public static String ADDRESS = "192.168.1.20";
	public static int PORT = 25565;
	public static int QUERY_PORT = 25565;
	
	public synchronized static void log(String line){
		System.out.println("[" + (new SimpleDateFormat("dd/mm/yyyy HH:mm:ss")).format(new Date()) + "]: " + line);
	}
	
	public static void main(String[] args){
		MineFake.log("Listening on " + MineFake.ADDRESS + ":" + MineFake.PORT);
		
		(new PingListener(MineFake.ADDRESS, MineFake.PORT)).start();
		(new QueryListener(MineFake.ADDRESS, MineFake.QUERY_PORT)).start();
	}
	
}
