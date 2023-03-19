package dev.zontreck.libzontreck.permissions;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.UUID;

public class PermissionUser {

    public UUID player;
    public List<Permission> permissions = Lists.newArrayList();

    public PermissionUser(Player player)
    {
        this(player.getUUID());
    }

    public PermissionUser(UUID ID)
    {
        player=ID;
    }

    public CompoundTag save()
    {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("id", player);
        ListTag perms = new ListTag();
        for (Permission perm :
                permissions) {
            perms.add(perm.save());

        }

        tag.put("perms", perms);
        return tag;
    }

    public PermissionUser(CompoundTag tag)
    {
        player = tag.getUUID("id");

        ListTag lst = tag.getList("perms", Tag.TAG_COMPOUND);
        for (Tag t :
                lst) {
            CompoundTag ct = (CompoundTag) t;
            permissions.add(new Permission(ct));
        }
    }


    public boolean hasPermission(String node)
    {
        return permissions.stream()
                .filter(CPRED-> CPRED.permName.equals(node))
                .count()>0;
    }
}
