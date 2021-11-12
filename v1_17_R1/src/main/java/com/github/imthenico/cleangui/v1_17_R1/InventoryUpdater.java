package com.github.imthenico.cleangui.v1_17_R1;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftContainer;
import org.bukkit.entity.Player;

public class InventoryUpdater implements com.github.imthenico.cleangui.nms.InventoryUpdater {

    @Override
    public void updateInventory(Player player, String title) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(
                entityPlayer.bV.j,
                CraftContainer.getNotchInventoryType(player.getOpenInventory().getTopInventory()),
                new ChatComponentText(title)
        );

        entityPlayer.b.sendPacket(packet);
        player.updateInventory();
    }
}