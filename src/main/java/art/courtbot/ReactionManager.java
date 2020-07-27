package art.courtbot;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ReactionManager extends ListenerAdapter {

    private ArrayList<String> emoteList;
    private TicketManager ticketManager;
    private PrivateMessageManager privateMessageManager;

    public ReactionManager(TicketManager ticketManager) {
        emoteList = new ArrayList<>();
        this.ticketManager = ticketManager;
        privateMessageManager = new PrivateMessageManager();
    }

    public void onReactionAdd(Event event, Message message, String emoteId) {
        message.addReaction(getEmoteFromId(event, emoteId)).complete();
        System.out.println(emoteId + " была добавлена реакция");
        emoteList.add(emoteId);
        System.out.println(emoteList.size());
        System.out.println(emoteId + " был добавлен в массив");
    }

    public Emote getEmoteFromId(Event event, String id) {
        return event.getJDA().getEmoteById(id);
    }

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        if (event.getMember().getUser().isBot()) {
            return;
        }

        System.out.println(emoteList.size());


        for (String reaction : emoteList) {
            if (reaction.equalsIgnoreCase(event.getReactionEmote().getEmote().getId())) {
                if (ticketManager.inTicketUsers.contains(event.getMember())) {
                    privateMessageManager.sendPrivateMessage(event.getMember(),"Для вас уже был создан тикет!");
                    return;
                }
                ticketManager.createTicket(event.getGuild(), event.getMember().getId(), "здравствуйте, задавайте ваш вопрос!");
                System.out.println("Тикет был создан");
                ticketManager.inTicketUsers.add(event.getMember());
            }
        }
    }
}
