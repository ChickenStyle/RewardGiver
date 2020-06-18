package me.chickenstyle.rewards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.netty.util.internal.ThreadLocalRandom;

public class EntityDamageEvents implements Listener {
	
	HashMap<ActiveMob,HashMap<UUID,Double>> damageStats = new HashMap<ActiveMob,HashMap<UUID,Double>>();
	
	@EventHandler
	public void onEnityDmage(EntityDamageByEntityEvent e) {
		BukkitAPIHelper api = MythicMobs.inst().getAPIHelper();
		if (api.isMythicMob(e.getEntity()) && e.getDamager() instanceof Player) {
			ActiveMob mob = api.getMythicMobInstance(e.getEntity());
			Player player = (Player) e.getDamager();
			if (MobsRewards.hasMythicMob(mob.getType())) {
				if (damageStats.containsKey(mob)) {
					HashMap<UUID,Double> damage = damageStats.get(mob);
					if (damage.containsKey(player.getUniqueId())) {
						damage.put(player.getUniqueId(), damage.get(player.getUniqueId()) + e.getFinalDamage());
						damageStats.put(mob, damage);
					} else {
						damage.put(player.getUniqueId(), e.getFinalDamage());
						damageStats.put(mob, damage);
					}
				} else {
					HashMap<UUID,Double> damage = new HashMap<UUID,Double>();
					damage.put(player.getUniqueId(), e.getDamage());
					damageStats.put(mob, damage);
				}
			}
		} else if (api.isMythicMob(e.getEntity()) && e.getDamager() instanceof Projectile) {
			Projectile shoot = (Projectile) e.getDamager();
			if (shoot.getShooter() instanceof Player) {
				Player player = (Player) shoot.getShooter();
				ActiveMob mob = api.getMythicMobInstance(e.getEntity());
				if (MobsRewards.hasMythicMob(mob.getType())) {
					if (damageStats.containsKey(mob)) {
						HashMap<UUID,Double> damage = damageStats.get(mob);
						if (damage.containsKey(player.getUniqueId())) {
							damage.put(player.getUniqueId(), damage.get(player.getUniqueId()) + e.getDamage());
							damageStats.put(mob, damage);
						} else {
							damage.put(player.getUniqueId(), e.getDamage());
							damageStats.put(mob, damage);
						}
					} else {
						HashMap<UUID,Double> damage = new HashMap<UUID,Double>();
						damage.put(player.getUniqueId(), e.getDamage());
						damageStats.put(mob, damage);
					}
				}
			}
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		BukkitAPIHelper api = MythicMobs.inst().getAPIHelper();
		if (api.isMythicMob(e.getEntity())) {
			ActiveMob mob = api.getMythicMobInstance(e.getEntity());
			if (MobsRewards.hasMythicMob(mob.getType())) {
				if (damageStats.containsKey(mob)) {
					ArrayList<String> sortedList = sortHashmap(damageStats.get(mob));	
					ArrayList<String> message = (ArrayList<String>) Main.getInstance().getConfig().get("broadcastMessage");
					//Send message
					if (MobsRewards.doBroadcast(mob.getType())) {
						for (Player player:Bukkit.getServer().getOnlinePlayers()) {
							for (String line:message) {
								player.sendMessage(Utils.Color(replacePlaceHolders(line, mob, player, sortedList)));
							}
						}
					} else {
						for (String data:sortedList) {
							if (!data.split(":")[0].equals("null")) {
								Player player = Bukkit.getPlayer(UUID.fromString(data.split(":")[0]));
								for (String line:message) {
									player.sendMessage(Utils.Color(replacePlaceHolders(line, mob, player, sortedList)));
								}
							}
						}
					}
					
					//Give rewards
					if (!sortedList.get(0).split(":")[0].equals("null")) {
						Player player = Bukkit.getPlayer(UUID.fromString(sortedList.get(0).split(":")[0]));
						HashMap<Integer,String> commands = new HashMap<>();
						for (String data:MobsRewards.getFirstPlaceCommands(mob.getType())) {
							int chance = Integer.valueOf(data.split("-")[0]);
							String command = data.split("-")[1];
							commands.put(chance, command);
						}
						randomCommand(commands, player);
					}
					if (!sortedList.get(1).split(":")[0].equals("null")) {
						Player player = Bukkit.getPlayer(UUID.fromString(sortedList.get(1).split(":")[0]));
						HashMap<Integer,String> commands = new HashMap<>();
						for (String data:MobsRewards.getSecondPlaceCommands(mob.getType())) {
							int chance = Integer.valueOf(data.split("-")[0]);
							String command = data.split("-")[1];
							commands.put(chance, command);
						}
						randomCommand(commands, player);
					}
					if (!sortedList.get(2).split(":")[0].equals("null")) {
						Player player = Bukkit.getPlayer(UUID.fromString(sortedList.get(2).split(":")[0]));
						HashMap<Integer,String> commands = new HashMap<>();
						for (String data:MobsRewards.getThirdPlaceCommands(mob.getType())) {
							int chance = Integer.valueOf(data.split("-")[0]);
							String command = data.split("-")[1];
							commands.put(chance, command);
						}
						randomCommand(commands, player);
					}
					damageStats.remove(mob);
				}
			}
		}
	}
	
	private ArrayList<String> sortHashmap(HashMap<UUID,Double> map) {
		ArrayList<String> sorted = new ArrayList<String>();
		ArrayList<UUID> checkedPlayers = new ArrayList<>();
		if (map.size() >= 3) {
			for (int i = 0; i < map.size();i++) {
				UUID topDamage = null;
				//Check for the highest damage
				for (UUID uuid:map.keySet()) {
					if (!checkedPlayers.contains(uuid)) {
						if (topDamage != null) {
							if (map.get(topDamage) < map.get(uuid)) {
								topDamage = uuid;
							}
						} else {
							topDamage = uuid;
						}
					}
				}
				sorted.add(topDamage + ":" + map.get(topDamage));
				checkedPlayers.add(topDamage);
			}
			
			//29dollars - 10,ChickenStyle - 50
		} else {
			int nullsAmount = 3 - map.size(); // 1
			for (int i = 0; i < map.size();i++) {
				UUID topDamage = null;
				//Check for the highest damage
				for (UUID uuid:map.keySet()) {
					if (!checkedPlayers.contains(uuid)) {
						if (topDamage != null) {
							if (map.get(topDamage) < map.get(uuid)) {
								topDamage = uuid;
							}
						} else {
							topDamage = uuid;
						}
					}
				}
				sorted.add(topDamage + ":" + map.get(topDamage));
				checkedPlayers.add(topDamage);
			}
			for (int i = 0;i < nullsAmount;i++) {
				sorted.add("null" + ":" + "0");
			}
		}
		System.out.println(sorted.size());
		return sorted;
	}
	
	private boolean containsPlayer(ArrayList<String> list,Player player) {
		boolean contains = false;
		for (String data:list) {
			if (!data.split(":")[0].equals("null")) {
				if (UUID.fromString(data.split(":")[0]).equals(player.getUniqueId())) {
					contains = true;
				}
			}
		}
		return contains;
	}
	
	
	private String replacePlaceHolders(String line,ActiveMob mob,Player player,ArrayList<String> list) {
		String[] places = {"first","second","third"};
		String newLine = line;
		
		newLine = newLine.replace("{mob-type}", mob.getDisplayName());
		for (int i = 0; i <3;i++) {
			if (list.get(i).split(":")[0].equals("null")) {
				newLine = newLine.replace("{" + places[i] + "Place}", "None")
				.replace("{" + places[i] + "Place_damage}", "0");
			} else {
				newLine =newLine.replace("{" + places[i] + "Place}", Bukkit.getPlayer(UUID.fromString(list.get(i).split(":")[0])).getName())
				.replace("{" + places[i] + "Place_damage}", (int) Math.round(Double.valueOf(list.get(i).split(":")[1])) + "");
			}
		}
		
		if (containsPlayer(list, player)) {
			int position = -99;
			int damage = 0;
			for (int i = 0;i < list.size();i++) {
				if (!list.get(i).split(":")[0].equals("null")) {
					if (UUID.fromString(list.get(i).split(":")[0]).equals(player.getUniqueId())) {
						position = i + 1;
						damage = (int)Math.round(Double.valueOf(list.get(i).split(":")[1]));
					}
				}
			}
			newLine = newLine.replace("{damage}", damage + "").replace("{position}", position + "");
			
		} else {
			newLine = newLine.replace("{damage}", "0").replace("{position}", "none");
	 	}
		return newLine;
				   
	}
	
	public void randomCommand(HashMap<Integer,String> map, Player player){
        int sizeMap = map.size();

        int total = 0;
        for (int i : map.keySet()){
            total = total + i;
        }
        for (Integer i : map.keySet()){
            int chnaceResult = ThreadLocalRandom.current().nextInt(0,total + 1);
            if (chnaceResult < i +1){
                String s = map.get(i).replace("{player}", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),s);
                return;
            }
            sizeMap = sizeMap -1;
        }

        if (sizeMap == 0){
            randomCommand(map,player);
        }

    }
	
}
