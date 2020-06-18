package me.chickenstyle.rewards.Prompts;

import java.util.ArrayList;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import me.chickenstyle.rewards.CustomMob;
import me.chickenstyle.rewards.RewardGiverCommand;
import me.chickenstyle.rewards.Utils;

public class FirstPlacePrompt extends StringPrompt{
	
	@Override
	public String getPromptText(ConversationContext context) {
		return Utils.Color("&7Enter a chance and a command you want to run &6for the first place! &7(like this {chance}-{command}) \n"
				+ "if you want to stop type 'stop'!");
	}
	
	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		if (!input.equals("stop")) {
			Player player = (Player) context.getForWhom();
			CustomMob mob = RewardGiverCommand.mobs.get(player.getUniqueId());
			ArrayList<String> commands = mob.getFirstPlaceCommands();
			commands.add(input);
			mob.setFirstPlaceCommands(commands);
			RewardGiverCommand.mobs.put(player.getUniqueId(), mob);
			return new FirstPlacePrompt();
		} else {
			return new SecondPlacePrompt();
		}
	}


}
