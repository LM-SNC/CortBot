package art.courtbot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.Event;

import java.awt.*;

public class TicketManager {

    PermissionManager permissionManager;
    ChannelsManager channelsManager;
    CustomManager customManager;
    int ticketIdToday;

    public TicketManager() {
        permissionManager = new PermissionManager();
        channelsManager = new ChannelsManager();
        customManager = new CustomManager();
    }

    public void createTicket(Guild guild, String memberId, String ticketMessage) {
        TextChannel textChannelTicket = channelsManager.createTextChannel(guild, "Ticket[SNC PROJECT] " + ticketIdToday);

        for (Member ticketMember : textChannelTicket.getMembers()) {
            if (!ticketMember.getId().equalsIgnoreCase(memberId)) {
                permissionManager.removePermissions(textChannelTicket, ticketMember, Permission.VIEW_CHANNEL);
            } else {
                permissionManager.getPermissions(textChannelTicket, ticketMember, Permission.VIEW_CHANNEL);
                permissionManager.removePermissions(textChannelTicket, ticketMember, Permission.CREATE_INSTANT_INVITE);
            }
        }
        customManager.Notification(textChannelTicket, getUserFromId(textChannelTicket, memberId), Color.CYAN,
                "","<@" + memberId + ">" + " ," + ticketMessage);
        textChannelTicket.sendMessage("<@" + memberId + ">").complete();


        ticketIdToday++;
    }

    public Member getUserFromId(TextChannel textChannel, String id) {
        for (Member ticketMember : textChannel.getMembers()) {
            if(ticketMember.getId().equalsIgnoreCase(id)){
                return ticketMember;
            }
        }
        return null;
    }
}
