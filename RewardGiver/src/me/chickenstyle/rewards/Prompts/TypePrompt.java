package me.chickenstyle.rewards.Prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import me.chickenstyle.rewards.CustomMob;
import me.chickenstyle.rewards.RewardGiverCommand;
import me.chickenstyle.rewards.Utils;

public class TypePrompt extends StringPrompt{
	
	@Override
	public String getPromptText(ConversationContext context) {
		return Utils.Color("&7Enter a valid MythicMob type!");
	}
	
	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		BukkitAPIHelper api = MythicMobs.inst().getAPIHelper();
		
		if (api.getMythicMob(input) != null) {
			Player player = (Player) context.getForWhom();
			CustomMob mob = RewardGiverCommand.mobs.get(player.getUniqueId());
			mob.setType(api.getMythicMob(input));
			RewardGiverCommand.mobs.put(player.getUniqueId(), mob);
			return new BroadcastPrompt();
		} else {
			return new TypePrompt();
		}
	}


}
