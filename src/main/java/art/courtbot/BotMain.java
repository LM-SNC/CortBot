package art.courtbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class BotMain {
    public static void main(String[] args) throws Exception {
        RolesManager rolesManager = new RolesManager();
        String active = "BSTeam - discord.gg/xp2KrvD";
        JDA jda = new JDABuilder("NzM0NDc0MzY2NDYwNDkzODQ1.XxSQWg.r8mebp-etcKpXvG0pGU9UxEUOyQ")
//                .addEventListeners(new MessageHandler())
//                // .addEventListeners(new RR())
//                .addEventListeners(new DyntaxCode())
//                // .addEventListeners(new CreateChannel())
//                .addEventListeners(new ShopMiniGames())
//                .addEventListeners(new RrMiniGames())
//                .addEventListeners(new CasinoRouletteMiniGames())
//                .addEventListeners(new Blitz())
                .addEventListeners(rolesManager)
                .addEventListeners(new CommandManager(rolesManager))
                .addEventListeners(new PrivateMessageManager())
                .setActivity(Activity.playing(active))
                .build();


        System.out.println("CourtBot successful started");
    }
}
