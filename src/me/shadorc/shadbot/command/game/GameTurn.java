package me.shadorc.shadbot.command.game;

public abstract class GameTurn {

	abstract public boolean parseInput(String input);

	abstract public String getInputErrorMessage();
}
