package dev.zontreck.libzontreck.chestgui;

import dev.zontreck.libzontreck.lore.LoreContainer;
import dev.zontreck.libzontreck.lore.LoreEntry;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.Vector2;
import dev.zontreck.libzontreck.vectors.Vector2i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ChestGUIButton
{
    private Item icon;
    private String name;
    private List<LoreEntry> tooltipInfo = new ArrayList<>();

    private IChestGUIButtonCallback callback;
    private CompoundTag NBT = new CompoundTag();

    /**
     * Position is Row (X), Column (Y)
     */
    private Vector2i position;
    private ItemStack built;
    private ItemStackHandler container;
    private LoreContainer lore;

    /**
     * Sets the name of the ChestGUI Button (Item Name)
     * @param name Name to set
     * @return Button instance
     */
    public ChestGUIButton withName(String name)
    {
        this.name=name;
        return this;
    }


    public ChestGUIButton(Item icon, String name, IChestGUIButtonCallback callback, Vector2i position)
    {
        this.icon = icon;
        this.name = name;
        this.callback = callback;
        this.position = position;
        tooltipInfo = new ArrayList<>();
    }

    public ChestGUIButton(ItemStack existing, IChestGUIButtonCallback callback, Vector2i position)
    {
        this.callback = callback;
        this.position = position;

        LoreContainer container = new LoreContainer(existing);
        tooltipInfo = container.miscData.loreData;
        name = existing.getHoverName().getString();
        icon = existing.getItem();
        NBT = existing.getTag();

    }

    public ChestGUIButton withNBT(CompoundTag tag)
    {
        this.NBT = tag;
        return this;
    }

    public ItemStack buildIcon()
    {
        ItemStack ret = new ItemStack(icon,1);


        NBT.putInt("slot", getSlotNum());
        NBT.put("pos", position.serialize());

        ret.setTag(NBT);
        LoreContainer cont = new LoreContainer(ret);
        cont.clear();

        cont.miscData.loreData.addAll(tooltipInfo);

        cont.commitLore();


        ret = ret.setHoverName(ChatHelpers.macro(name));
        built=ret;
        lore=cont;

        return ret;
    }

    public ItemStackHandler buildIconStack()
    {
        ItemStack stack = buildIcon();
        ItemStackHandler st = new ItemStackHandler(1);
        st.setStackInSlot(0, stack);

        built=stack;
        lore = new LoreContainer(built);
        return st;
    }

    protected ChestGUIButton withContainer(ItemStackHandler handler)
    {
        this.container=handler;
        return this;
    }

    /**
     * Adds a line to the Lore (Tooltip) of the button
     * @param line The line to add
     * @return ChestGUIButton instance
     */
    public ChestGUIButton withInfo(LoreEntry line)
    {
        tooltipInfo.add(line);

        return this;
    }

    /**
     * Sets the position and returns the builder
     * @param pos New button position
     * @return This button instance
     */
    public ChestGUIButton withPosition(Vector2i pos)
    {
        this.position=pos;

        return this;
    }

    /**
     * Check if the slot's row and column match (X,Y)
     * @param slot
     * @return True if matches
     */
    public boolean matchesSlot(Vector2i slot)
    {
        return position.same(slot);
    }

    /**
     * Returns the slot number in the 32 item grid
     * @return Slot number from vector (row, column)
     */
    public int getSlotNum()
    {
        return (int) Math.floor((position.x * 9) + position.y);
    }

    private static long lastInvoked = 0;
    /**
     * Button was clicked!
     */
    public void clicked()
    {
        if(Instant.now().getEpochSecond() > (lastInvoked + 1))
        {

            lastInvoked = Instant.now().getEpochSecond();
            callback.run(built, container, lore);
        }
    }
}
