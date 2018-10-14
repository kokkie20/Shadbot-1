package me.shadorc.shadbot.command.game.rpsx;

import me.shadorc.shadbot.utils.object.Emoji;

public enum Handsign {
	ROCK("Rock", Emoji.GEM),
	PAPER("Paper", Emoji.LEAF),
	SCISSORS("Scissors", Emoji.SCISSORS),
	SPOCK("Spock", Emoji.SPOCK),
	LIZARD("Lizard", Emoji.LIZARD);

	private final String handsign;
	private final Emoji emoji;

	Handsign(String handsign, Emoji emoji) {
		this.handsign = handsign;
		this.emoji = emoji;
	}

	public String getHandsign() {
		return handsign;
	}

	public String getRepresentation() {
		return emoji + " " + handsign;
	}

	public boolean isSuperior(Handsign other) {
		return this.equals(Handsign.ROCK) && other.equals(Handsign.SCISSORS) //Rock crushes scissors
				|| this.equals(Handsign.ROCK) && other.equals(Handsign.LIZARD) // Rocks crushes lizard
						
				|| this.equals(Handsign.PAPER) && other.equals(Handsign.ROCK) // Papers covers rock
				|| this.equals(Handsign.PAPER) && other.equals(Handsign.SPOCK) // Paper disproves spock
				
						
				|| this.equals(Handsign.SCISSORS) && other.equals(Handsign.PAPER) // scissors cuts paper
				|| this.equals(Handsign.SCISSORS) && other.equals(Handsign.LIZARD) // scissors decapitates lizard
				
				|| this.equals(Handsign.LIZARD) && other.equals(Handsign.SPOCK) // Lizard poisons spock
				|| this.equals(Handsign.LIZARD) && other.equals(Handsign.PAPER) // Lizard eats paper
				
				|| this.equals(Handsign.SPOCK) && other.equals(Handsign.SCISSORS) // Spock smashes scissors
				|| this.equals(Handsign.SPOCK) && other.equals(Handsign.ROCK); // Spock vaporizes rock
	}

}