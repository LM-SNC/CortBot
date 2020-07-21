package art.courtbot;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class ChannelsManager {

    public TextChannel createTextChannel(Guild guild, String channelName) {
        System.out.println("ChannelsManager:createTextChannel() -- канал " + channelName + " был успешно создан!");
        return guild.createTextChannel(channelName).complete();
    }

    public void createVoiceChannel(Category category) {
//        category.createVoiceChannel();
    }
}
