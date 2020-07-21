package art.courtbot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class RolesManager extends ListenerAdapter {


    String defaultEmotes[][];
    String specialEmotes[][];

    PrivateMessageManager privateMessageManager;
    ChannelsManager channelsManager;
    PermissionManager permissionManager;

    public RolesManager() {
        privateMessageManager = new PrivateMessageManager();
        channelsManager = new ChannelsManager();
        permissionManager = new PermissionManager();
    }


    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println(event.getJDA().getEmotes());
        defaultEmotes = new String[][]{{"728372880743596063", "728372858824294511", "728372894438260736", "728372909080444929"},
                {"734491453442752585", "734491453442752585", "734491453442752585", "734491453442752585"}};

        specialEmotes = new String[][]{{"729000468730085426"},
                {"734544082248138834"}};

    }

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        event.getMember().getGuild().addRoleToMember(event.getMember(), getRole(event, "734491453442752585")).complete();
        System.out.println("RolesManager:getRole() -- Роль" + getRole(event, "734491453442752585").getName()
                + " была успешно выдана участнику " + event.getMember().getEffectiveName());
    }

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        Member member = event.getMember();
        if (event.getUser().isBot()) {
            return;
        }

        for (int i = 0; i < defaultEmotes.length; i++) {
            if (getEmoteFromId(event, defaultEmotes[0][i]).equals(event.getReactionEmote().getEmote())) {
                System.out.println("default!");
                event.getMember().getGuild().addRoleToMember(member, getRole(event, defaultEmotes[1][i])).complete();
                return;
            }
        }


        for (int i = 0; i < specialEmotes[0].length; i++) {
            if (getEmoteFromId(event, specialEmotes[0][i]).equals(event.getReactionEmote().getEmote())) {
                TextChannel textChannelTicket;
                Message sendMessage;
                System.out.println("special!");
                String notifMessage = "Для получения роли " + getEmoteFromId(event, specialEmotes[0][i]).getName() + " нужно пройти дополнительную проверку, вы готовы это сделать?";
                privateMessageManager.sendPrivateMessage(member, notifMessage);

            }
        }

    }

    public Role getRole(Event event, String roleId) {
        return event.getJDA().getRoleById(roleId);
    }

    public void massAddRoles(Event event, Message message) {

        for (String emote : defaultEmotes[0]) {
            message.addReaction(getEmoteFromId(event, emote)).complete();
        }

        for (String emote : specialEmotes[0]) {
            message.addReaction(getEmoteFromId(event, emote)).complete();
        }
    }

    public Emote getEmoteFromId(Event event, String id) {
        return event.getJDA().getEmoteById(id);
    }


}
