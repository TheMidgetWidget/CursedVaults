package me.lightlord323dev.cursedvaults.api.user;

import java.util.UUID;

/**
 * Created by Luda on 8/18/2020.
 */
public class CursedVaultUser {

    private UUID user, vault;

    public CursedVaultUser(UUID user, UUID vault) {
        this.user = user;
        this.vault = vault;
    }

    public UUID getUser() {
        return user;
    }

    public UUID getVault() {
        return vault;
    }
}
