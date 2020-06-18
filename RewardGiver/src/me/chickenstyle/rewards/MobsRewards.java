package me.chickenstyle.rewards;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.file.YamlConfiguration;

import io.lumine.xikage.mythicmobs.mobs.MythicMob;

public class MobsRewards {
	
	
	/*
	 *    Mobs:
		  {MobType}:
		    broadcastMessage: {true/false}
		    firstPlaceCommands:
		    - "{chance}:{command}"
		    - "{chance}:{command}"
		    - "{chance}:{command}"
		   secondPlaceCommands:
		    - "{chance}:{command}"
		    - "{chance}:{command}"
		    - "{chance}:{command}"
		   thirdPlaceCommands:
		    - "{chance}:{command}"
		    - "{chance}:{command}"
		    - "{chance}:{command}"
	 *    
	 *    
	 *    
	 *
	 * 
	 * 
	 * 
	 */
	
	private static File file;
	private static YamlConfiguration config;
	public MobsRewards(Main main) {
  	  file = new File(main.getDataFolder(), "Mobs-Rewards.yml");
  	 if (!file.exists()) {
  		 try {
				 file.createNewFile();
		    	 config = YamlConfiguration.loadConfiguration(file);
		    	  	try {
		    				config.save(file);
		    		    	config = YamlConfiguration.loadConfiguration(file);
		    			} catch (IOException e) {
		    				e.printStackTrace();
		    			}
			} catch (IOException e) {
				e.printStackTrace();
			}
  		 
  	 }
  	config = YamlConfiguration.loadConfiguration(file);
   }
    
    static public void addMythicMob(CustomMob mob) {
    		config.set("Mobs." + mob.getType().toString() + ".broadcastMessage", mob.isBroadcastMessage());
    		config.set("Mobs." + mob.getType().toString() + ".firstPlaceCommands", mob.getFirstPlaceCommands());
    		config.set("Mobs." + mob.getType().toString() + ".secondPlaceCommands", mob.getSecondPlaceCommands());
    		config.set("Mobs." + mob.getType().toString() + ".thirdPlaceCommands", mob.getThirdPlaceCommands());
    	  	try {
    			config.save(file);
    	    	config = YamlConfiguration.loadConfiguration(file);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    }
    

    	
    static public boolean hasMythicMob(MythicMob type) {
    	if (config.get("Mobs." + type.toString()) != null) {
    		return true;
    	}
    	return false;
    }
    
    
    @SuppressWarnings("unchecked")
	static public ArrayList<String> getFirstPlaceCommands(MythicMob type) {
    	return (ArrayList<String>) config.get("Mobs." + type.toString() + ".firstPlaceCommands");
    }
    
    @SuppressWarnings("unchecked")
	static public ArrayList<String> getSecondPlaceCommands(MythicMob type) {
    	return (ArrayList<String>) config.get("Mobs." + type.toString() + ".secondPlaceCommands");
    }
    
    @SuppressWarnings("unchecked")
	static public ArrayList<String> getThirdPlaceCommands(MythicMob type) {
    	return (ArrayList<String>) config.get("Mobs." + type.toString() + ".thirdPlaceCommands");
    }
    
    static public boolean doBroadcast(MythicMob type) {
    	return config.getBoolean("Mobs." + type.toString() + ".broadcastMessage");
    }
	
	static public void configReload() {
   	 config = YamlConfiguration.loadConfiguration(file);
		try {
			config.save(file);
			config = YamlConfiguration.loadConfiguration(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
