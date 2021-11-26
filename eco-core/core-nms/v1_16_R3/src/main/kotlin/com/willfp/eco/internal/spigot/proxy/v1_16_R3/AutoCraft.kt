package com.willfp.eco.internal.spigot.proxy.v1_16_R3
import com.willfp.eco.internal.spigot.proxy.AutoCraftProxy
import net.minecraft.server.v1_16_R3.MinecraftKey
import net.minecraft.server.v1_16_R3.PacketPlayOutAutoRecipe

class AutoCraft : AutoCraftProxy {
    override fun modifyPacket(packet: Any) {
        val recipePacket = packet as PacketPlayOutAutoRecipe
        val fKey = recipePacket.javaClass.getDeclaredField("b")
        fKey.isAccessible = true
        val key = fKey[recipePacket] as MinecraftKey
        fKey[recipePacket] = MinecraftKey(key.namespace, key.key + "_displayed")
    }
}