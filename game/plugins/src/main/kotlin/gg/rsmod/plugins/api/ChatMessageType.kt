package gg.rsmod.plugins.api

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class ChatMessageType(val id: Int) {
    GAME_MESSAGE(0),
    MOD_CHAT(1),
    PUBLIC_CHAT(2),
    PRIVATE_CHAT(3),
    ENGINE(4),
    LOGIN_LOGOUT_NOTIFICATION(5),
    PRIVATE_CHAT_OUT(6),
    MOD_PRIVATE_CHAT(7),
    FRIENDS_CHAT(9),
    FRIENDS_CHAT_NOTIFICATION(11),
    BROADCAST(14),
    SNAPSHOT_FEEDBACK(26),
    EXAMINE_ITEM(27), // OBJ_EXAMINE
    EXAMINE_NPC(28), // NPC_EXAMINE
    EXAMINE_OBJECT(29), // LOC_EXAMINE
    FRIEND_NOTIFICATION(30),
    IGNORE_NOTIFICATION(31),
    AUTO_TYPER(90),
    MOD_AUTO_TYPER(91),
    CONSOLE(99),
    TRADE_REQ(101),
    TRADE(102),
    CHALREQ_TRADE(103),
    CHALREQ_FRIENDS_CHAT(104),
    SPAM(105),
    PLAYER_RELATED(106),
    TEN_SEC_TIMEOUT(107),
    FILTERED(109)
}