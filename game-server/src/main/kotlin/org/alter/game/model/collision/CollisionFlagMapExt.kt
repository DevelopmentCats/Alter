package org.alter.game.model.collision

import dev.openrune.cache.CacheManager
import org.alter.game.model.Tile
import org.alter.game.model.entity.GameObject
import org.rsmod.routefinder.LineValidator
import org.rsmod.routefinder.collision.CollisionFlagMap
import org.rsmod.routefinder.flag.CollisionFlag.BLOCK_NPCS
import org.rsmod.routefinder.flag.CollisionFlag.GROUND_DECOR
import org.rsmod.routefinder.flag.CollisionFlag.LOC
import org.rsmod.routefinder.flag.CollisionFlag.LOC_PROJ_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.LOC_ROUTE_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_EAST
import org.rsmod.routefinder.flag.CollisionFlag.WALL_EAST_PROJ_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_EAST_ROUTE_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_NORTH
import org.rsmod.routefinder.flag.CollisionFlag.WALL_NORTH_EAST
import org.rsmod.routefinder.flag.CollisionFlag.WALL_NORTH_EAST_PROJ_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_NORTH_EAST_ROUTE_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_NORTH_PROJ_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_NORTH_ROUTE_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_NORTH_WEST
import org.rsmod.routefinder.flag.CollisionFlag.WALL_NORTH_WEST_PROJ_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_NORTH_WEST_ROUTE_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_SOUTH
import org.rsmod.routefinder.flag.CollisionFlag.WALL_SOUTH_EAST
import org.rsmod.routefinder.flag.CollisionFlag.WALL_SOUTH_EAST_PROJ_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_SOUTH_EAST_ROUTE_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_SOUTH_PROJ_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_SOUTH_ROUTE_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_SOUTH_WEST
import org.rsmod.routefinder.flag.CollisionFlag.WALL_SOUTH_WEST_PROJ_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_SOUTH_WEST_ROUTE_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_WEST
import org.rsmod.routefinder.flag.CollisionFlag.WALL_WEST_PROJ_BLOCKER
import org.rsmod.routefinder.flag.CollisionFlag.WALL_WEST_ROUTE_BLOCKER

fun CollisionFlagMap.isClipped(tile: Tile): Boolean {
    val zoneIndex = zoneIndex(tile.x, tile.z, tile.height)
    val tileIndex = tileIndex(tile.x, tile.z)
    return flags[zoneIndex]?.get(tileIndex) != 0
}

/**
 * Casts a line using Bresenham's Line Algorithm with point A [start] and
 * point B [target] being its two points and makes sure that there's no
 * collision flag that can block movement from and to both points.
 *
 * @param projectile
 * Projectiles have a higher tolerance for certain objects when the object's
 * metadata explicitly allows them to.
 */
fun CollisionFlagMap.raycast(
    start: Tile,
    target: Tile,
    projectile: Boolean,
): Boolean {
    check(start.height == target.height) { "Tiles must be on the same height level." }

    var x0 = start.x
    var y0 = start.z
    val x1 = target.x
    val y1 = target.z
    val height = start.height
    val validator = LineValidator(this)
    return if (projectile) {
        validator.hasLineOfSight(height, x0, y0, x1, y1)
    } else {
        validator.hasLineOfWalk(height, x0, y0, x1, y1)
    }
}

private fun tileIndex(
    x: Int,
    y: Int,
): Int = (x and 0x7) or ((y and 0x7) shl 3)

private fun zoneIndex(
    x: Int,
    y: Int,
    level: Int,
): Int =
    ((x shr 3) and 0x7FF) or
        (((y shr 3) and 0x7FF) shl 11) or ((level and 0x3) shl 22)

fun CollisionFlagMap.addActorCollision(tile: Tile) {
    addCollisionFlag(tile, BLOCK_NPCS, true)
}

fun CollisionFlagMap.removeActorCollision(tile: Tile) {
    addCollisionFlag(tile, BLOCK_NPCS, false)
}

fun CollisionFlagMap.addObjectCollision(
    obj: GameObject,
) = changeNormalCollision(obj, true)

fun CollisionFlagMap.removeObjectCollision(
    obj: GameObject,
) = changeNormalCollision(obj, false)

private fun CollisionFlagMap.changeNormalCollision(
    obj: GameObject,
    add: Boolean,
) {
    val def = CacheManager.getObject(obj.id)
    val shape = obj.type
    val location = obj.tile
    val rotation = obj.rot
    val solid = def.solid
    val blockProjectile = def.impenetrable
    val breakRouteFinding = def.obstructive

    when {
        shape in GameObjectShape.WALL_SHAPES && solid != 0 -> {
            changeWallCollision(
                location,
                rotation,
                shape,
                blockProjectile,
                !breakRouteFinding,
                add,
            )
        }
        shape in GameObjectShape.NORMAL_SHAPES && solid != 0 -> {
            var width = def.sizeX
            var length = def.sizeY
            if (rotation == 1 || rotation == 3) {
                width = def.sizeY
                length = def.sizeX
            }
            changeNormalCollision(
                location,
                width,
                length,
                blockProjectile,
                !breakRouteFinding,
                add,
            )
        }
        shape in GameObjectShape.GROUND_DECOR_SHAPES && solid == 1 -> changeFloorDecor(location, add)
    }
}

private fun CollisionFlagMap.changeNormalCollision(
    tile: Tile,
    width: Int,
    length: Int,
    blocksProjectile: Boolean,
    breakRouteFinding: Boolean,
    add: Boolean,
) {
    var flag = LOC

    if (blocksProjectile) {
        flag = flag or LOC_PROJ_BLOCKER
    }

    if (breakRouteFinding) {
        flag = flag or LOC_ROUTE_BLOCKER
    }

    for (x in 0 until width) {
        for (y in 0 until length) {
            val translate = tile.transform(x, y)
            addCollisionFlag(translate, flag, add)
        }
    }
}

private fun CollisionFlagMap.changeWallRouteFinding(
    tile: Tile,
    rotation: Int,
    shape: Int,
    add: Boolean,
) {
    when (shape) {
        GameObjectShape.WALL_STRAIGHT ->
            when (rotation) {
                0 -> {
                    addCollisionFlag(tile, WALL_WEST_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(-1, 0), WALL_EAST_ROUTE_BLOCKER, add)
                }
                1 -> {
                    addCollisionFlag(tile, WALL_NORTH_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(0, 1), WALL_SOUTH_ROUTE_BLOCKER, add)
                }
                2 -> {
                    addCollisionFlag(tile, WALL_EAST_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(1, 0), WALL_WEST_ROUTE_BLOCKER, add)
                }
                3 -> {
                    addCollisionFlag(tile, WALL_SOUTH_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(0, -1), WALL_NORTH_ROUTE_BLOCKER, add)
                }
            }
        GameObjectShape.WALL_DIAGONALCORNER, GameObjectShape.WALL_SQUARECORNER ->
            when (rotation) {
                0 -> {
                    addCollisionFlag(tile, WALL_NORTH_WEST_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(-1, 1), WALL_SOUTH_EAST_ROUTE_BLOCKER, add)
                }
                1 -> {
                    addCollisionFlag(tile, WALL_NORTH_EAST_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(1, 1), WALL_SOUTH_WEST_ROUTE_BLOCKER, add)
                }
                2 -> {
                    addCollisionFlag(tile, WALL_SOUTH_EAST_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(1, -1), WALL_NORTH_WEST_ROUTE_BLOCKER, add)
                }
                3 -> {
                    addCollisionFlag(tile, WALL_SOUTH_WEST_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(-1, -1), WALL_NORTH_EAST_ROUTE_BLOCKER, add)
                }
            }
        GameObjectShape.WALL_L ->
            when (rotation) {
                0 -> {
                    addCollisionFlag(tile, WALL_NORTH_ROUTE_BLOCKER or WALL_WEST_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(-1, 0), WALL_EAST_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(0, 1), WALL_SOUTH_ROUTE_BLOCKER, add)
                }
                1 -> {
                    addCollisionFlag(tile, WALL_NORTH_ROUTE_BLOCKER or WALL_EAST_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(0, 1), WALL_SOUTH_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(1, 0), WALL_WEST_ROUTE_BLOCKER, add)
                }
                2 -> {
                    addCollisionFlag(tile, WALL_SOUTH_ROUTE_BLOCKER or WALL_EAST_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(1, 0), WALL_WEST_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(0, -1), WALL_NORTH_ROUTE_BLOCKER, add)
                }
                3 -> {
                    addCollisionFlag(tile, WALL_SOUTH_ROUTE_BLOCKER or WALL_WEST_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(0, -1), WALL_NORTH_ROUTE_BLOCKER, add)
                    addCollisionFlag(tile.transform(-1, 0), WALL_EAST_ROUTE_BLOCKER, add)
                }
            }
    }
}

private fun CollisionFlagMap.changeWallCollision(
    tile: Tile,
    rotation: Int,
    shape: Int,
    blockProjectile: Boolean,
    breakRouteFinding: Boolean,
    add: Boolean,
) {
    changeWallCollision(tile, rotation, shape, add)
    if (blockProjectile) changeWallProjectileCollision(tile, rotation, shape, add)
    if (breakRouteFinding) changeWallRouteFinding(tile, rotation, shape, add)
}

private fun CollisionFlagMap.changeWallProjectileCollision(
    tile: Tile,
    rotation: Int,
    shape: Int,
    add: Boolean,
) {
    when (shape) {
        GameObjectShape.WALL_STRAIGHT ->
            when (rotation) {
                0 -> {
                    addCollisionFlag(tile, WALL_WEST_PROJ_BLOCKER, add)
                    addCollisionFlag(tile.transform(-1, 0), WALL_EAST_PROJ_BLOCKER, add)
                }
                1 -> {
                    addCollisionFlag(tile, WALL_NORTH_PROJ_BLOCKER, add)
                    addCollisionFlag(tile.transform(0, 1), WALL_SOUTH_PROJ_BLOCKER, add)
                }
                2 -> {
                    addCollisionFlag(tile, WALL_EAST_PROJ_BLOCKER, add)
                    addCollisionFlag(tile.transform(1, 0), WALL_WEST_PROJ_BLOCKER, add)
                }
                3 -> {
                    addCollisionFlag(tile, WALL_SOUTH_PROJ_BLOCKER, add)
                    addCollisionFlag(tile.transform(0, -1), WALL_NORTH_PROJ_BLOCKER, add)
                }
            }
        GameObjectShape.WALL_DIAGONALCORNER, GameObjectShape.WALL_SQUARECORNER ->
            when (rotation) {
                0 -> {
                    addCollisionFlag(tile, WALL_NORTH_WEST_PROJ_BLOCKER, add)
                    addCollisionFlag(tile.transform(-1, 1), WALL_SOUTH_EAST_PROJ_BLOCKER, add)
                }
                1 -> {
                    addCollisionFlag(tile, WALL_NORTH_EAST_PROJ_BLOCKER, add)
                    addCollisionFlag(tile.transform(1, 1), WALL_SOUTH_WEST_PROJ_BLOCKER, add)
                }
                2 -> {
                    addCollisionFlag(tile, WALL_SOUTH_EAST_PROJ_BLOCKER, add)
                    addCollisionFlag(tile.transform(1, -1), WALL_NORTH_WEST_PROJ_BLOCKER, add)
                }
                3 -> {
                    addCollisionFlag(tile, WALL_SOUTH_WEST_PROJ_BLOCKER, add)
                    addCollisionFlag(tile.transform(-1, -1), WALL_NORTH_EAST_PROJ_BLOCKER, add)
                }
            }
        GameObjectShape.WALL_L ->
            when (rotation) {
                0 -> {
                    val flag = WALL_WEST_PROJ_BLOCKER or WALL_NORTH_PROJ_BLOCKER
                    addCollisionFlag(tile, flag, add)
                    addCollisionFlag(tile.transform(-1, 0), WALL_EAST_PROJ_BLOCKER, add)
                    addCollisionFlag(tile.transform(0, 1), WALL_SOUTH_PROJ_BLOCKER, add)
                }
                1 -> {
                    val flag = WALL_NORTH_PROJ_BLOCKER or WALL_EAST_PROJ_BLOCKER
                    addCollisionFlag(tile, flag, add)
                    addCollisionFlag(tile.transform(0, 1), WALL_SOUTH_PROJ_BLOCKER, add)
                    addCollisionFlag(tile.transform(1, 0), WALL_WEST_PROJ_BLOCKER, add)
                }
                2 -> {
                    val flag = WALL_EAST_PROJ_BLOCKER or WALL_SOUTH_PROJ_BLOCKER
                    addCollisionFlag(tile, flag, add)
                    addCollisionFlag(tile.transform(1, 0), WALL_WEST_PROJ_BLOCKER, add)
                    addCollisionFlag(tile.transform(0, -1), WALL_NORTH_PROJ_BLOCKER, add)
                }
                3 -> {
                    val flag = WALL_SOUTH_PROJ_BLOCKER or WALL_WEST_PROJ_BLOCKER
                    addCollisionFlag(tile, flag, add)
                    addCollisionFlag(tile.transform(0, -1), WALL_NORTH_PROJ_BLOCKER, add)
                    addCollisionFlag(tile.transform(-1, 0), WALL_EAST_PROJ_BLOCKER, add)
                }
            }
    }
}

private fun CollisionFlagMap.changeWallCollision(
    tile: Tile,
    rotation: Int,
    shape: Int,
    add: Boolean,
) {
    when (shape) {
        GameObjectShape.WALL_STRAIGHT ->
            when (rotation) {
                0 -> {
                    addCollisionFlag(tile, WALL_WEST, add)
                    addCollisionFlag(tile.transform(-1, 0), WALL_EAST, add)
                }
                1 -> {
                    addCollisionFlag(tile, WALL_NORTH, add)
                    addCollisionFlag(tile.transform(0, 1), WALL_SOUTH, add)
                }
                2 -> {
                    addCollisionFlag(tile, WALL_EAST, add)
                    addCollisionFlag(tile.transform(1, 0), WALL_WEST, add)
                }
                3 -> {
                    addCollisionFlag(tile, WALL_SOUTH, add)
                    addCollisionFlag(tile.transform(0, -1), WALL_NORTH, add)
                }
            }
        GameObjectShape.WALL_DIAGONALCORNER, GameObjectShape.WALL_SQUARECORNER ->
            when (rotation) {
                0 -> {
                    addCollisionFlag(tile, WALL_NORTH_WEST, add)
                    addCollisionFlag(tile.transform(-1, 1), WALL_SOUTH_EAST, add)
                }
                1 -> {
                    addCollisionFlag(tile, WALL_NORTH_EAST, add)
                    addCollisionFlag(tile.transform(1, 1), WALL_SOUTH_WEST, add)
                }
                2 -> {
                    addCollisionFlag(tile, WALL_SOUTH_EAST, add)
                    addCollisionFlag(tile.transform(1, -1), WALL_NORTH_WEST, add)
                }
                3 -> {
                    addCollisionFlag(tile, WALL_SOUTH_WEST, add)
                    addCollisionFlag(tile.transform(-1, -1), WALL_NORTH_EAST, add)
                }
            }
        GameObjectShape.WALL_L ->
            when (rotation) {
                0 -> {
                    addCollisionFlag(tile, WALL_NORTH or WALL_WEST, add)
                    addCollisionFlag(tile.transform(-1, 0), WALL_EAST, add)
                    addCollisionFlag(tile.transform(0, 1), WALL_SOUTH, add)
                }
                1 -> {
                    addCollisionFlag(tile, WALL_NORTH or WALL_EAST, add)
                    addCollisionFlag(tile.transform(0, 1), WALL_SOUTH, add)
                    addCollisionFlag(tile.transform(1, 0), WALL_WEST, add)
                }
                2 -> {
                    addCollisionFlag(tile, WALL_SOUTH or WALL_EAST, add)
                    addCollisionFlag(tile.transform(1, 0), WALL_WEST, add)
                    addCollisionFlag(tile.transform(0, -1), WALL_NORTH, add)
                }
                3 -> {
                    addCollisionFlag(tile, WALL_SOUTH or WALL_WEST, add)
                    addCollisionFlag(tile.transform(0, -1), WALL_NORTH, add)
                    addCollisionFlag(tile.transform(-1, 0), WALL_EAST, add)
                }
            }
    }
}

private fun CollisionFlagMap.changeFloorDecor(
    coords: Tile,
    add: Boolean,
) = addCollisionFlag(coords, GROUND_DECOR, add)

private fun CollisionFlagMap.addCollisionFlag(
    tile: Tile,
    mask: Int,
    add: Boolean,
) = when {
    add -> add(tile.x, tile.z, tile.height, mask)
    else -> remove(tile.x, tile.z, tile.height, mask)
}

// private fun CollisionFlagMap.addFloorCollision(tile: Tile) = addCollisionFlag(tile, FLOOR, true)
