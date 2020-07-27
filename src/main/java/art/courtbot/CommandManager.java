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
    private ArrayList<String> ticketCommands;
    private ArrayList<TextChannel> verificationTextChannels;
    private CustomManager customManager;
    private ReactionManager reactionManager;
    private TicketManager ticketManager;
    private ConnectionManager connectionManager;
    private ArrayList<String> welcomeMessage;

    public CommandManager(ConnectionManager connectionManager, ReactionManager reactionManager, TicketManager ticketManager) {
        System.out.println("public CommandManager()");

        tcvCommands = new ArrayList<>(); //массив с командами
        tcvCommands.add("!tcv");
        tcvCommands.add("!thisChannelVerification");

        ticketCommands = new ArrayList<>();
        ticketCommands.add("!thisChannelSupport");
        ticketCommands.add("!tcs");

        verificationTextChannels = new ArrayList<>();
        welcomeMessage = new ArrayList<>();

        customManager = new CustomManager();
        this.reactionManager = reactionManager;
        this.ticketManager = ticketManager;
        this.connectionManager = connectionManager;

    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.getChannelType().isGuild()) {
            return;
        }

        event.getGuild().getId();
        String[] message = event.getMessage().getContentRaw().split(" ");
        Member member = event.getMember();

        TextChannel textChannel = event.getTextChannel();
        StringBuilder embedMessage = new StringBuilder();
        Message sendMessage;

        boolean isAdministrator = member.hasPermission(Permission.ADMINISTRATOR);
        if (message[0].equalsIgnoreCase("!help")) {
            embedMessage = new StringBuilder("Чтобы организовать систему верификации на этом канале пропишите: thisChannelVerification или !tcv");
            customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");

            embedMessage = new StringBuilder("Чтобы организовать систему тикетов в этом канале пропишите: !thisChannelSupport или !tcs");
            customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
        }

        if (isAdministrator && tcvCommands.contains(message[0])) {
            connectionManager.onVerificationChannel(event.getGuild().getId(), textChannel.getId(), null);
            embedMessage = new StringBuilder("Канал верификации был успешно изменен на: " + textChannel.getName());
            customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
        } else if (!isAdministrator && tcvCommands.contains(message[0])) {
            embedMessage = new StringBuilder("У вас недостаточно прав для выполнения данной команды");
            customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
        }


        message = event.getMessage().getContentRaw().split("@");
        if (isAdministrator && message[0].trim().equalsIgnoreCase("!qa")) {
            if (message.length == 2) {
                connectionManager.onAddQuestion(event.getGuild().getId(), message[1]);
            } else if (message.length < 2) {
                embedMessage = new StringBuilder("Правильное использование команды: !qa 'Ваш вопрос' 'Ответ на заданный вопрос' ");
                customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
            }

        } else if (!isAdministrator) {
            embedMessage = new StringBuilder("У вас недостаточно прав для выполнения данной команды");
            customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
        }

        message = event.getMessage().getContentRaw().split(" ");


        if (isAdministrator && message[0].trim().equalsIgnoreCase("!qd")) {
            if (message.length == 2) {
                connectionManager.onAddQuestion(event.getGuild().getId(), message[1]);
            } else if (message.length < 2) {
                embedMessage = new StringBuilder("Правильное использование команды: !qa 'Ваш вопрос' 'Ответ на заданный вопрос' ");
                customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
            }

        } else if (!isAdministrator) {
            embedMessage = new StringBuilder("У вас недостаточно прав для выполнения данной команды");
            customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
        }

        if (isAdministrator && message[0].trim().equalsIgnoreCase("!ql")) {
            embedMessage = new StringBuilder();
            for (String qlist : connectionManager.getQuestion(event.getGuild().getId())) {
                embedMessage.append("\t").append(qlist);
            }
            customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
        } else if (!isAdministrator) {
            embedMessage = new StringBuilder("У вас недостаточно прав для выполнения данной команды");
            customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
        }

        if (isAdministrator && ticketCommands.contains(message[0])) {
            if (!verificationTextChannels.contains(textChannel)) {
                verificationTextChannels.add(textChannel);

                embedMessage = new StringBuilder("Вы успешно подключили данный канал к системе проверок!");
                String finalEmbedMessage1 = embedMessage.toString();
                new Thread(() -> DeleteMessage(customManager.Notification(textChannel, member, Color.cyan, "", "**" + finalEmbedMessage1 + "**"))).start();

                connectionManager.onVerificationChannel(event.getGuild().getId(), null, textChannel.getId());
                embedMessage = new StringBuilder("Канал поддержки был успешно изменен на: " + textChannel.getName());
                customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");

                embedMessage = new StringBuilder("Для того чтобы открыть новй тикет нажмите на галочку");
                reactionManager.onReactionAdd(event, customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**"), "729000468730085426");


            } else {
                embedMessage = new StringBuilder("Данный канал уже подключен к системе проверок");
                String finalEmbedMessage = embedMessage.toString();
                new Thread(() -> DeleteMessage(customManager.Notification(textChannel, member, Color.cyan, "", "**" + finalEmbedMessage + "**"))).start();
                customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
            }
        } else if (!isAdministrator && ticketCommands.contains(message[0])) {
            embedMessage = new StringBuilder("У вас недостаточно прав для выполнения данной команды");
            customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
        }


        if (isAdministrator && message[0].equalsIgnoreCase("!stop")) {
            if (ticketManager.channelsTicket.contains(event.getTextChannel())) {
                ticketManager.deleteTicket(event.getTextChannel());
            }
        } else if (!isAdministrator && message[0].equalsIgnoreCase("!stop")) {
            embedMessage = new StringBuilder("У вас недостаточно прав для выполнения данной команды");
            customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
        }

        if (isAdministrator && message[0].equalsIgnoreCase("!clear")) {
            for (Message messageInHistory : textChannel.getIterableHistory()) {
                messageInHistory.delete().complete();
            }
        } else if (!isAdministrator && message[0].equalsIgnoreCase("!clear")) {
            embedMessage = new StringBuilder("У вас недостаточно прав для выполнения данной команды");
            customManager.Notification(textChannel, member, Color.cyan, "", "**" + embedMessage + "**");
        }
    }

    public void DeleteMessage(Message message) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        message.delete().complete();
    }
}