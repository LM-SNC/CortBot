package art.courtbot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class PermissionManager {
    public void getPermissions(TextChannel textChannel, Member member, Permission permission) {

        textChannel.createPermissionOverride(member)
                .setAllow(permission)
                .queue();

        System.out.println("PermissionManager:getPermission() -- право " + permission.getName() + " было успешно выдано юзеру "
                + member.getEffectiveName() + " на канал" + textChannel.getName());
    }

    public void removePermissions(TextChannel textChannel, Member member, Permission permission) {
        textChannel.createPermissionOverride(member)
                .setDeny(permission).queue();



        System.out.println("PermissionManager:getPermission() -- право " + permission.getName() + " было успешно удалено у юзера "
                + member.getEffectiveName() + " на канал" + textChannel.getName());
    }
}
