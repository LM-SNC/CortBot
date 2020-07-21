package art.courtbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.Event;

import java.awt.*;

public class CustomManager {

    public Message Notification(TextChannel textChannel, Member member, Color color, String emote, String message){
        Message sendMessage = null;
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(color);
        embedBuilder.setAuthor(member.getUser().getAsTag(), null, member.getUser().getAvatarUrl());
        embedBuilder.setDescription(emote + " " + message);
        sendMessage = textChannel.sendMessage(embedBuilder.build()).complete();

        return sendMessage;

    }
}
