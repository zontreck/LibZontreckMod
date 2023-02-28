package dev.zontreck.libzontreck.profiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.events.ProfileCreatedEvent;
import dev.zontreck.libzontreck.events.ProfileUnloadedEvent;
import dev.zontreck.libzontreck.util.FileTreeDatastore;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;

public class Profile {
    public String username;
    public String user_id;
    public String prefix;
    public String nickname;
    public String name_color; // ChatColor.X
    public String prefix_color;
    public String chat_color;
    public Boolean flying;
    public int available_vaults;

    private File accessor;

    public static final Path BASE;
    static{
        BASE = LibZontreck.BASE_CONFIG.resolve("profiles");
        if(!BASE.toFile().exists())
        {
            try {
                Files.createDirectory(BASE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Profile(String username, String prefix, String nickname, String name_color, String ID, String prefix_color, String chat_color, Boolean isFlying, int vaults, File vaultFile) {
        this.username = username;
        this.prefix = prefix;
        this.nickname = nickname;
        this.name_color = name_color;
        this.user_id = ID;
        this.prefix_color = prefix_color;
        this.chat_color = chat_color;
        this.flying=isFlying;
        this.available_vaults=vaults;


        this.accessor = vaultFile;
    }


    public static Profile get_profile_of(String UUID) throws UserProfileNotYetExistsException
    {
        if(LibZontreck.PROFILES.containsKey(UUID)){
            return LibZontreck.PROFILES.get(UUID);
        }else {
            // Create or load profile
            Path userProfile = BASE.resolve(UUID);
            if(userProfile.toFile().exists())
            {
                // Load profile data
                File ace = userProfile.resolve("profile.nbt").toFile();
                try {
                    Profile actual = load(NbtIo.read(ace), ace);
                    LibZontreck.PROFILES.put(UUID, actual);
                    return actual;
                } catch (IOException e) {
                    throw new UserProfileNotYetExistsException(UUID);
                }

            }else {
                // Create directory, then throw a exception so a new profile gets created
                try {
                    Files.createDirectories(userProfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                throw new UserProfileNotYetExistsException(UUID);
            }
        }
    }

    private static Profile load(CompoundTag tag, File accessor)
    {
        return new Profile(tag.getString("user"), tag.getString("prefix"), tag.getString("nick"), tag.getString("nickc"), tag.getString("id"), tag.getString("prefixc"), tag.getString("chatc"), tag.getBoolean("flying"), tag.getInt("vaults"), accessor);
    }

    private static void generateNewProfile(ServerPlayer player)
    {
        
        Path userProfile = BASE.resolve(player.getStringUUID());
        if(userProfile.toFile().exists())
        {
            // Load profile data
            File ace = userProfile.resolve("profile.dat").toFile();
            Profile template = new Profile(player.getName().getString(), "Member", player.getDisplayName().getString(), ChatColor.GREEN, player.getStringUUID(), ChatColor.AQUA, ChatColor.WHITE, false, 0, ace);
            template.commit();

            
            MinecraftForge.EVENT_BUS.post(new ProfileCreatedEvent(template));

            template=null;
            return;
        }else {
            try {
                Files.createDirectories(userProfile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            generateNewProfile(player);
        }
    }

    @Override
    public void finalize()
    {
        LibZontreck.LOGGER.info("Profile is unloaded for "+username);
        MinecraftForge.EVENT_BUS.post(new ProfileUnloadedEvent(user_id));
    }

    public static void unload(Profile prof)
    {
        LibZontreck.PROFILES.remove(prof.user_id);
        prof=null;
    }

    public static Profile factory(ServerPlayer play)
    {
        try {
            return get_profile_of(play.getStringUUID());
        } catch (UserProfileNotYetExistsException e) {
            generateNewProfile(play);
            return factory(play);
        }
    }

    public void commit()
    {
        // Save data to FileTree
        CompoundTag serial = new CompoundTag();
        serial.putString("user", username);
        serial.putString("prefix", prefix);
        serial.putString("nick", nickname);
        serial.putString("id", user_id);
        serial.putString("nickc", name_color);
        serial.putString("prefixc", prefix_color);
        serial.putString("chatc", chat_color);
        serial.putBoolean("flying", flying);
        serial.putInt("vaults", available_vaults);



        try {
            NbtIo.write(serial, accessor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
