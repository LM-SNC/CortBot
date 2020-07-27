package art.courtbot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;

public class PermissionManager {
    public void getPermissions(GuildChannel textChannel, Member member, Permission permission) {

        textChannel.createPermissionOverride(member)
                .setAllow(permission)
                .queue();

        System.out.println("PermissionManager:getPermission() -- право " + permission.getName() + " было успешно выдано юзеру "
                + member.getEffectiveName() + " на канал" + textChannel.getName());
    }

    public void removePermissions(GuildChannel guildChannel, Member member, Permission permission) {
        guildChannel.createPermissionOverride(member)
                .setDeny(permission).queue();



        System.out.println("PermissionManager:getPermission() -- право " + permission.getName() + " было успешно удалено у юзера "
                + member.getEffectiveName() + " на канал" + guildChannel.getName());
    }
}
