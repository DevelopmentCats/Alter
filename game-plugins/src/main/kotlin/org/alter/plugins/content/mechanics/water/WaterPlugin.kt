package org.alter.plugins.content.mechanics.water

import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*

class WaterPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        
    }
    //@TODO Replace to RSCM
///**
// * Handle the filling of each [WaterContainers]' [Item] for each of the [WaterSources]
// */
//WaterSources.values().forEach { source ->
//    source.waterObjIds.forEach { obj ->
//        WaterContainers.values().forEach {
//            onItemOnObj(obj = obj, item = it.container.unfilled) {
//                val message =
//                    if (it.container.unfilled.getItemName().contains("Cup")) {
//                        "You fill the cup."
//                    } else {
//                        source.message.replaceItemName(it.container.unfilled, world.definitions)
//                    }
//                it.container.fill(player, message)
//            }
//        }
//    }
//}
//
///**
// * Handle the emptying of each [WaterContainers]' [Item] except for Watering can
// * and Waterskin as they DO NOT have "Empty" option, only "Drop" or "Use".
// */
//WaterContainers.values().filter { it != WaterContainers.CAN && it != WaterContainers.WATERSKIN }.forEach {
//    onItemOption(item = it.container.filled, option = "Empty") {
//        it.container.empty(player)
//    }
//}
//
//WaterContainers.values().forEach {
//    /**
//     * Using [WaterContainer] on another one does nothing,
//     * you cannot transfer water around like pots, you must fill them.
//     */
//    onItemOnItem(it.container.filled, it.container.filled) {
//        player.nothingMessage()
//    }
//    onItemOnItem(it.container.unfilled, it.container.filled) {
//        player.nothingMessage()
//    }
//
//    /**
//     * Toy sink item!
//     */
//    onItemOnItem(Items.SINK, it.container.unfilled) {
//        it.container.fill(player, "The cute sink fills the ${it.container.unfilled.getItemName(lowercase = true)} to the brim.")
//    }
//    onItemOnItem(Items.SINK, it.container.filled) {
//        player.message("The ${it.container.unfilled.getItemName(lowercase = true)} cannot hold any more water.")
//    }
//}
//
///**
// * sexy little drop hack creates a toy sink object one tile north
// * of player and queues it for removal after 300 cycles (~3minutes)
// * also prevents from dropping item from inventory, but could be done here
// */
//canDropItem(Items.SINK) {
//    val obj = DynamicObject(Objs.TOY_SINK, 10, 3, player.tile.transform(0, 1))
//    world.spawn(obj)
//    player.world.queue {
//        wait(300)
//        world.remove(obj)
//    }
//    false
//}
//
///**
// * hot water is apparently only created in bowls lol, registering bowls to heat
// * here would require knowing all the fire sources so we'll ignore lack of ability
// * to make for now; this is mostly used for testing Guthix rest teas without heat plugins.
// */
//onItemOnItem(Items.BOWL_OF_HOT_WATER, Items.EMPTY_CUP) {
//    if (player.comboItemReplace(
//            oldItem = Items.EMPTY_CUP,
//            newItem = Items.CUP_OF_HOT_WATER,
//            otherOld = Items.BOWL_OF_HOT_WATER,
//            otherNew = Items.BOWL,
//            slotAware = true,
//        )
//    ) {
//        player.message("You pour the hot water into the tea cup.")
//    }
//}

}
