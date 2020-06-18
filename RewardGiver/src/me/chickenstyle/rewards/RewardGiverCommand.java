package me.chickenstyle.rewards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import me.chickenstyle.rewards.Prompts.TypePrompt;

public class RewardGiverCommand implements CommandExecutor {
	public static HashMap<UUID,CustomMob> mobs = new HashMap<>();
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			switch (args[0]) {
			case "add":
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (player.hasPermission("RewardGiver.add")) {
						ConversationFactory factory = new ConversationFactory(Main.getInstance());
						Conversation conversation = factory.withFirstPrompt(new TypePrompt()).withLocalEcho(true).buildConversation(player);
						conversation.begin();
						mobs.put(player.getUniqueId(), new CustomMob(null,false
								,new ArrayList<String>()
								,new ArrayList<String>()
								,new ArrayList<String>()));
					} else {
						player.sendMessage(ChatColor.RED + "You dont have permission to use this command!");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "You cannot use this command through console!");
				}
			break;
			
			
			case "reload":
				if (sender.hasPermission("RewardGiver.reload")) {
					MobsRewards.configReload();
					Main.getInstance().reloadConfig();
					sender.sendMessage(ChatColor.GREEN + "Configs have been reloaded!");
				} else {
					sender.sendMessage(ChatColor.RED + "You dont have permission to use this command!");
				}
			break;
			
			default:
				
			break;
			}
		}
		
		return false;
	}

}
