package me.shadorc.shadbot.command.game.fishing;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import me.shadorc.shadbot.core.command.AbstractCommand;
import me.shadorc.shadbot.core.command.CommandCategory;
import me.shadorc.shadbot.core.command.Context;
import me.shadorc.shadbot.core.command.annotation.Command;
import me.shadorc.shadbot.data.db.Database;
import me.shadorc.shadbot.data.fishing.DatabaseFishing;
import me.shadorc.shadbot.exception.IllegalCmdArgumentException;
import me.shadorc.shadbot.exception.MissingArgumentException;
import me.shadorc.shadbot.ratelimiter.RateLimiter;
import me.shadorc.shadbot.utils.BotUtils;
import me.shadorc.shadbot.utils.LogUtils;
import me.shadorc.shadbot.utils.embed.HelpBuilder;
import me.shadorc.shadbot.utils.object.Emoji;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageBuilder;

@Command(category = CommandCategory.GAME, names = { "fishing" })
public class FishingCmd extends AbstractCommand {

	public static org.json.simple.JSONObject catches;
	private final RateLimiter rateLimiter = new RateLimiter(1, 10, ChronoUnit.SECONDS);

	@Override
	public void execute(Context context) throws MissingArgumentException, IllegalCmdArgumentException {
		IUser Owner = context.getGuild().getOwner();
		String AuthorID = context.getAuthor().getStringID();
		if (!rateLimiter.isLimited(context.getChannel(), context.getAuthor())) {
			if (context.getArg().equals("CreateDB") && context.getAuthor().equals(Owner)) {
				try {
					DatabaseFishing.CreateAllUserFishingDB(context);
					BotUtils.sendMessage("Done creating the Databases of all current users", context.getChannel());
				} catch (JSONException | IOException e) {
					// TODO Auto-generated catch block
					LogUtils.error(e, "An error occurred while creating Fishing DB.");
					BotUtils.sendMessage("Something went wrong, log has been created...", context.getChannel());
				}
			} else if (context.getArg().matches("Inventory|inv|inventory")) {
				try {
					catches = DatabaseFishing.getJsonObjectInformation(AuthorID);
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					LogUtils.error(e, "An error occurred while getting fishing inventory");
					BotUtils.sendMessage("Something went wrong, log has been created...", context.getChannel());
				}
				MessageBuilder message = new MessageBuilder(context.getClient()).withChannel(context.getChannel())
						.withEmbed(
								new EmbedBuilder()
										.withDesc("Ahoy Matey " + context.getAuthor().mention() + "! \n"
												+ "You have an inventory of:")
										.appendField("Fishing Pole",
												Emoji.FISHING_POLE.toString() + " "
														+ catches.get(Fishes.Fishing_Pole.getKey()),
												true)
										.appendField("Frog",
												Emoji.FROG.toString() + " " + catches.get(Fishes.Frog.getKey()), true)
										.appendField("Boot",
												Emoji.BOOT.toString() + " " + catches.get(Fishes.Boot.getKey()), true)
										.appendField("Poop",
												Emoji.POOP.toString() + " " + catches.get(Fishes.Poop.getKey()), true)
										.appendField("Sandal",
												Emoji.SANDAL.toString() + " " + catches.get(Fishes.Sandal.getKey()),
												true)
										.appendField("Crab",
												Emoji.CRAB.toString() + " " + catches.get(Fishes.Crab.getKey()), true)
										.appendField("Octopus",
												Emoji.OCTOPUS.toString() + " " + catches.get(Fishes.Octopus.getKey()),
												true)
										.appendField("Turtle",
												Emoji.TURTLE.toString() + " " + catches.get(Fishes.Turtle.getKey()),
												true)
										.appendField("Fish",
												Emoji.FISH.toString() + " " + catches.get(Fishes.Fish.getKey()), true)
										.appendField("Blowfish",
												Emoji.BLOWFISH.toString() + " " + catches.get(Fishes.Blowfish.getKey()),
												true)
										.appendField("Dolphin",
												Emoji.DOLPHIN.toString() + " " + catches.get(Fishes.Dolphin.getKey()),
												true)
										.appendField("Crocodile",
												Emoji.CROCODILE.toString() + " "
														+ catches.get(Fishes.Crocodile.getKey()),
												true)
										.appendField("Shark",
												Emoji.SHARK.toString() + " " + catches.get(Fishes.Shark.getKey()), true)
										.appendField("Squid",
												Emoji.SQUID.toString() + " " + catches.get(Fishes.Squid.getKey()), true)
										.appendField("Treasure",
												Emoji.TREASURE.toString() + " " + catches.get(Fishes.Treasure.getKey()),
												true)
										.appendField("Shrimp",
												Emoji.SHRIMP.toString() + " " + catches.get(Fishes.Shrimp.getKey()),
												true)
										.appendField("Whale",
												Emoji.WHALE.toString() + " " + catches.get(Fishes.Whale.getKey()), true)
										.appendField("Shell",
												Emoji.SHELL.toString() + " " + catches.get(Fishes.Shell.getKey()), true)
										.build());
				BotUtils.sendMessage(message);
				return;
			} else if (context.getArg().matches("Price|price")) {
				MessageBuilder message = new MessageBuilder(context.getClient()).withChannel(context.getChannel())
						.withEmbed(
								new EmbedBuilder()
										.withDesc("Ahoy Matey " + context.getAuthor().mention() + "! \n"
												+ "The current prices of the fishes are: ")
										.appendField("Fishing Pole",
												Emoji.FISHING_POLE.toString() + " 0 Coins", true)
										.appendField("Frog",
												Emoji.FROG.toString() + " " + Fishes.Frog.getValue() + " Coins", true)
										.appendField("Boot",
												Emoji.BOOT.toString() + " " + Fishes.Boot.getValue() + " Coins", true)
										.appendField("Poop",
												Emoji.POOP.toString() + " " + Fishes.Poop.getValue() + " Coins", true)
										.appendField("Sandal",
												Emoji.SANDAL.toString() + " " + Fishes.Sandal.getValue() + " Coins",
												true)
										.appendField("Crab",
												Emoji.CRAB.toString() + " " + Fishes.Crab.getValue() + " Coins", true)
										.appendField("Octopus",
												Emoji.OCTOPUS.toString() + " " + Fishes.Octopus.getValue() + " Coins",
												true)
										.appendField("Turtle",
												Emoji.TURTLE.toString() + " " + Fishes.Turtle.getValue() + " Coins",
												true)
										.appendField("Fish",
												Emoji.FISH.toString() + " " + Fishes.Fish.getValue() + " Coins", true)
										.appendField("Blowfish",
												Emoji.BLOWFISH.toString() + " " + Fishes.Blowfish.getValue() + " Coins",
												true)
										.appendField("Dolphin",
												Emoji.DOLPHIN.toString() + " " + Fishes.Dolphin.getValue() + " Coins",
												true)
										.appendField("Crocodile",
												Emoji.CROCODILE.toString() + " "
														+ Fishes.Crocodile.getValue() + " Coins",
												true)
										.appendField("Shark",
												Emoji.SHARK.toString() + " " + Fishes.Shark.getValue() + " Coins", true)
										.appendField("Squid",
												Emoji.SQUID.toString() + " " + Fishes.Squid.getValue() + " Coins", true)
										.appendField("Treasure",
												Emoji.TREASURE.toString() + " " + Fishes.Treasure.getValue() + " Coins",
												true)
										.appendField("Shrimp",
												Emoji.SHRIMP.toString() + " " + Fishes.Shrimp.getValue() + " Coins",
												true)
										.appendField("Whale",
												Emoji.WHALE.toString() + " " + Fishes.Whale.getValue() + " Coins", true)
										.appendField("Shell",
												Emoji.SHELL.toString() + " " + Fishes.Shell.getValue() + " Coins", true)
										.build());
				BotUtils.sendMessage(message);
				return;
			} else if (context.getArg().matches("Sell|sell")) {
				long Total = 0;
				long fishprice = 0;
				long inventory_amount = 0;
				try {
					catches = DatabaseFishing.getJsonObjectInformation(AuthorID);
					if((long) catches.get(Fishes.Frog.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Frog.getKey());
						fishprice = Fishes.Frog.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Frog.getKey());
					}
					if((long) catches.get(Fishes.Boot.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Boot.getKey());
						fishprice = Fishes.Boot.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Boot.getKey());
					}
					if((long) catches.get(Fishes.Poop.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Poop.getKey());
						fishprice = Fishes.Poop.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Poop.getKey());
					}
					if((long) catches.get(Fishes.Sandal.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Sandal.getKey());
						fishprice = Fishes.Sandal.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Sandal.getKey());
					}
					if((long) catches.get(Fishes.Crab.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Crab.getKey());
						fishprice = Fishes.Crab.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Crab.getKey());
					}
					if((long) catches.get(Fishes.Octopus.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Octopus.getKey());
						fishprice = Fishes.Octopus.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Octopus.getKey());
					}
					if((long) catches.get(Fishes.Turtle.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Turtle.getKey());
						fishprice = Fishes.Turtle.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Turtle.getKey());
					}
					if((long) catches.get(Fishes.Fish.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Fish.getKey());
						fishprice = Fishes.Fish.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Fish.getKey());
					}
					if((long) catches.get(Fishes.Blowfish.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Blowfish.getKey());
						fishprice = Fishes.Blowfish.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Blowfish.getKey());
					}
					if((long) catches.get(Fishes.Dolphin.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Dolphin.getKey());
						fishprice = Fishes.Dolphin.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Dolphin.getKey());
					}
					if((long) catches.get(Fishes.Crocodile.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Crocodile.getKey());
						fishprice = Fishes.Crocodile.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Crocodile.getKey());
					}
					if((long) catches.get(Fishes.Shark.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Shark.getKey());
						fishprice = Fishes.Shark.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Shark.getKey());
					}
					if((long) catches.get(Fishes.Squid.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Squid.getKey());
						fishprice = Fishes.Squid.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Squid.getKey());
					}
					if((long) catches.get(Fishes.Treasure.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Treasure.getKey());
						fishprice = Fishes.Treasure.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Treasure.getKey());
					}
					if((long) catches.get(Fishes.Shrimp.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Shrimp.getKey());
						fishprice = Fishes.Shrimp.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Shrimp.getKey());
					}
					if((long) catches.get(Fishes.Whale.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Whale.getKey());
						fishprice = Fishes.Whale.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Whale.getKey());
					}
					if((long) catches.get(Fishes.Shell.getKey()) >= 1)
					{
						inventory_amount = (long) catches.get(Fishes.Shell.getKey());
						fishprice = Fishes.Shell.getValue();
						Total = Total + (inventory_amount * fishprice);
						DatabaseFishing.RemoveItem(AuthorID, Fishes.Shell.getKey());
					}
					
					BotUtils.sendMessage("Sold for a total of: " + Total, context.getChannel());
					Database.getDBUser(context.getGuild(), context.getAuthor()).addCoins((int)Total);
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					LogUtils.error(e, "An error occurred while selling fish.");
					BotUtils.sendMessage("Something went wrong, log has been created...", context.getChannel());
				}
				return;
			} else if (context.getArg().matches("Buy|buy")) {
				try {
					catches = DatabaseFishing.getJsonObjectInformation(AuthorID);
					long fishing_pole_inv = (long) catches.get(Fishes.Fishing_Pole.getKey());
					if (fishing_pole_inv == 1) {
						BotUtils.sendMessage(context.getAuthor().mention() + " Already have a fishing rod available in your inventory!",
								context.getChannel());
					} else {
						DatabaseFishing.AddItem(AuthorID, Fishes.Fishing_Pole.getKey(), 1);
						Database.getDBUser(context.getGuild(), context.getAuthor()).addCoins(-15);
						BotUtils.sendMessage(context.getAuthor().mention() + "You just bought a fishing pole for 15 coins!", context.getChannel());
					}
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					LogUtils.error(e, "An error occurred while buying a fishing pole.");
					BotUtils.sendMessage("Something went wrong, log has been created...", context.getChannel());
				}
			} else {
				try {
					catches = DatabaseFishing.getJsonObjectInformation(AuthorID);
					Fishes catched = CatchFish();
					long fishing_pole_inv = (long) catches.get(Fishes.Fishing_Pole.getKey());
					if (fishing_pole_inv == 1) {
						if (catched.getKey() == "CROCODILE" && catched.getKey() != null) {
							BotUtils.sendMessage(
									context.getAuthor().mention() + " You just caught a " + catched.getName() + " " + Emoji.GetEmoji(catched.getEmoji())
											+ "\n" + "Sadly it destroyed your fishing rod... Maybe buy a new one?",
									context.getChannel());
							DatabaseFishing.AddItem(AuthorID, Fishes.Crocodile.getKey(), 1);
							DatabaseFishing.RemoveItem(AuthorID, Fishes.Fishing_Pole.getKey());
						} else if (catched.getKey() == "SHARK" && catched.getKey() != null) {
							BotUtils.sendMessage(
									context.getAuthor().mention() + " You just caught a " + catched.getName() + " " + Emoji.GetEmoji(catched.getEmoji())
											+ "\n" + "Sadly it destroyed your fishing rod... Maybe buy a new one?",
									context.getChannel());
							DatabaseFishing.AddItem(AuthorID, Fishes.Shark.getKey(), 1);
							DatabaseFishing.RemoveItem(AuthorID, Fishes.Fishing_Pole.getKey());
						} else if (catched.getKey() == "NOTHING") {
							BotUtils.sendMessage(context.getAuthor().mention() + "Sorry you caught nothing! Try again!", context.getChannel());
						} else {
							BotUtils.sendMessage(
									context.getAuthor().mention() + " You just caught a " + catched.getName() + " " + Emoji.GetEmoji(catched.getEmoji())
											+ "\n" + Fishes.valueOf(catched.getName()).getDescription()
											+ " It has been added to your inventory!",
									context.getChannel());
							long Total = (long) catches.get(catched.getKey()) + 1;
							DatabaseFishing.AddItem(AuthorID, catched.getKey(), Total);
						}
					} else {
						BotUtils.sendMessage(
								context.getAuthor().mention() + " No fishing rod available in your inventory! \n"
										+ "Please buy one using /fishing buy  (Will cost you 15 coins!)",
								context.getChannel());
					}
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					LogUtils.error(e, "An error occurred while while catching a fish.");
					BotUtils.sendMessage("Something went wrong, log has been created...", context.getChannel());
				}
			}
		} else {
			BotUtils.sendMessage(Emoji.STOPWATCH + " " + context.getAuthor().mention() + Emoji.STOPWATCH + "\n"
					+ "**You can use this command every 10 seconds! Have some patience!**", context.getChannel());
			LogUtils.error("User: " + context.getAuthorName() + " is spamming the fish command...");
		}
	}

	private static Fishes CatchFish() {
		Random rng = new Random();
		int fishnr = rng.nextInt(140) + 1;
		Fishes fish = null;
		if (fishnr >= 1 && fishnr < 4) {
			fish = Fishes.Frog;
		} else if (fishnr >= 5 && fishnr < 11) {
			fish = Fishes.Boot;
		} else if (fishnr >= 12 && fishnr < 18) {
			fish = Fishes.Poop;
		} else if (fishnr >= 19 && fishnr < 25) {
			fish = Fishes.Crab;
		} else if (fishnr >= 26 && fishnr < 32) {
			fish = Fishes.Octopus;
		} else if (fishnr >= 33 && fishnr < 39) {
			fish = Fishes.Turtle;
		} else if (fishnr >= 40 && fishnr < 46) {
			fish = Fishes.Blowfish;
		} else if (fishnr >= 47 && fishnr < 60) {
			fish = Fishes.Fish;
		} else if (fishnr >= 61 && fishnr < 65) {
			fish = Fishes.Dolphin;
		} else if (fishnr >= 66 && fishnr < 70) {
			fish = Fishes.Whale;
		} else if (fishnr >= 71 && fishnr < 77) {
			fish = Fishes.Crocodile; // destroy rod!
		} else if (fishnr >= 78 && fishnr < 84) {
			fish = Fishes.Shark; // destroy rod!
		} else if (fishnr >= 85 && fishnr < 91) {
			fish = Fishes.Squid;
		} else if (fishnr >= 92 && fishnr < 95) {
			fish = Fishes.Treasure;
		} else if (fishnr >= 96 && fishnr < 102) {
			fish = Fishes.Shrimp;
		} else if (fishnr >= 103 && fishnr < 107) {
			fish = Fishes.Shell;
		} else {
			fish = Fishes.Nothing;
		}
		return fish;
	}

	@Override
	public EmbedObject getHelp(String prefix) {
		return new HelpBuilder(this, prefix).setDescription("Go fishing on a boat." + Emoji.ROWBOAT.toString())
				.addArg("Inventory|inv", "See your inventory of fishes/items you have caught!", true)
				.addArg("Sell", "Sell all the items you currently have in your inventory!", true)
				.addArg("Buy", "Buy a fishing pole!", true)
				.addArg("Price", "See here the current price list of the fishes!", true)
				.setInformation(
						"A fishing pole is required to go fishing! \n" + "Make sure to buy 1 using: /fishing Buy|buy")
				.build();
	}
}