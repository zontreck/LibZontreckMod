package dev.zontreck.libzontreck.util;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dev.zontreck.libzontreck.LibZontreck;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;


@EventBusSubscriber(modid=LibZontreck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DelayedExecutorService {
    private static int COUNT = 0;
    private static final DelayedExecutorService inst;
    static{
        inst=new DelayedExecutorService();
    }
    private DelayedExecutorService(){}

    public static DelayedExecutorService getInstance()
    {
        return inst;
    }
    public class DelayedExecution
    {
        public DelayedExecution(Runnable run, long unix) {
            scheduled=run;
            unix_time=unix;
        }
        public Runnable scheduled;
        public long unix_time;
    }

    public List<DelayedExecution> EXECUTORS = new ArrayList<>();

    public void schedule(Runnable run, int seconds)
    {
        long unix = Instant.now().getEpochSecond()+ (seconds);
        DelayedExecution exe = new DelayedExecution(run,unix);
        EXECUTORS.add(exe);
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent ev)
    {
        if(!LibZontreck.ALIVE)
        {
            LibZontreck.LOGGER.info("Tearing down delayed executor service");
            
            return;
        }
        Iterator<DelayedExecution> it = EXECUTORS.iterator();
        while(it.hasNext())
        {
            DelayedExecution e = it.next();
            if(e.unix_time < Instant.now().getEpochSecond())
            {
                it.remove();
                Thread tx = new Thread(e.scheduled);
                tx.setName("DelayedExecutorTask-"+String.valueOf(DelayedExecutorService.getNext()));
                tx.start();
            }
        }
    }

    public static int getNext()
    {
        COUNT++;
        return COUNT;
    }
}
