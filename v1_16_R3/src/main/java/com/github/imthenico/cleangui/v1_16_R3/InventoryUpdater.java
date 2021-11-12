package com.github.imthenico.cleangui.v1_16_R3;

import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftContainer;
import org.bukkit.entity.Player;

public class InventoryUpdater implements com.github.imthenico.cleangui.nms.InventoryUpdater {

    @Override
    public void updateInventory(Player player, String title) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(
                entityPlayer.activeContainer.windowId,
                CraftContainer.getNotchInventoryType(player.getOpenInventory().getTopInventory()),
                new ChatComponentText(title)
        );

        entityPlayer.playerConnection.sendPacket(packet);
        player.updateInventory();
    }
}