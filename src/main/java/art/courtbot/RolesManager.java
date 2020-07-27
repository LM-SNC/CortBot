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


//    String defaultEmotes[][];
//    String specialEmotes[][];

    PrivateMessageManager privateMessageManager;
    ChannelsManager channelsManager;
    PermissionManager permissionManager;

    public RolesManager() {
        privateMessageManager = new PrivateMessageManager();
        channelsManager = new ChannelsManager();
        permissionManager = new PermissionManager();
    }

    public void addRole(Event event, Member member, String roleId){
        member.getGuild().addRoleToMember(member, getRole(event, roleId)).complete();
        System.out.println("RolesManager:getRole() -- Роль" + getRole(event, "734491453442752585").getName()
                + " была успешно выдана участнику " + member.getEffectiveName());
    }


    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println(event.getJDA().getEmotes());
//        defaultEmotes = new String[][]{{"728372880743596063", "728372858824294511", "728372894438260736", "728372909080444929"},
//                {"734491453442752585", "734491453442752585", "734491453442752585", "734491453442752585"}};
//
//        specialEmotes = new String[][]{{"729000468730085426"},
//                {"734544082248138834"}};

    }



    public Role getRole(Event event, String roleId) {
        return event.getJDA().getRoleById(roleId);
    }

//    public void massAddRoles(Event event, Message message) {
//
//        for (String emote : defaultEmotes[0]) {
//            message.addReaction(getEmoteFromId(event, emote)).complete();
//        }
//
//        for (String emote : specialEmotes[0]) {
//            message.addReaction(getEmoteFromId(event, emote)).complete();
//        }
//    }


}
