package dev.openrune.cache.filestore.definition.data

object ParamMapper {
    object item {
        val STAB_ATTACK_BONUS = 0
        val SLASH_ATTACK_BONUS = 1
        val CRUSH_ATTACK_BONUS = 2
        val MAGIC_ATTACK_BONUS = 3
        val RANGED_ATTACK_BONUS = 4
        val STAB_DEFENCE_BONUS = 5
        val SLASH_DEFENCE_BONUS = 6
        val CRUSH_DEFENCE_BONUS = 7
        val MAGIC_DEFENCE_BONUS = 8
        val RANGED_DEFENCE_BONUS = 9
        val MELEE_STRENGTH = 10
        val PRAYER_BONUS = 11
        val ATTACK_RATE = 14
        val MAGIC_DAMAGE_BONUS_SALAMANDER = 65
        val MAGIC_DAMAGE_STRENGTH = 299 // Should be divided by 10
        val RANGED_STRENGTH_BONUS = 189
        val PRIMARY_SKILL = 434
        val PRIMARY_LEVEL = 436
        val SECONDARY_SKILL = 435
        val SECONDARY_LEVEL = 437
        val TERTIARY_SKILL = 191
        val TERTIARY_LEVEL = 613
        val QUATERNARY_SKILL = 579
        val QUATERNARY_LEVEL = 614
    }
    object npc {
        const val STAB_ATTACK_BONUS = 0
        const val SLASH_ATTACK_BONUS = 1
        const val CRUSH_ATTACK_BONUS = 2
        const val MAGIC_ATTACK_BONUS = 3
        const val RANGED_ATTACK_BONUS = 4
        const val STAB_DEFENCE_BONUS = 5
        const val SLASH_DEFENCE_BONUS = 6
        const val CRUSH_DEFENCE_BONUS = 7
        const val MAGIC_DEFENCE_BONUS = 8
        const val RANGED_DEFENCE_BONUS = 9
        const val MELEE_STRENGTH_BONUS = 10
        const val RANGED_STRENGTH_BONUS = 12
        const val ATTACK_RATE = 14 // Attack rate in ticks
        const val MAGIC_DAMAGE_BONUS = 65
        const val DRACONIC = 190 // 1 = If is draconic
        const val GOLEM = 1178 // 1 = If is Golem
        const val KALPHITE = 1353 // 1 = If is Kalphite
        const val DEATH_DROP = 46 // What to drop on death Bones/Ashes etc.
        const val PRIMARY_SLAYER_CATEGORY = 50

        object SLAYER_CATEGORIES {
            const val MONKEYS = 1
            const val GOBLINS = 2
            const val RATS = 3
            const val SPIDERS = 4
            const val BIRDS = 5
            const val COWS = 6
            const val SCORPIONS = 7
            const val BATS = 8
            const val WOLVES = 9
            const val ZOMBIES = 10
            const val SKELETONS = 11
            const val GHOSTS = 12
            const val BEARS = 13
            const val HILL_GIANTS = 14
            const val ICE_GIANTS_ICE_GIANTS = 15
            const val FIRE_GIANTS = 16
            const val MOSS_GIANTS = 17
            const val TROLLS = 18
            const val ICE_WARRIORS = 19
            const val OGRES = 20
            const val HOBGOBLINS = 21
            const val DOGS = 22
            const val GHOULS = 23
            const val GREEN_DRAGONS = 24
            const val BLUE_DRAGONS = 25
            const val RED_DRAGONS = 26
            const val BLACK_DRAGONS = 27
            const val LESSER_DEMONS = 28
            const val GREATER_DEMONS = 29
            const val BLACK_DEMONS = 30
            const val HELLHOUNDS = 31
            const val SHADOW_WARRIORS = 32
            const val WEREWOLVES = 33
            const val VAMPYRES = 34
            const val DAGANNOTH = 35
            const val TUROTH = 36
            const val CAVE_CRAWLERS = 37
            const val BANSHEES = 38
            const val CRAWLING_HANDS = 39
            const val INFERNAL_MAGES = 40
            const val ABERRANT_SPECTRES = 41
            const val ABYSSAL_DEMONS = 42
            const val BASILISKS = 43
            const val COCKATRICE = 44
            const val KURASK = 45
            const val GARGOYLES = 46
            const val PYREFIENDS = 47
            const val BLOODVELD = 48
            const val DUST_DEVILS = 49
            const val JELLIES = 50
            const val ROCKSLUGS = 51
            const val NECHRYAEL = 52
            const val KALPHITE = 53
            const val EARTH_WARRIORS = 54
            const val OTHERWORLDLY_BEINGS = 55
            const val ELVES = 56
            const val DWARVES = 57
            const val BRONZE_DRAGONS = 58
            const val IRON_DRAGONS = 59
            const val STEEL_DRAGONS = 60
            const val WALL_BEASTS = 61
            const val CAVE_SLIMES = 62
            const val CAVE_BUGS = 63
            const val SHADES = 64
            const val CROCODILES = 65
            const val DARK_BEASTS = 66
            const val MOGRES = 67
            const val LIZARDS = 68
            const val HARPIE_BUG_SWARMS = 70
            const val SEA_SNAKES = 71
            const val SKELETAL_WYVERNS = 72
            const val KILLERWATTS = 73
            const val MUTATED_ZYGOMITES = 74
            const val ICEFIENDS = 75
            const val MINOTAURS = 76
            const val FLESH_CRAWLERS = 77
            const val CATABLEPON = 78
            const val ANKOU = 79
            const val CAVE_HORRORS = 80
            const val JUNGLE_HORRORS = 81
            const val GORAKS_REMOVE = 82
            const val SUQAHS = 83
            const val SCABARITES = 85
            const val TERROR_DOGS = 86
            const val MOLANISK = 87
            const val WATERFIENDS = 88
            const val SPIRITUAL_CREATURES = 89
            const val LIZARDMEN = 90
            const val MAGIC_AXES = 91
            const val CAVE_KRAKEN = 92
            const val MITHRIL_DRAGONS = 93
            const val AVIANSIE = 94
            const val SMOKE_DEVILS = 95
            const val TZHAAR = 96
            const val MAMMOTHS = 99
            const val ROGUES = 100
            const val ENTS = 101
            const val BANDITS = 102
            const val DARK_WARRIORS = 103
            const val LAVA_DRAGONS = 104
            const val FOSSIL_ISLAND_WYVERNS = 106
            const val REVENANTS = 107
            const val ADAMANT_DRAGONS = 108
            const val RUNE_DRAGONS = 109
            const val CHAOS_DRUIDS = 110
            const val WYRMS = 111
            const val DRAKES = 112
            const val HYDRAS = 113
            const val SAND_CRARBS = 118
            const val BLACK_KNIGHTS = 119
            const val PIRATES = 120
            const val SOURHOGS = 121
            const val WARPED_CREATURES = 122
            const val LESSER_NAGUA = 123
        }
    }


}