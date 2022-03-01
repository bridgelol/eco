package com.willfp.eco.internal.spigot.proxy.v1_17_R1.ai

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import net.minecraft.world.entity.AgeableMob
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.entity.ambient.AmbientCreature
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.monster.AbstractIllager
import net.minecraft.world.entity.monster.SpellcasterIllager
import net.minecraft.world.entity.monster.piglin.AbstractPiglin
import net.minecraft.world.entity.player.Player
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_17_R1.CraftServer
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity
import org.bukkit.entity.AbstractHorse
import org.bukkit.entity.AbstractSkeleton
import org.bukkit.entity.AbstractVillager
import org.bukkit.entity.Ageable
import org.bukkit.entity.Ambient
import org.bukkit.entity.Animals
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Illager
import org.bukkit.entity.Mob
import org.bukkit.entity.Monster
import org.bukkit.entity.PiglinAbstract
import org.bukkit.entity.Spellcaster
import org.bukkit.entity.Tameable
import java.util.Optional

private val mappedClasses = mapOf(
    Pair(AbstractHorse::class.java, net.minecraft.world.entity.animal.horse.AbstractHorse::class.java),
    Pair(AbstractSkeleton::class.java, net.minecraft.world.entity.monster.AbstractSkeleton::class.java),
    Pair(AbstractVillager::class.java, net.minecraft.world.entity.npc.AbstractVillager::class.java),
    Pair(Ageable::class.java, AgeableMob::class.java),
    Pair(Ambient::class.java, AmbientCreature::class.java),
    Pair(Animals::class.java, Animal::class.java),
    Pair(HumanEntity::class.java, Player::class.java), // Can't spawn players
    Pair(Illager::class.java, AbstractIllager::class.java),
    Pair(Mob::class.java, net.minecraft.world.entity.Mob::class.java),
    Pair(Monster::class.java, net.minecraft.world.entity.monster.Monster::class.java),
    Pair(PiglinAbstract::class.java, AbstractPiglin::class.java),
    Pair(org.bukkit.entity.Player::class.java, Player::class.java), // Can't spawn players
    Pair(Spellcaster::class.java, SpellcasterIllager::class.java),
    Pair(Tameable::class.java, TamableAnimal::class.java)
)

private val cache: LoadingCache<Class<out org.bukkit.entity.LivingEntity>, Optional<Class<out LivingEntity>>> =
    Caffeine.newBuilder()
        .build {
            val mapped = mappedClasses[it]
            if (mapped != null) {
                return@build Optional.of(mapped)
            }

            val world = Bukkit.getWorlds().first() as CraftWorld

            @Suppress("UNCHECKED_CAST")
            val nmsClass = Optional.ofNullable(runCatching {
                world.createEntity(
                    Location(world, 0.0, 100.0, 0.0),
                    it
                )::class.java as Class<out LivingEntity>
            }.getOrNull())

            return@build nmsClass
        }

fun <T : org.bukkit.entity.LivingEntity> Class<T>.toNMSClass(): Class<out LivingEntity> =
    cache.get(this).orElseThrow { IllegalArgumentException("Invalid/Unsupported entity type!") }

fun LivingEntity.toBukkitEntity(): org.bukkit.entity.LivingEntity? =
    CraftEntity.getEntity(Bukkit.getServer() as CraftServer, this) as? org.bukkit.entity.LivingEntity