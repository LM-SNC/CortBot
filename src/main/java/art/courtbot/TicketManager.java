package art.courtbot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.Event;

import java.awt.*;
import java.util.ArrayList;

public class TicketManager {

    PermissionManager permissionManager;
    ChannelsManager channelsManager;
    CustomManager customManager;
    int ticketIdToday;
    ArrayList<Member> inTicketUsers;
    ArrayList<TextChannel> channelsTicket;

    public TicketManager() {
        permissionManager = new PermissionManager();
        channelsManager = new ChannelsManager();
        customManager = new CustomManager();
        inTicketUsers = new ArrayList<>();
        channelsTicket = new ArrayList<>();
    }

    public void createTicket(Guild guild, String memberId, String ticketMessage) {
        TextChannel textChannelTicket = channelsManager.createTextChannel(guild, "Ticket[SNC PROJECT] " + ticketIdToday);
        channelsTicket.add(textChannelTicket);

        for (Member ticketMember : textChannelTicket.getMembers()) {
            if (!ticketMember.getId().equalsIgnoreCase(memberId)) {
                permissionManager.removePermissions(textChannelTicket, ticketMember, Permission.VIEW_CHANNEL);
            } else {
                permissionManager.getPermissions(textChannelTicket, ticketMember, Permission.VIEW_CHANNEL);
                permissionManager.removePermissions(textChannelTicket, ticketMember, Permission.CREATE_INSTANT_INVITE);
            }
        }
        customManager.Notification(textChannelTicket, getUserFromId(textChannelTicket, memberId), Color.CYAN,
                "", "<@" + memberId + ">" + " ," + ticketMessage);
        textChannelTicket.sendMessage("<@" + memberId + ">").complete();
//        new Thread(() -> inActive(textChannelTicket, memberId)).start();
        System.out.println(textChannelTicket.getHistory().size() + " histirySize");


        ticketIdToday++;
    }

    public void deleteTicket(TextChannel textChannel){
        textChannel.delete().complete();
        for(Member memberChannel : textChannel.getMembers()){
            if(inTicketUsers.contains(memberChannel)){
                inTicketUsers.remove(memberChannel);
            }
        }
    }

    public Member getUserFromId(TextChannel textChannel, String id) {
        for (Member ticketMember : textChannel.getMembers()) {
            if (ticketMember.getId().equalsIgnoreCase(id)) {
                return ticketMember;
            }
        }
        return null;
    }

//    public void inActive(TextChannel textChannel, String memberId) {
//        while (true) {
//            int historySize = 0;
//            historySize = getDiff(textChannel);
//
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if (historySize == getDiff(textChannel)) {
//                customManager.Notification(textChannel, getUserFromId(textChannel, memberId), Color.CYAN,
//                        "", "<@" + memberId + ">" + " ," + "из-за неактивности ваш тикет закроют через 10 секунд");
//            }
//            break;
//        }
//
//        textChannel.delete().complete();
//
//        if (inTicketUsers.contains(getUserFromId(textChannel, memberId))) {
//            inTicketUsers.remove(getUserFromId(textChannel, memberId));
//        }
//    }

    public int getDiff(TextChannel textChannel) {
        int diff = 0;
        for (Message message : textChannel.getIterableHistory()) {
            diff++;
        }

        return diff;
    }
}
