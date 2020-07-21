package art.courtbot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;

public class CommandManager extends ListenerAdapter {
    private ArrayList<String> tcvCommands;
    private ArrayList<TextChannel> verificationTextChannels;
    private CustomManager customManager;
    private RolesManager rolesManager;

    public CommandManager(RolesManager rolesManager) {
        System.out.println("public CommandManager()");

        tcvCommands = new ArrayList<>();
        tcvCommands.add("!tcv");
        tcvCommands.add("!thisChannelVerification");

        verificationTextChannels = new ArrayList<>();

        customManager = new CustomManager();
        this.rolesManager = rolesManager;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if(event.getAuthor().isBot() || !event.getChannelType().isGuild()){
            return;
        }

        String[] message = event.getMessage().getContentRaw().split(" ");
        Member member = event.getMember();

        TextChannel textChannel = event.getTextChannel();
        String embedMessage = "";
        Message sendMessage;

        boolean isAdministrator = member.hasPermission(Permission.ADMINISTRATOR);
        if (message[0].equalsIgnoreCase("!help")) {
            embedMessage = "Чтобы организовать раздачу ролей в этом канале пропишите: !thisChannelVerification или !tcv";
            customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
        }

        if (isAdministrator && tcvCommands.contains(message[0])) {
            if (!verificationTextChannels.contains(textChannel)) {
                verificationTextChannels.add(textChannel);

                embedMessage = "Вы успешно подключили данный канал к системе проверок!";
                String finalEmbedMessage1 = embedMessage;
                new Thread(() -> DeleteMessage(customManager.Notification(textChannel, member, Color.cyan, "", "**" + finalEmbedMessage1 + "**"))).start();

                embedMessage = "Роли: ";
                rolesManager.massAddRoles(event, customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**"));


            } else {
                embedMessage = "Данный канал уже подключен к системе проверок";
                String finalEmbedMessage = embedMessage;
                new Thread(() -> DeleteMessage(customManager.Notification(textChannel, member, Color.cyan, "", "**" + finalEmbedMessage + "**"))).start();
                customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
            }
        }else if(!isAdministrator && tcvCommands.contains(message[0])){
            embedMessage = "У вас недостаточно прав для выполнения данной команды";
            customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
        }
    }

    public void DeleteMessage(Message message){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        message.delete().complete();
    }
}