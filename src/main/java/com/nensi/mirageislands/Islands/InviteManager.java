package com.nensi.mirageislands.Islands;

import com.nensi.mirageislands.MirageIslands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class InviteManager {

    private static final HashMap<UUID, Long> invite_cd = new HashMap<>();

    private final static HashMap<UUID, PlayerIsland> invited = new HashMap<>();

    public boolean isInvited(Player p) {
        return invited.containsKey(p.getUniqueId());
    }

    public PlayerIsland getInvited(Player p) {
        return invited.getOrDefault(p.getUniqueId(), null);
    }

    public void setInvited(Player p, PlayerIsland island) {
        invited.put(p.getUniqueId(), island);
    }

    public void clearInvited(Player p) {
        invited.remove(p.getUniqueId());
    }

    @SuppressWarnings("deprecation")
    public void invitePlayer(Player p, Player target) {

        if (p == null) return;

        IslandManager islandManager = new IslandManager();

        if (!islandManager.isOnIsland(p)) {
            p.sendMessage(MirageIslands.utils.setColors("&cВы должны быть на острове, чтобы пригласить кого-то!"));
            MirageIslands.sounds.clientSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1.0f, 1.0f);
            return;
        }

        if (invite_cd.getOrDefault(p, 0L) > System.currentTimeMillis()) {
            p.sendMessage(MirageIslands.utils.setColors("&cВы уже отправили приглашение! Подождите немного."));
            MirageIslands.sounds.clientSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1.0f, 1.0f);
            return;
        }

        if (target == null || !target.isOnline()) {
            p.sendMessage(MirageIslands.utils.setColors("&cНа сервере нет такого игрока!"));
            MirageIslands.sounds.clientSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1.0f, 1.0f);
            return;
        }

        if (target.getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString())) {
            p.sendMessage(MirageIslands.utils.setColors("&cНельзя отправить приглашение себе!"));
            MirageIslands.sounds.clientSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1.0f, 1.0f);
            return;
        }

        if (islandManager.isOnIsland(target) && islandManager.getCurrentIsland(target).getIndex() == islandManager.getCurrentIsland(p).getIndex()) {
            p.sendMessage(MirageIslands.utils.setColors("&cЭтот игрок уже у вас на острове!"));
            MirageIslands.sounds.clientSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1.0f, 1.0f);
            return;
        }

        int cd = 10;
        invite_cd.put(p.getUniqueId(), 1000L * cd);

        setInvited(target, islandManager.getCurrentIsland(p));

        //Target Part
        {
            target.sendMessage(" ");
            target.sendMessage(MirageIslands.utils.setColors("&b%SENDER_NAME% &aПригласил вас на свой остров.".replaceAll("%SENDER_NAME%", p.getName())));

            MiniMessage message = MiniMessage.miniMessage();

            Component accept_hover = message.deserialize("<white>Нажмите, чтобы подтвердить");
            Component accept_component = message.deserialize("<color:#66ff66>Accept").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mirageislands invite accept")).
                    hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, accept_hover));

            Component deny_hover = message.deserialize("<white>Нажмите, чтобы отменить");
            Component deny_component = message.deserialize("<color:#ff6666>Deny").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mirageislands invite deny"));

            Component component = message.deserialize("                       ").append(accept_component).append(message.deserialize("   <gray>|   ")).append(deny_component).
                    hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, deny_hover));

            target.sendMessage(" ");

            target.sendMessage(component);

            target.sendMessage(" ");

            target.sendTitle(MirageIslands.utils.setColors("&aПриглашение получено!"), MirageIslands.utils.setColors("&fВас приглсили на остров!"), 10, 60, 15);

            MirageIslands.sounds.clientSound(target, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            MirageIslands.sounds.clientSound(target, Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.0f);
            MirageIslands.sounds.clientSound(target, Sound.BLOCK_BELL_USE, 1.0f, 1.0f);
        }

        //Sender Part
        {
            p.sendMessage(" ");
            p.sendMessage(MirageIslands.utils.setColors("&aВы отправили приглашенеи на остров игроку &b%RECEIVER_NAME%.".replaceAll("%RECEIVER_NAME%", target.getName())));
            p.sendMessage(MirageIslands.utils.setColors("&fЫВы сможете отправить след. приглашение через &e%COOLDOWN% &fсекунд.".replaceAll("%COOLDOWN%", String.valueOf(cd))));
            p.sendMessage(" ");
            p.sendMessage(" ");

            p.sendTitle(MirageIslands.utils.setColors("&aПриглашение отправлено!"), MirageIslands.utils.setColors("&fПодождите пока игрок его примет..."), 10, 60, 15);

            MirageIslands.sounds.clientSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            MirageIslands.sounds.clientSound(p, Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.0f);
            MirageIslands.sounds.clientSound(p, Sound.ENTITY_ENDER_EYE_LAUNCH, 1.0f, 1.0f);
        }

    }


    public void acceptInvitation(Player p) {

        if (isInvited(p)) {

            p.sendMessage(" ");
            p.sendMessage(MirageIslands.utils.setColors("&aВы приняли приглашенеи на остров!"));
            p.sendMessage(" ");
            p.sendMessage(" ");
            MirageIslands.sounds.clientSound(p, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.0f, 1.0f);

            PlayerIsland playerIsland = getInvited(p);

            playerIsland.Teleport(p);

            clearInvited(p);

        } else {
            p.sendMessage(" ");
            p.sendMessage(MirageIslands.utils.setColors("&cУ вас нет приглашений на остров!"));
            p.sendMessage(" ");
            MirageIslands.sounds.clientSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1.0f, 1.0f);
        }
    }



    public void denyInvitation(Player p) {

        if (isInvited(p)) {

            p.sendMessage(" ");
            p.sendMessage(MirageIslands.utils.setColors("&aВы отклонили приглашение на остров!"));
            p.sendMessage(" ");
            p.sendMessage(" ");
            MirageIslands.sounds.clientSound(p, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 1.0f);
            MirageIslands.sounds.clientSound(p, Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.0f);
            MirageIslands.sounds.clientSound(p, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);


            clearInvited(p);

        } else {
            p.sendMessage(" ");
            p.sendMessage(MirageIslands.utils.setColors("&cУ вас нет приглашений в гильдию!"));
            p.sendMessage(" ");
            MirageIslands.sounds.clientSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1.0f, 1.0f);
        }
    }

}
