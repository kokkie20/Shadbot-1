package me.shadorc.shadbot.command.gamestats;

import java.io.IOException;
import java.util.List;

import me.shadorc.shadbot.Config;
import me.shadorc.shadbot.core.command.AbstractCommand;
import me.shadorc.shadbot.core.command.CommandCategory;
import me.shadorc.shadbot.core.command.Context;
import me.shadorc.shadbot.core.command.annotation.Command;
import me.shadorc.shadbot.core.command.annotation.RateLimited;
import me.shadorc.shadbot.exception.IllegalCmdArgumentException;
import me.shadorc.shadbot.exception.MissingArgumentException;
import me.shadorc.shadbot.utils.FormatUtils;
import me.shadorc.shadbot.utils.MathUtils;
import me.shadorc.shadbot.utils.StringUtils;
import me.shadorc.shadbot.utils.Utils;
import me.shadorc.shadbot.utils.embed.EmbedUtils;
import me.shadorc.shadbot.utils.embed.HelpBuilder;
import me.shadorc.shadbot.utils.object.Emoji;
import me.shadorc.shadbot.utils.object.LoadingMessage;
import net.shadorc.overwatch4j.HeroDesc;
import net.shadorc.overwatch4j.Overwatch4J;
import net.shadorc.overwatch4j.OverwatchException;
import net.shadorc.overwatch4j.OverwatchPlayer;
import net.shadorc.overwatch4j.enums.Platform;
import net.shadorc.overwatch4j.enums.Region;
import net.shadorc.overwatch4j.enums.TopHeroesStats;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

@RateLimited
@Command(category = CommandCategory.GAMESTATS, names = { "overwatch" }, alias = "ow")
public class OverwatchCmd extends AbstractCommand {

	static {
		Overwatch4J.setTimeout(Config.DEFAULT_TIMEOUT);
	}

	@Override
	public void execute(Context context) throws MissingArgumentException, IllegalCmdArgumentException {
		List<String> splitArgs = StringUtils.split(context.getArg());
		if(!MathUtils.isInRange(splitArgs.size(), 1, 3)) {
			throw new MissingArgumentException();
		}

		LoadingMessage loadingMsg = new LoadingMessage("Loading Overwatch profile...", context.getChannel());
		loadingMsg.send();

		try {
			OverwatchPlayer player;

			String username = null;
			Platform platform = null;
			Region region = null;
			switch (splitArgs.size()) {
				case 1:
					username = splitArgs.get(0);
					player = new OverwatchPlayer(username);
					break;

				case 2:
					platform = this.getPlatform(splitArgs.get(0));
					username = splitArgs.get(1);
					player = new OverwatchPlayer(username, platform);
					break;

				default:
					platform = this.getPlatform(splitArgs.get(0));
					region = this.getRegion(splitArgs.get(1));
					username = splitArgs.get(2);
					player = new OverwatchPlayer(username, platform, region);
					break;
			}

			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setLenient(true)
					.withAuthorName("Overwatch Stats")
					.withAuthorIcon("http://vignette4.wikia.nocookie.net/overwatch/images/b/bd/Overwatch_line_art_logo_symbol-only.png")
					.withUrl(player.getProfileURL())
					.withThumbnail(player.getIconUrl())
					.appendDescription(String.format("Stats for user **%s**%s",
							player.getName(), player.getRegion() == Region.NONE ? "" : " (Region: " + player.getRegion() + ")"))
					.appendField("Level", Integer.toString(player.getLevel()), true)
					.appendField("Competitive rank", Integer.toString(player.getRank()), true)
					.appendField("Wins", Integer.toString(player.getWins()), true)
					.appendField("Game time", player.getTimePlayed(), true)
					.appendField("Top hero (Time played)", this.getTopThreeHeroes(player.getList(TopHeroesStats.TIME_PLAYED)), true)
					.appendField("Top hero (Eliminations per life)", this.getTopThreeHeroes(player.getList(TopHeroesStats.ELIMINATIONS_PER_LIFE)), true);
			loadingMsg.edit(embed.build());

		} catch (OverwatchException err) {
			String msg;
			switch (err.getType()) {
				case BLIZZARD_INTERNAL_ERROR:
					msg = "There's an internal error on the Blizzard side, please try again later.";
					break;
				case NO_DATA:
					msg = "There is no data for this account yet.";
					break;
				case USER_NOT_FOUND:
					msg = "This user doesn't play Overwatch or doesn't exist.";
					break;
				default:
					msg = "An unknown error occurred while getting information from Overwatch profile.";
					break;
			}
			loadingMsg.edit(Emoji.MAGNIFYING_GLASS + " " + msg);
		} catch (IOException err) {
			loadingMsg.delete();
			Utils.handle("getting information from Overwatch profile", context, err);
		}
	}

	private String getTopThreeHeroes(List<HeroDesc> heroesList) {
		return FormatUtils.numberedList(3, heroesList.size(), count -> String.format("**%s**. %s (%s)",
				count, heroesList.get(count - 1).getName(), heroesList.get(count - 1).getDesc()));
	}

	private Platform getPlatform(String str) throws IllegalCmdArgumentException {
		Platform platform = Utils.getValueOrNull(Platform.class, str.toUpperCase());
		if(platform == null) {
			throw new IllegalCmdArgumentException(String.format("`%s` is not a valid Platform. %s",
					str, FormatUtils.formatOptions(Platform.class)));
		}
		return platform;
	}

	private Region getRegion(String str) throws IllegalCmdArgumentException {
		Region region = Utils.getValueOrNull(Region.class, str.toUpperCase());
		if(region == null) {
			throw new IllegalCmdArgumentException(String.format("`%s` is not a valid Region. %s",
					str, FormatUtils.formatOptions(Region.class)));
		}
		return region;
	}

	@Override
	public EmbedObject getHelp(String prefix) {
		return new HelpBuilder(this, prefix)
				.setDescription("Show player's stats for Overwatch.")
				.addArg("platform", String.format("user's platform (%s)",
						FormatUtils.format(Utils.removeAndGet(Platform.values(), Platform.NONE), platform -> platform.toString().toLowerCase(), ", ")), true)
				.addArg("region", String.format("user's region (%s)",
						FormatUtils.format(Utils.removeAndGet(Region.values(), Region.NONE), region -> region.toString().toLowerCase(), ", ")), true)
				.addArg("battletag#0000", false)
				.appendField("Info", "**platform** and **region** are automatically detected if nothing is specified.", false)
				.build();
	}

}
