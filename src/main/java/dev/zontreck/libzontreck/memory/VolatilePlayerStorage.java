package dev.zontreck.libzontreck.memory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

// This class is a universal data storage class for per-player data storage
// If a player logs out, this memory gets erased. Do not store anything here that requires persistence. Store it on the player instead.
public class VolatilePlayerStorage {
    private List<PlayerContainer> datas = new ArrayList<>();


    public void update(UUID player, PlayerContainer comp)
    {
        int indexOf = index(player);
        if(indexOf!=-1)
            datas.set(indexOf, comp);
    }

    public PlayerContainer getNew(UUID player)
    {
        if(index(player)==-1)
        {
            PlayerContainer comp = new PlayerContainer(player);
            datas.add(comp);
            return comp;
        }else return get(player);
    }

    public PlayerContainer get(UUID player)
    {
        int indexOf = index(player);
        if(indexOf!=-1)
        {
            return datas.get(indexOf);
        }else return getNew(player);
    }

    public void delete(UUID id)
    {
        int index = index(id);
        if(index!=-1)
        {
            datas.remove(index);
        }
    }
    
    // Returns the index of the component by ID, or -1 if not found
    public int index(UUID id)
    {
        int ret=-1;
        Iterator<PlayerContainer> it = datas.iterator();
        while(it.hasNext())
        {
            PlayerContainer comp = it.next();
            if(comp.ID.equals(id))
            {
                ret=datas.indexOf(comp);
                break;
            }
        }


        return ret;
    }
}
