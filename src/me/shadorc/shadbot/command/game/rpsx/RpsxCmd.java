package me.shadorc.shadbot.command.game.rpsx;

import java.util.concurrent.ThreadLocalRandom;

import me.shadorc.shadbot.core.command.AbstractCommand;
import me.shadorc.shadbot.core.command.CommandCategory;
import me.shadorc.shadbot.core.command.Context;
import me.shadorc.shadbot.core.command.annotation.Command;
import me.shadorc.shadbot.core.command.annotation.RateLimited;
import me.shadorc.shadbot.data.db.Database;
import me.shadorc.shadbot.data.stats.MoneyStatsManager;
import me.shadorc.shadbot.data.stats.MoneyStatsManager.MoneyEnum;
import me.shadorc.shadbot.exception.IllegalCmdArgumentException;
import me.shadorc.shadbot.exception.MissingArgumentException;
import me.shadorc.shadbot.ratelimiter.RateLimiter;
import me.shadorc.shadbot.utils.BotUtils;
import me.shadorc.shadbot.utils.FormatUtils;
import me.shadorc.shadbot.utils.Utils;
import me.shadorc.shadbot.utils.embed.HelpBuilder;
import sx.blah.discord.api.internal.json.objects.EmbedObject;

@RateLimited(cooldown = RateLimiter.GAME_COOLDOWN, max = 1)
@Command(category = CommandCategory.GAME, names = { "rpsx" })
public class RpsxCmd extends AbstractCommand {

	private static final int GAINS = 170;

	@Override
	public void execute(Context context) throws MissingArgumentException, IllegalCmdArgumentException {
		if(!context.hasArg()) {
			throw new MissingArgumentException();
		}

		Handsign userHandsign = Utils.getValueOrNull(Handsign.class, context.getArg());
		if(userHandsign == null) {
			throw new IllegalCmdArgumentException(String.format("`%s` is not a valid handsign. %s.",
					context.getArg(), FormatUtils.formatOptions(Handsign.class)));
		}

		Handsign botHandsign = Handsign.values()[ThreadLocalRandom.current().nextInt(Handsign.values().length)];

		StringBuilder strBuilder = new StringBuilder(String.format("**%s**: %s.%n**Shadbot**: %s.%n",
				context.getAuthorName(), userHandsign.getRepresentation(), botHandsign.getRepresentation()));

		if(userHandsign.equals(botHandsign)) {
			strBuilder.append("It's a draw !");
		} else if(userHandsign.isSuperior(botHandsign)) {
			strBuilder.append(String.format("%s wins ! Well done, you won **%d coins**.", context.getAuthorName(), GAINS));
			Database.getDBUser(context.getGuild(), context.getAuthor()).addCoins(GAINS);
			MoneyStatsManager.log(MoneyEnum.MONEY_GAINED, this.getName(), GAINS);
		} else {
			strBuilder.append(context.getClient().getOurUser().getName() + " wins !");
		}

		BotUtils.sendMessage(strBuilder.toString(), context.getChannel());
	}

	@Override
	public EmbedObject getHelp(String prefix) {
		return new HelpBuilder(this, prefix)
				.setDescription("Play a Rock–paper–scissors-lizard-spock game.")
				.addArg("handsign", FormatUtils.format(Handsign.values(), Handsign::getHandsign, ", "), false)
				.setInformation("Rock crushes scissors - scissors cuts paper - Papers covers rock \n"
						+ "Lizard poisons spock - Spock smashes scissors - Rocks crushes lizard \n"
						+ "Paper disproves spock - scissors decapitates lizard - Lizard eats paper \n "
						+ "Spock vaporizes rock")
				.setGains("The winner gets **%d coins**.", GAINS)
				.build();
	}

}
