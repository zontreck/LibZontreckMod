package dev.zontreck.libzontreck.profiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.events.ProfileCreatedEvent;
import dev.zontreck.libzontreck.events.ProfileSavingEvent;
import dev.zontreck.libzontreck.events.ProfileUnloadedEvent;
import dev.zontreck.libzontreck.events.ProfileUnloadingEvent;
import dev.zontreck.libzontreck.util.ServerUtilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;

/**
 * A libZontreck user profile
 *<p></p>
 * This is used to contain common player data, as well as be capable of serializing the player's data and sending to/from the client.
 */
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
    public int deaths;


    public ServerPlayer player;
    private File accessor;
    private CompoundTag miscData;
    public CompoundTag NBT;

    public static final Path BASE;
    public static final Profile SYSTEM;
    static{
        BASE = LibZontreck.BASE_CONFIG.resolve("profiles");
        if(!BASE.toFile().exists())
        {
            try {
                Files.createDirectory(BASE);
                LibZontreck.LOGGER.info("Created profiles base directory");
            } catch (IOException e) {
                e.printStackTrace();
                LibZontreck.LOGGER.error("Failed to create profiles base directory");
            }
        }

        SYSTEM = new Profile("SYSTEM", "", "SYSTEM", ChatColor.DARK_RED, LibZontreck.NULL_ID.toString(), "", "", false, 0, null, 0, null, null, new CompoundTag());
    }

    public Profile(String username, String prefix, String nickname, String name_color, String ID, String prefix_color, String chat_color, Boolean isFlying, int vaults, File vaultFile, int deathCount, ServerPlayer player, CompoundTag misc, CompoundTag NBT) {
        this.username = username;
        this.prefix = prefix;
        this.nickname = nickname;
        this.name_color = name_color;
        this.user_id = ID;
        this.prefix_color = prefix_color;
        this.chat_color = chat_color;
        this.flying=isFlying;
        this.available_vaults=vaults;
        this.deaths=deathCount;
        this.player=player;
        miscData = misc;
        this.NBT = NBT;


        this.accessor = vaultFile;
    }

    public boolean runSanityChecks()
    {
        if(player == null) return false;
        return true;
    }


    /**
     * Gets, or loads from persistent data, the user's LibZ profile
     * @param UUID Player ID
     * @return Profile
     * @throws UserProfileNotYetExistsException When the profile has not yet been created
     */
    public static Profile get_profile_of(String UUID) throws UserProfileNotYetExistsException
    {
        if(LibZontreck.PROFILES.containsKey(UUID)){
            Profile prof = LibZontreck.PROFILES.get(UUID);
            if(!prof.runSanityChecks())
            {
                prof.player = ServerUtilities.getPlayerByID(UUID);
            }
            return prof;
        }else {
            // Create or load profile
            Path userProfile = BASE.resolve(UUID);
            if(userProfile.toFile().exists())
            {
                // Load profile data
                File ace = userProfile.resolve("profile.nbt").toFile();
                try {
                    Profile actual = load(NbtIo.read(ace), ace, ServerUtilities.getPlayerByID(UUID));
                    LibZontreck.PROFILES.put(UUID, actual);
                    return actual;
                } catch (IOException e) {
                    throw new UserProfileNotYetExistsException(UUID);
                } catch (NullPointerException e)
                {
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

    private static Profile load(CompoundTag tag, File accessor, ServerPlayer player)
    {
        return new Profile(tag.getString("user"), tag.getString("prefix"), tag.getString("nick"), tag.getString("nickc"), tag.getString("id"), tag.getString("prefixc"), tag.getString("chatc"), tag.getBoolean("flying"), tag.getInt("vaults"), accessor, tag.getInt("deaths"), player, tag.getCompound("misc"), tag.getCompound("data"));
    }

    /**
     * Generate a new user profile.
     *
     * @param player The player to generate the profile for
     */
    private static void generateNewProfile(ServerPlayer player)
    {
        
        Path userProfile = BASE.resolve(player.getStringUUID());
        if(userProfile.toFile().exists())
        {
            // Load profile data
            File ace = userProfile.resolve("profile.nbt").toFile();
            Profile template = new Profile(player.getName().getString(), "Member", player.getDisplayName().getString(), ChatColor.GREEN, player.getStringUUID(), ChatColor.AQUA, ChatColor.WHITE, false, 0, ace, 0, player, new CompoundTag(), new CompoundTag());
            template.commit();

            LibZontreck.PROFILES.put(player.getStringUUID(), template);

            
            MinecraftForge.EVENT_BUS.post(new ProfileCreatedEvent(template));
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
        if(MinecraftForge.EVENT_BUS.post(new ProfileUnloadingEvent(this, player)))
        {
            commit();
        }
        LibZontreck.LOGGER.info("Profile is unloaded for "+username);
        MinecraftForge.EVENT_BUS.post(new ProfileUnloadedEvent(user_id));
    }

    public static void unload(Profile prof)
    {
        LibZontreck.PROFILES.remove(prof.user_id);
        prof=null;
    }

    /**
     * Creates a profile if it does not yet exist
     * @param play Player
     * @return The existing, or new, profile instance.
     */
    public static Profile factory(ServerPlayer play)
    {
        try {
            return get_profile_of(play.getStringUUID());
        } catch (UserProfileNotYetExistsException e) {
            generateNewProfile(play);
            return factory(play);
        }
    }

    /**
     * Save the profile to disk
     */
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
        serial.putInt("deaths", deaths);
        ProfileSavingEvent event = new ProfileSavingEvent(this, miscData);
        MinecraftForge.EVENT_BUS.post(event);
        serial.put("misc", event.tag);
        serial.put("data", NBT);



        try {
            NbtIo.write(serial, accessor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
