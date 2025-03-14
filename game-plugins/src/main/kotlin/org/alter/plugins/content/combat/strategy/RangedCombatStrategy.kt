package org.alter.plugins.content.combat.strategy

import org.alter.api.EquipmentType
import org.alter.api.Skills
import org.alter.api.WeaponType
import org.alter.api.ext.*
import org.alter.game.model.Tile
import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.PawnHit
import org.alter.game.model.combat.XpMode
import org.alter.game.model.entity.*
import org.alter.rscm.RSCM.getRSCM
import org.alter.plugins.content.combat.Combat
import org.alter.plugins.content.combat.CombatConfigs
import org.alter.plugins.content.combat.createProjectile
import org.alter.plugins.content.combat.dealHit
import org.alter.plugins.content.combat.formula.RangedCombatFormula
import org.alter.plugins.content.combat.strategy.ranged.RangedProjectile
import org.alter.plugins.content.combat.strategy.ranged.ammo.Darts
import org.alter.plugins.content.combat.strategy.ranged.ammo.Knives
import org.alter.plugins.content.combat.strategy.ranged.weapon.BowType
import org.alter.plugins.content.combat.strategy.ranged.weapon.Bows
import org.alter.plugins.content.combat.strategy.ranged.weapon.CrossbowType

/**
 * @author Tom <rspsmods@gmail.com>
 */
object RangedCombatStrategy : CombatStrategy {
    private const val DEFAULT_ATTACK_RANGE = 7

    private const val MAX_ATTACK_RANGE = 10

    override fun getAttackRange(pawn: Pawn): Int {
        if (pawn is Player) {
            val weapon = pawn.getEquipment(EquipmentType.WEAPON)
            val attackStyle = CombatConfigs.getAttackStyle(pawn)

            var range =
                when (weapon?.id) {
                    getRSCM("item.armadyl_crossbow") -> 8
                    getRSCM("item.craws_bow"), getRSCM("item.craws_bow_u") -> 10
                    getRSCM("item.chinchompa_10033"), getRSCM("item.red_chinchompa_10034"), getRSCM("item.black_chinchompa") -> 9
                    in Bows.LONG_BOWS -> 9
                    in Knives.KNIVES -> 6
                    in Darts.DARTS -> 3
                    in Bows.CRYSTAL_BOWS -> 10
                    else -> DEFAULT_ATTACK_RANGE
                }

            if (attackStyle == AttackStyle.LONG_RANGE) {
                range += 2
            }

            return Math.min(MAX_ATTACK_RANGE, range)
        }
        return DEFAULT_ATTACK_RANGE
    }

    override fun canAttack(
        pawn: Pawn,
        target: Pawn,
    ): Boolean {
        if (pawn is Player) {
            val weapon = pawn.getEquipment(EquipmentType.WEAPON)
            val ammo = pawn.getEquipment(EquipmentType.AMMO)

            val crossbow = CrossbowType.values.firstOrNull { it.item == weapon?.id }
            if (crossbow != null && ammo?.id !in crossbow.ammo) {
                val message = if (ammo != null) "You can't use that ammo with your crossbow." else "There is no ammo left in your quiver."
                pawn.message(message)
                return false
            }

            val bow = BowType.values.firstOrNull { it.item == weapon?.id }
            if (bow != null && bow.ammo.isNotEmpty() && ammo?.id !in bow.ammo) {
                val message = if (ammo != null) "You can't use that ammo with your bow." else "There is no ammo left in your quiver."
                pawn.message(message)
                return false
            }
        }
        return true
    }

    override fun attack(
        pawn: Pawn,
        target: Pawn,
    ) {
        val world = pawn.world

        val animation = CombatConfigs.getAttackAnimation(pawn)
        /**
         * @TODO Refactor
         */
        if (target is Player) {
            when (pawn) {
                is Npc -> {
                    CombatConfigs.getCombatDef(pawn)!!.let {
                        if (it.defaultAttackSoundArea) {
                            world.spawn(
                                AreaSound(pawn.tile, it.defaultAttackSound, it.defaultAttackSoundRadius, it.defaultAttackSoundVolume),
                            )
                        } else {
                            target.playSound(pawn.combatDef.defaultAttackSound, pawn.combatDef.defaultAttackSoundVolume)
                        }
                    }
                }
                // @TODO later for player block sound.
            }
        }
        /*
         * A list of actions that will be executed upon this hit dealing damage
         * to the [target].
         */
        var ammoDropAction: ((PawnHit).() -> Unit) = {}

        if (pawn is Player) {
            /*
             * Get the [EquipmentType] for the ranged weapon you're using.
             */
            val ammoSlot =
                when {
                    pawn.hasWeaponType(WeaponType.THROWN) || pawn.hasWeaponType(WeaponType.CHINCHOMPA) -> EquipmentType.WEAPON
                    else -> EquipmentType.AMMO
                }

            val ammo = pawn.getEquipment(ammoSlot)

            /*
             * Create a projectile based on ammo.
             */
            val ammoProjectile = if (ammo != null) RangedProjectile.values.firstOrNull { ammo.id in it.items } else null
            if (ammoProjectile != null) {
                val projectile = pawn.createProjectile(target, ammoProjectile.gfx, ammoProjectile.type)
                ammoProjectile.drawback?.let { drawback -> pawn.graphic(drawback) }
                ammoProjectile.impact?.let { impact -> target.graphic(impact.id, impact.height, projectile.lifespan) }
                world.spawn(projectile)
            }

            /*
             * Remove or drop ammo if applicable.
             */
            if (ammo != null && (ammoProjectile == null || !ammoProjectile.breakOnImpact())) {
                val chance = world.random(99)
                val breakAmmo = chance in 0..19
                val dropAmmo =
                    when {
                        pawn.hasEquipped(EquipmentType.CAPE, "item.avas_accumulator") -> chance in 20..27
                        pawn.hasEquipped(EquipmentType.CAPE, "item.avas_assembler") -> false
                        else -> !breakAmmo
                    }

                val amount = 1
                if (breakAmmo || dropAmmo) {
                    pawn.equipment.remove(ammo.id, amount)
                }
                if (dropAmmo) {
                    ammoDropAction = { world.spawn(GroundItem(ammo.id, amount, target.tile, pawn)) }
                }
            }
        }
        pawn.animate(animation)

        val formula = RangedCombatFormula
        val accuracy = formula.getAccuracy(pawn, target)
        val maxHit = formula.getMaxHit(pawn, target)
        val landHit = accuracy >= world.randomDouble()
        val hitDelay = getHitDelay(pawn.getCentreTile(), target.tile.transform(target.getSize() / 2, target.getSize() / 2))
        val damage =
            pawn.dealHit(
                target = target,
                maxHit = maxHit,
                landHit = landHit,
                delay = hitDelay,
                onHit = ammoDropAction,
            ).hit.hitmarks.sumOf { it.damage }

        if (damage > 0 && pawn.entityType.isPlayer) {
            addCombatXp(pawn as Player, target, damage)
        }
    }

    fun getHitDelay(
        start: Tile,
        target: Tile,
    ): Int {
        val distance = start.getDistance(target)
        return 2 + (Math.floor((3.0 + distance) / 6.0)).toInt()
    }

    private fun addCombatXp(
        player: Player,
        target: Pawn,
        damage: Int,
    ) {
        val modDamage = if (target.entityType.isNpc) Math.min(target.getCurrentHp(), damage) else damage
        val mode = CombatConfigs.getXpMode(player)
        val multiplier = if (target is Npc) Combat.getNpcXpMultiplier(target) else 1.0

        if (mode == XpMode.RANGED) {
            player.addXp(Skills.RANGED, modDamage * 4.0 * multiplier)
            player.addXp(Skills.HITPOINTS, modDamage * 1.33 * multiplier)
        } else if (mode == XpMode.SHARED) {
            player.addXp(Skills.RANGED, modDamage * 2.0 * multiplier)
            player.addXp(Skills.DEFENCE, modDamage * 2.0 * multiplier)
            player.addXp(Skills.HITPOINTS, modDamage * 1.33 * multiplier)
        }
    }
}
