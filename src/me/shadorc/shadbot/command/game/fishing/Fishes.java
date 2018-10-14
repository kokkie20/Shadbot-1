package me.shadorc.shadbot.command.game.fishing;

public enum Fishes {
	Frog("FROG","Frog","frog","",4),
	Boot("BOOT","Boot","boot","",1),
	Poop("POOP","Poop","poop","This kinda stinks...",0),
	Sandal("SANDAL", "Sandal","sandal","",1),
	Crab("CRAB","Crab","crab","",6),
	Octopus("OCTOPUS","Octopus","octopus","",7),
	Turtle("TURTLE","Turtle","turtle","",10),
	Fish("FISH","Fish","fish","",5),
	Blowfish("BLOWFISH","Blowfish","blowfish","",4),
	Dolphin("DOLPHIN","Dolphin","dolphin","",25),
	Whale("WHALE","Whale","whale2","",40),
	Crocodile("CROCODILE","Crocodile","crocodile","",10),
	Shark("SHARK","Shark","shark","",13),
	Squid("SQUID","Squid","squid","",12),
	Treasure("TREASURE","Treasure","gem","Woah you found the rare treasure!!!",500),
	Shrimp("SHRIMP","Shrimp","shrimp","",3),
	Fishing_Pole("FISHING_POLE","Pole","fishing_pole_and_fish","",15),
	Nothing("NOTHING","Nothing","x","",0),
	Shell("SHELL","Shell","shell","",5);

	private final String key;
	private final String name;
	private final String emoji;
	private final String description;
	private final long value;

	Fishes(String Key, String Name, String Emoji, String Description, int Value) {
		this.key = Key;
		this.name = Name;
		this.emoji = Emoji;
		this.description = Description;
		this.value = Value;
	}

	public String getKey() {
		return key;
	}
	
	public String getEmoji() {
		return emoji;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getName() {
		return name;
	}
	
	public long getValue() {
		return value;
	}

}