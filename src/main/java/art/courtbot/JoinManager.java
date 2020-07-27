package art.courtbot;

import jdk.nashorn.internal.runtime.ListAdapter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class JoinManager extends ListenerAdapter {

    private RolesManager rolesManager;
    private ConnectionManager connectionManager;
    private PermissionManager permissionManager;

    public JoinManager(ConnectionManager connectionManager){
        rolesManager = new RolesManager();
        permissionManager = new PermissionManager();
        this.connectionManager = connectionManager;
    }

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        Member member = event.getMember();
        String verificationChannel = connectionManager.getVerificationChannel(event.getGuild().getId());
        rolesManager.addRole(event, member, "734491453442752585");
        System.out.println(verificationChannel);

        if(verificationChannel != null){
            System.out.println("В бд есть значение! Работаем!");
            event.getGuild().getChannels().forEach(guildChannel -> permissionManager.removePermissions(guildChannel, member, Permission.VIEW_CHANNEL));
            event.getGuild().getChannels().forEach(guildChannel -> permissionManager.removePermissions(guildChannel, member, Permission.MESSAGE_READ));
            permissionManager.getPermissions(getGuildChannel(event, verificationChannel), member, Permission.VIEW_CHANNEL);
            permissionManager.getPermissions(getGuildChannel(event, verificationChannel), member, Permission.MESSAGE_READ);
        }else {
            System.out.println("К сожалению здесь пусто :(");
        }
    }

    private GuildChannel getGuildChannel(Event event, String id){
        return event.getJDA().getGuildChannelById(id);
    }
}
