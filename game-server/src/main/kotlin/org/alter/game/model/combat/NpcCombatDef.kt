package org.alter.game.model.combat

import org.alter.game.model.weightedTableBuilder.LootTable

/**
 * Represents the combat definition for an npc.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class NpcCombatDef(
    val attack: Int,
    val defence: Int,
    val strength: Int,
    val hitpoints: Int,
    val ranged: Int,
    val magic: Int,
    val attackSpeed: Int,
    val attackAnimation: Int,
    val blockAnimation: Int,
    val deathAnimation: List<Int>,
    val defaultAttackSound: Int,
    val defaultAttackSoundArea: Boolean,
    val defaultAttackSoundRadius: Int,
    val defaultAttackSoundVolume: Int,
    val defaultBlockSound: Int,
    val defaultBlockSoundArea: Boolean,
    val defaultBlockSoundRadius: Int,
    val defaultBlockSoundVolume: Int,
    val defaultDeathSound: Int,
    val defaultDeathSoundArea: Boolean,
    val defaultDeathSoundRadius: Int,
    val defaultDeathSoundVolume: Int,
    val respawnDelay: Int,
    val aggressiveRadius: Int,
    val aggroTargetDelay: Int,
    val aggressiveTimer: Int,
    val poisonChance: Double,
    val venomChance: Double,
    val slayerReq: Int,
    val slayerXp: Double,
    val bonuses: List<Int>,
    val species: Set<Any>,
    val LootTables: MutableSet<LootTable>?,
    val immunePoison: Boolean,
    val immuneVenom: Boolean,
    val immuneCannons: Boolean,
    val immuneThralls: Boolean
) {
    companion object {
        private const val DEFAULT_HITPOINTS = 10
        private const val DEFAULT_ATTACK_SPEED = 4
        private const val DEFAULT_RESPAWN_DELAY = 25
        private const val DEFAULT_ATTACK_ANIMATION = 422
        private const val DEFAULT_BLOCK_ANIMATION = 424
        private const val DEFAULT_DEATH_ANIMATION = 836

        val DEFAULT =
            NpcCombatDef(
                hitpoints = DEFAULT_HITPOINTS,
                attack = 0,
                defence = 0,
                strength = 0,
                ranged = 0,
                magic = 0,
                attackSpeed = DEFAULT_ATTACK_SPEED,
                aggressiveRadius = 0,
                aggroTargetDelay = 0,
                aggressiveTimer = 0,
                attackAnimation = DEFAULT_ATTACK_ANIMATION,
                blockAnimation = DEFAULT_BLOCK_ANIMATION,
                deathAnimation = listOf(DEFAULT_DEATH_ANIMATION),
                defaultAttackSound = -1,
                defaultAttackSoundArea = false,
                defaultAttackSoundRadius = -1,
                defaultAttackSoundVolume = -1,
                defaultBlockSound = -1,
                defaultBlockSoundArea = false,
                defaultBlockSoundRadius = -1,
                defaultBlockSoundVolume = -1,
                defaultDeathSound = -1,
                defaultDeathSoundArea = false,
                defaultDeathSoundRadius = -1,
                defaultDeathSoundVolume = -1,
                respawnDelay = DEFAULT_RESPAWN_DELAY,
                poisonChance = 0.0,
                venomChance = 0.0,
                slayerReq = 1,
                slayerXp = 0.0,
                bonuses = emptyList(),
                species = emptySet(),
                LootTables = null,
                immunePoison = false,
                immuneVenom = false,
                immuneCannons = false,
                immuneThralls = false
            )
    }
}
