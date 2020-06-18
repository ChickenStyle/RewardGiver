package me.chickenstyle.rewards.Prompts;

import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import me.chickenstyle.rewards.CustomMob;
import me.chickenstyle.rewards.RewardGiverCommand;
import me.chickenstyle.rewards.Utils;

public class BroadcastPrompt extends BooleanPrompt{

	@Override
	public String getPromptText(ConversationContext context) {
		return Utils.Color("&7Do you want to broadcast the kill message to all players?");
	}
	

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, boolean boo) {
		Player player = (Player) context.getForWhom();
		CustomMob mob = RewardGiverCommand.mobs.get(player.getUniqueId());
		mob.setBroadcastMessage(boo);
		RewardGiverCommand.mobs.put(player.getUniqueId(),mob);
		return new FirstPlacePrompt();
	}

}
