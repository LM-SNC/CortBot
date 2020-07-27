package art.courtbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class BotMain {
    public static void main(String[] args) throws Exception {
        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.onBdCreate();
        RolesManager rolesManager = new RolesManager();
        TicketManager ticketManager = new TicketManager();
        ReactionManager reactionManager = new ReactionManager(ticketManager);
        String active = "BSTeam - discord.gg/xp2KrvD";
        JDA jda = new JDABuilder(".Xxc4qg.ASs0G7EhrtDRPPUyw-7jEFk-LtA")
                .addEventListeners(rolesManager)
                .addEventListeners(new CommandManager(connectionManager, reactionManager, ticketManager))
                .addEventListeners(new PrivateMessageManager())
                .addEventListeners(reactionManager)
                .addEventListeners(new JoinManager(connectionManager))
                .setActivity(Activity.playing(active))
                .build();


        System.out.println("CourtBot successful started");
    }
}
