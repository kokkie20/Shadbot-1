package me.shadorc.discordbot.command.utility;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import me.shadorc.discordbot.Emoji;
import me.shadorc.discordbot.command.Command;
import me.shadorc.discordbot.command.Context;
import me.shadorc.discordbot.utility.BotUtils;
import me.shadorc.discordbot.utility.Log;
import me.shadorc.discordbot.utility.NetUtils;
import sx.blah.discord.util.EmbedBuilder;

public class WikiCmd extends Command {

	public WikiCmd() {
		super(false, "wiki", "wikipedia");
	}

	@Override
	public void execute(Context context) {
		if(context.getArg() == null) {
			BotUtils.sendMessage(Emoji.WARNING + " Merci d'indiquer une recherche.", context.getChannel());
			return;
		}

		try {
			String searchEncoded = URLEncoder.encode(context.getArg(), "UTF-8");
			//Wiki api doc https://en.wikipedia.org/w/api.php?action=help&modules=query%2Bextracts
			String json = NetUtils.getHTML(new URL("https://fr.wikipedia.org/w/api.php?"
					+ "action=query"
					+ "&titles=" + searchEncoded
					+ "&prop=extracts"
					+ "&format=json"
					+ "&explaintext=true"
					+ "&exintro=true"
					+ "&exsentences=5"));

			JSONObject pagesObj = new JSONObject(json).getJSONObject("query").getJSONObject("pages");
			String pageId = pagesObj.names().getString(0);
			JSONObject searchObj = pagesObj.getJSONObject(pageId);

			if(pageId.equals("-1") || searchObj.getString("extract").isEmpty()) {
				BotUtils.sendMessage(Emoji.WARNING + " Aucun résultat pour : " + context.getArg(), context.getChannel());
				return;
			}

			EmbedBuilder builder = new EmbedBuilder()
					.withAuthorName(searchObj.getString("title"))
					.withThumbnail("https://s1.qwant.com/thumbr/300x0/2/8/50c4ce83955fe31f8f070e40c10926/b_0_q_0_p_0.jpg?u=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2Fthumb%2Fd%2Fd1%2FWikipedia-logo-v2-fr.svg%2F892px-Wikipedia-logo-v2-fr.svg.png&q=0&b=0&p=0&a=0")
					.withAuthorIcon(context.getAuthor().getAvatarURL())
					.withColor(new Color(170, 196, 222))
					.appendDesc(searchObj.getString("extract"))
					.withFooterText("Page Wikipédia : https://fr.wikipedia.org/wiki/" + URLEncoder.encode(searchObj.getString("title"), "UTF-8"));

			BotUtils.sendEmbed(builder.build(), context.getChannel());
		} catch (IOException e) {
			Log.error("Une erreur est survenue lors de la récupération des informations sur Wikipédia.", e, context.getChannel());
		}
	}

}
