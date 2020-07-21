package art.courtbot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.priv.GenericPrivateMessageEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class PrivateMessageManager extends ListenerAdapter {
    TicketManager ticketManager;
    PermissionManager permissionManager;
    ChannelsManager channelsManager;

    public PrivateMessageManager(){
        ticketManager = new TicketManager();
        permissionManager = new PermissionManager();
        channelsManager = new ChannelsManager();
    }
    public void sendPrivateMessage(Member member, String message) {
        member.getUser().openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();
    }

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");

        if(message[0].equalsIgnoreCase("Да")){
            ticketManager.createTicket(event.getJDA().getGuildById("719317466915799056"), event.getAuthor().getId(), "привет! Ты годов пройти верификацию");
            event.getChannel().sendMessage("Отлично! Для вас был создан ticket!").complete();

        }
        //На данный момент всё более менее работает, раскидай всё по нужным классам
    }


}
