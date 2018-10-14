package me.shadorc.shadbot.command.game;

import sx.blah.discord.handle.obj.IUser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Random;

public abstract class AbstractGame<turnType extends GameTurn> {
	private GameState gameState;
	private IUser[] players;
	private int activePlayerIndex = 0;
	private int winnerIndex = -1;

	/**
	 * gets a short name of the game, this name is used as input to create a new game and as an identifier in the database
	 *
	 * @return codeName of the game
	 */
	public abstract String getCodeName();

	/**
	 * a full version of the name, this is used to display
	 *
	 * @return full game name
	 */
	public abstract String getFullname();

	/**
	 * receives a new instance of turnType
	 *
	 * @return new instance of turnType
	 */
	public final turnType getGameTurnInstance() {
		Class<?> turnTypeClass = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		turnType type;
		try {
			type = (turnType) turnTypeClass.getConstructor().newInstance();
			return type;
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected IUser getPlayer(int index) {
		return players[index];
	}

	protected int getActivePlayerIndex() {
		return activePlayerIndex;
	}

	protected IUser getActivePlayer() {
		return players[activePlayerIndex];
	}

	protected void setWinner(int playerIndex) {
		winnerIndex = playerIndex;
	}

	protected int getWinnerIndex() {
		return winnerIndex;
	}

	/**
	 * the total amount of players in a game
	 *
	 * @return total players
	 */
	public abstract int getTotalPlayers();

	/**
	 * Resets the game
	 */
	public void reset() {
		winnerIndex = getTotalPlayers();
		players = new IUser[getTotalPlayers()];
		for (int i = 0; i < getTotalPlayers(); i++) {
			players[i] = null;
		}
		gameState = GameState.INITIALIZING;
	}


	public GameState getGameState() {
		return gameState;
	}

	/**
	 * attempts to play a turn
	 *
	 * @param player   the player
	 * @param turnInfo the details about the move
	 * @return turn successfully played?
	 */
	public final boolean playTurn(IUser player, turnType turnInfo) {
		if (!(gameState.equals(GameState.IN_PROGRESS) || gameState.equals(GameState.READY))) {
			return false;
		}
		if (!isTurnOf(player) || !isValidMove(player, turnInfo)) {
			return false;
		}
		doPlayerMove(player, turnInfo);
		if (isTheGameOver()) {
			gameState = GameState.OVER;
		}
		endTurn();
		return true;
	}

	/**
	 * adds a player to the game
	 *
	 * @param player the player
	 * @return if it added the player to the game or not
	 */
	public final boolean addPlayer(IUser player) {
		if (!gameState.equals(GameState.INITIALIZING)) {
			return false;
		}
		for (int i = 0; i < getTotalPlayers(); i++) {
			if (players[i] == null) {
				players[i] = player;
				if (i == (getTotalPlayers() - 1)) {
					activePlayerIndex = new Random().nextInt(getTotalPlayers());
					gameState = GameState.READY;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * shifts the active player index over to the next one
	 */
	private void endTurn() {
		activePlayerIndex = (activePlayerIndex + 1) % getTotalPlayers();
	}

	/**
	 * checks if the game is still in progress
	 *
	 * @return true if the game is over, false if its still in progress
	 */
	protected abstract boolean isTheGameOver();

	/**
	 * @param player to check
	 * @return is it players turn?
	 */
	public boolean isTurnOf(IUser player) {
		return players[activePlayerIndex].equals(player);
	}

	/**
	 * checks if the attempted move is a valid one
	 *
	 * @param player   the player
	 * @param turnInfo the details about the move
	 * @return is a valid move?
	 */
	public abstract boolean isValidMove(IUser player, turnType turnInfo);

	/**
	 * play the turn
	 *
	 * @param player   the player
	 * @param turnInfo the details about the move
	 */
	protected abstract void doPlayerMove(IUser player, turnType turnInfo);


	/**
	 * are we still waiting for more players?
	 *
	 * @return well?
	 */
	public boolean waitingForPlayer() {
		return gameState.equals(GameState.INITIALIZING);
	}

}
