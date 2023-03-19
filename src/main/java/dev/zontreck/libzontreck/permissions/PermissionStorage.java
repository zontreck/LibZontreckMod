package dev.zontreck.libzontreck.permissions;

import com.google.common.collect.Lists;
import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.util.FileTreeDatastore;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PermissionStorage extends FileTreeDatastore
{
    public static final Path BASE = LibZontreck.BASE_CONFIG.resolve(
            "permissions.nbt");

    private static final List<PermissionUser> permissionUsers;
    public static List<PermissionUser> getListOfUsers()
    {
        return Lists.newArrayList(permissionUsers);
    }

    static{
        // Load the permission storage!
        List<PermissionUser> perms = new ArrayList<>();
        try {
            CompoundTag file = NbtIo.read(BASE.toFile());
            ListTag lst = file.getList("perms", Tag.TAG_COMPOUND);
            for (Tag tag :
                    lst) {
                CompoundTag ct = (CompoundTag) tag;
                perms.add(new PermissionUser(ct));
            }
        } catch (IOException e) {

        }
        permissionUsers=perms;
    }


    public PermissionUser getUser(UUID user)
    {
        try{

            return permissionUsers.stream()
                    .filter(C->C.player.equals(user))
                    .collect(Collectors.toList()).get(0);
        }catch(Exception e)
        {
            return new PermissionUser(user);
        }
    }

    public static void save() throws IOException {
        CompoundTag ct = new CompoundTag();
        ListTag tag = new ListTag();
        for (PermissionUser user :
                permissionUsers) {
            tag.add(user.save());
        }

        ct.put("perms", tag);
        NbtIo.write(ct, BASE.toFile());
    }
}
