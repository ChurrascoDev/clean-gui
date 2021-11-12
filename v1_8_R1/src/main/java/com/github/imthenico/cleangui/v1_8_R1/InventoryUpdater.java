package com.github.imthenico.cleangui.v1_8_R1;

import net.minecraft.server.v1_8_R1.ChatComponentText;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftContainer;
import org.bukkit.entity.Player;

public class InventoryUpdater implements com.github.imthenico.cleangui.nms.InventoryUpdater {

    @Override
    public void updateInventory(Player player, String title) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(
                entityPlayer.activeContainer.windowId,
                CraftContainer.getNotchInventoryType(player.getOpenInventory().getTopInventory().getType()),
                new ChatComponentText(title),
                player.getOpenInventory().getTopInventory().getSize()
        );

        entityPlayer.playerConnection.sendPacket(packet);
        player.updateInventory();
    }
}