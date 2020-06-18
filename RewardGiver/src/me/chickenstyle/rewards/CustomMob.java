package me.chickenstyle.rewards;

import java.util.ArrayList;

import io.lumine.xikage.mythicmobs.mobs.MythicMob;

public class CustomMob {
	private MythicMob type;
	private boolean broadcastMessage;
	private ArrayList<String> firstPlaceCommands;
	private ArrayList<String> secondPlaceCommands;
	private ArrayList<String> thirdPlaceCommands;
	
	
	public CustomMob(MythicMob type, boolean broadcastMessage, ArrayList<String> firstPlaceCommands,
			ArrayList<String> secondPlaceCommands, ArrayList<String> thirdPlaceCommands) {
		this.type = type;
		this.broadcastMessage = broadcastMessage;
		this.firstPlaceCommands = firstPlaceCommands;
		this.secondPlaceCommands = secondPlaceCommands;
		this.thirdPlaceCommands = thirdPlaceCommands;
	}


	public MythicMob getType() {
		return type;
	}


	public void setType(MythicMob type) {
		this.type = type;
	}


	public boolean isBroadcastMessage() {
		return broadcastMessage;
	}


	public void setBroadcastMessage(boolean broadcastMessage) {
		this.broadcastMessage = broadcastMessage;
	}


	public ArrayList<String> getFirstPlaceCommands() {
		return firstPlaceCommands;
	}


	public void setFirstPlaceCommands(ArrayList<String> firstPlaceCommands) {
		this.firstPlaceCommands = firstPlaceCommands;
	}


	public ArrayList<String> getSecondPlaceCommands() {
		return secondPlaceCommands;
	}


	public void setSecondPlaceCommands(ArrayList<String> secondPlaceCommands) {
		this.secondPlaceCommands = secondPlaceCommands;
	}


	public ArrayList<String> getThirdPlaceCommands() {
		return thirdPlaceCommands;
	}


	public void setThirdPlaceCommands(ArrayList<String> thirdPlaceCommands) {
		this.thirdPlaceCommands = thirdPlaceCommands;
	}
	
	
	
	
}
