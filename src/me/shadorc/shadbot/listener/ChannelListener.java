package me.shadorc.shadbot.listener;

import java.util.List;

import org.json.JSONArray;

import me.shadorc.shadbot.command.admin.setting.core.SettingEnum;
import me.shadorc.shadbot.data.db.DBGuild;
import me.shadorc.shadbot.data.db.Database;
import me.shadorc.shadbot.shard.ShardManager;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelDeleteEvent;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelEvent;

public class ChannelListener {

	@EventSubscriber
	public void onChannelEvent(ChannelEvent event) {
		ShardManager.execute(event.getGuild(), () -> {
			if(event instanceof ChannelDeleteEvent) {
				this.onChannelDeleteEvent((ChannelDeleteEvent) event);
			}
		});
	}

	private void onChannelDeleteEvent(ChannelDeleteEvent event) {
		DBGuild dbGuild = Database.getDBGuild(event.getGuild());
		List<Long> allowedChannelsID = dbGuild.getAllowedChannels();
		if(allowedChannelsID.remove(event.getChannel().getLongID())) {
			dbGuild.setSetting(SettingEnum.ALLOWED_CHANNELS, new JSONArray(allowedChannelsID));
		}
	}
}
