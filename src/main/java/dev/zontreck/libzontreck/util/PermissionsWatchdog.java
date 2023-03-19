package dev.zontreck.libzontreck.util;

import dev.zontreck.libzontreck.permissions.PermissionStorage;
import dev.zontreck.libzontreck.permissions.PermissionUser;

import java.io.IOException;
import java.util.List;

public class PermissionsWatchdog implements Runnable
{
    public List<PermissionUser> perms;

    @Override
    public void run() {
        if(!perms.equals(PermissionStorage.getListOfUsers()))
        {
            try {
                PermissionStorage.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            perms = PermissionStorage.getListOfUsers();
        }

        DelayedExecutorService.getInstance().schedule(this, 10);
    }
}
