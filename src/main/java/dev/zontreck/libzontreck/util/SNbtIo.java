package dev.zontreck.libzontreck.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.zontreck.ariaslib.util.FileIO;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

import java.io.File;
import java.nio.file.Path;

/**
 * Provides helpers for reading and writing snbt to file
 */
public class SNbtIo
{
    /**
     * Read the file at the path, and deserialize from snbt
     * @param path The file to load
     * @return The deserialized compound tag, or a blank tag
     */
    public static CompoundTag loadSnbt(Path path)
    {
        if(!path.toFile().exists())
            return new CompoundTag();
        else {
            File fi = path.toFile();
            try {
                return NbtUtils.snbtToStructure(FileIO.readFile(fi.getAbsolutePath()));
            } catch (CommandSyntaxException e) {
                return new CompoundTag();
            }
        }
    }

    /**
     * Writes the tag to the file specified
     * @param path The file to write
     * @param tag The tag to serialize
     */
    public static void writeSnbt(Path path, CompoundTag tag)
    {
        String snbt = NbtUtils.structureToSnbt(tag);
        FileIO.writeFile(path.toFile().getAbsolutePath(), snbt);
    }
}