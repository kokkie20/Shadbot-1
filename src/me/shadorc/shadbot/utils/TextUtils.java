package me.shadorc.shadbot.utils;

import java.util.EnumSet;
import java.util.concurrent.ThreadLocalRandom;

import me.shadorc.shadbot.Config;
import me.shadorc.shadbot.command.admin.setting.core.SettingEnum;
import me.shadorc.shadbot.utils.object.Emoji;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class TextUtils {

	public static final String MISSING_ARG = Emoji.WHITE_FLAG + " Some arguments are missing, here is the help for this command.";

	public static final String PLAYLIST_LIMIT_REACHED =
			String.format(Emoji.WARNING + " You've reached the maximum number (%d) of tracks in a playlist. "
					+ "You can remove this limit by contributing to Shadbot. More info on **%s**", Config.MAX_PLAYLIST_SIZE, Config.PATREON_URL);

	public static final String NO_PLAYING_MUSIC =
			Emoji.MUTE + " No currently playing music.";

	private static final String[] SPAM_MESSAGES = { "Take it easy, we are not in a hurry !",
			"Phew.. give me time to rest, you're too fast for me.",
			"I'm not going anywhere, no need to be this fast.",
			"I don't think everyone here want to be spammed by us, just wait a little bit." };

	private static final String[] TIPS_MESSAGES = { String.format("Check %slotto", Config.DEFAULT_PREFIX),
			String.format("Add a music first using %splayfirst", Config.DEFAULT_PREFIX),
			String.format("Help me keep Shadbot alive ! %s", Config.PATREON_URL),
			String.format("Support server: %s", Config.SUPPORT_SERVER) };

	public static String getSpamMessage() {
		return SPAM_MESSAGES[ThreadLocalRandom.current().nextInt(SPAM_MESSAGES.length)];
	}

	public static String getTip() {
		return TIPS_MESSAGES[ThreadLocalRandom.current().nextInt(TIPS_MESSAGES.length)];
	}

	public static String notEnoughCoins(IUser user) {
		return String.format(Emoji.BANK + " (**%s**) You don't have enough coins. You can get some by playing **RPS**, **Hangman** "
				+ "or **Trivia**.", user.getName());
	}

	public static String noResult(String search) {
		return String.format(Emoji.MAGNIFYING_GLASS + " No results for `%s`.", search);
	}

	public static String mustBeNSFW(String prefix) {
		return String.format(Emoji.GREY_EXCLAMATION + " This must be a NSFW-channel. If you're an admin, you can use `%ssetting %s enable`",
				prefix, SettingEnum.NSFW);
	}

	public static String missingPerm(EnumSet<Permissions> permissions) {
		return TextUtils.missingPerm(permissions.toArray(new Permissions[permissions.size()]));
	}

	public static String missingPerm(Permissions... permissions) {
		return String.format(Emoji.ACCESS_DENIED + " I can't execute this command due to the lack of permission."
				+ "%nPlease, check my permissions and channel-specific ones to verify that %s %s checked.",
				FormatUtils.format(permissions, perm -> String.format("**%s**", StringUtils.capitalize(perm.toString().replace("_", " "))), " and "),
				permissions.length > 1 ? "are" : "is");
	}
}
