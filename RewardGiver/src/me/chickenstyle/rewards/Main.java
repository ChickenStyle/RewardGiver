package me.chickenstyle.rewards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	
	private static Main mainInstance;
	
	@Override
	public void onEnable() {
		mainInstance = this;
		
		Bukkit.getPluginManager().registerEvents(new EntityDamageEvents(),this);
		
		this.getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		getCommand("rewardgiver").setExecutor(new RewardGiverCommand());
		
		new MobsRewards(this);
		
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Rewards-Giver has been enabled!");
	}
	
	public static Main getInstance() {
		return mainInstance;
	}
}
