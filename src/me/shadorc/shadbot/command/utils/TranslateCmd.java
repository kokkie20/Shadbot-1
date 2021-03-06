package me.shadorc.shadbot.command.utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import me.shadorc.shadbot.core.command.AbstractCommand;
import me.shadorc.shadbot.core.command.CommandCategory;
import me.shadorc.shadbot.core.command.Context;
import me.shadorc.shadbot.core.command.annotation.Command;
import me.shadorc.shadbot.core.command.annotation.RateLimited;
import me.shadorc.shadbot.exception.IllegalCmdArgumentException;
import me.shadorc.shadbot.exception.MissingArgumentException;
import me.shadorc.shadbot.utils.BotUtils;
import me.shadorc.shadbot.utils.NetUtils;
import me.shadorc.shadbot.utils.StringUtils;
import me.shadorc.shadbot.utils.Utils;
import me.shadorc.shadbot.utils.embed.HelpBuilder;
import me.shadorc.shadbot.utils.object.Emoji;
import sx.blah.discord.api.internal.json.objects.EmbedObject;

@RateLimited
@Command(category = CommandCategory.UTILS, names = { "translate", "translation", "trans" })
public class TranslateCmd extends AbstractCommand {

	private static final BiMap<String, String> LANG_ISO_MAP = HashBiMap.create();

	static {
		for(String iso : Locale.getISOLanguages()) {
			LANG_ISO_MAP.put(new Locale(iso).getDisplayLanguage(Locale.ENGLISH).toLowerCase(), iso);
		}
		LANG_ISO_MAP.put("auto", "auto");
	}

	@Override
	public void execute(Context context) throws MissingArgumentException, IllegalCmdArgumentException {
		List<String> args = StringUtils.split(context.getArg(), 3);
		if(args.size() < 2) {
			throw new MissingArgumentException();
		}

		if(args.size() == 2) {
			args.add(0, "auto");
		}

		String langFrom = this.toISO(args.get(0));
		String langTo = this.toISO(args.get(1));

		if(langFrom == null || langTo == null) {
			throw new IllegalCmdArgumentException(String.format("One of the specified language doesn't exist. "
					+ "Use `%shelp %s` to see a complete list of supported languages.", context.getPrefix(), this.getName()));
		}

		String sourceText = args.get(2);
		try {
			String url = String.format("https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s",
					NetUtils.encode(langFrom), NetUtils.encode(langTo), NetUtils.encode(sourceText));
			JSONArray result = new JSONArray(NetUtils.getJSON(url));

			if(!(result.get(0) instanceof JSONArray)) {
				throw new IllegalCmdArgumentException(String.format("One of the specified language isn't supported. "
						+ "Use `%shelp %s` to see a complete list of supported languages.", context.getPrefix(), this.getName()));
			}

			String translatedText = ((JSONArray) ((JSONArray) result.get(0)).get(0)).get(0).toString();
			BotUtils.sendMessage(Emoji.MAP + String.format(" **%s** (%s) <=> **%s** (%s)",
					sourceText, StringUtils.capitalize(LANG_ISO_MAP.inverse().get(langFrom)),
					translatedText, StringUtils.capitalize(LANG_ISO_MAP.inverse().get(langTo))), context.getChannel());

		} catch (JSONException | IOException err) {
			Utils.handle("getting translation", context, err);
		}
	}

	private String toISO(String lang) {
		return LANG_ISO_MAP.containsValue(lang) ? lang : LANG_ISO_MAP.get(lang);
	}

	@Override
	public EmbedObject getHelp(String prefix) {
		return new HelpBuilder(this, prefix)
				.setDescription("Translate a text from a language to another.")
				.addArg("fromLang", "source language, by leaving it blank the language will be automatically detected", true)
				.addArg("toLang", "destination language", false)
				.addArg("text", false)
				.setExample(String.format("`%s%s en fr How are you ?`", prefix, this.getName()))
				.appendField("Documentation", "List of supported languages: https://cloud.google.com/translate/docs/languages", false)
				.build();
	}
}
