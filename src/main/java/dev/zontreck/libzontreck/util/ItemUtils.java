package dev.zontreck.libzontreck.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class ItemUtils {
    public static Map<Enchantment, Integer> getEnchantments(ItemStack stack)
    {
        
        ListTag enchants = stack.getEnchantmentTags();
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        Iterator<Tag> enchantsIterator = enchants.iterator();
        while(enchantsIterator.hasNext())
        {
            CompoundTag theTag = (CompoundTag)enchantsIterator.next();
            Enchantment ench = Registry.ENCHANTMENT.getOptional(EnchantmentHelper.getEnchantmentId(theTag)).get();

            Integer level = EnchantmentHelper.getEnchantmentLevel(theTag);
            enchantments.put(ench, level);
            
        }

        return enchantments;
    }

    public static Integer getEnchantmentLevel(Enchantment ench, ItemStack stack)
    {
        Integer ret = 0;

        try{

            Map<Enchantment, Integer> enchants = getEnchantments(stack);
            ret=enchants.get(ench);
        }catch(Exception e)
        {
            ret =0;
        }

        return ret;
    }
}
