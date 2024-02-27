package com.nensi.mirageislands.Commands;

import com.nensi.mirageislands.GUI.IslandsMainGui;
import com.nensi.mirageislands.Islands.InviteManager;
import com.nensi.mirageislands.Islands.IslandManager;
import com.nensi.mirageislands.Islands.PlayerIsland;
import com.nensi.mirageislands.MirageIslands;
import com.nensi.mirageislands.Player.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainCommand implements CommandExecutor, TabExecutor {

    private final MirageIslands plugin = MirageIslands.getPlugin(MirageIslands.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player p) {

            if (p.hasPermission("mirageislands.command.admin")) {
                if (args.length != 0) {

                    if (args[0].equalsIgnoreCase("reload")) {
                        plugin.loadConfigurations();
                        p.sendMessage(MirageIslands.utils.setColors("&aПерезагружено!"));
                        MirageIslands.sounds.clientSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    }

                    if (args[0].equalsIgnoreCase("teleport")) {
                        if (args.length > 1) {
                            String id = args[1];

                            new IslandManager().getPlayerIsland(p, id).Teleport();
                        }
                    }

                    if (args[0].equalsIgnoreCase("add")) {
                        if (args.length > 1) {

                            Player target = Bukkit.getPlayerExact(args[1]);

                            if (target == null) {
                                return true;
                            }

                            if (args.length > 2) {
                                String id = args[2];

                                new PlayerDataManager(target).addIsland(new PlayerIsland(IslandManager.placed_amount, target.getUniqueId().toString(), new IslandManager().getIsland(id)));
                                IslandManager.placed_amount++;
                                new IslandManager().placeIslands();
                                p.sendMessage(MirageIslands.utils.setColors("&aДобавлено!"));
                                MirageIslands.sounds.clientSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                            }

                        }
                    }

                    if (args[0].equalsIgnoreCase("remove")) {
                        if (args.length > 1) {

                            Player target = Bukkit.getPlayerExact(args[1]);

                            if (target == null) {
                                return true;
                            }

                            if (args.length > 2) {
                                String id = args[2];

                                new PlayerDataManager(target).removeIsland(new PlayerIsland(IslandManager.placed_amount, target.getUniqueId().toString(), new IslandManager().getIsland(id)));
                                IslandManager.placed_amount++;
                                new IslandManager().placeIslands();
                                p.sendMessage(MirageIslands.utils.setColors("&aУдалено!"));
                                MirageIslands.sounds.clientSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                            }

                        }
                    }

                    if (args[0].equalsIgnoreCase("invite")) {

                        if (args.length > 1) {

                            String action = args[1];
                            switch (action) {

                                case "send" -> {
                                    if (args.length > 2) {

                                        Player target = Bukkit.getPlayerExact(args[2]);
                                        new InviteManager().invitePlayer(p, target);

                                    }
                                }

                                case "accept" -> new InviteManager().acceptInvitation(p);

                                case "deny" -> new InviteManager().denyInvitation(p);
                            }

                        }

                    }

                    if (args[0].equalsIgnoreCase("expansion")) {
                        if (args.length > 1) {

                            Player target = Bukkit.getPlayerExact(args[1]);

                            if (target == null) {
                                return true;
                            }

                            if (args.length > 2) {
                                String action = args[2];

                                if (action.equalsIgnoreCase("add")) {

                                    if (args.length > 3) {
                                        String island_id = args[3];

                                        if (args.length > 4) {
                                            String exp_id = args[4];

                                            new IslandManager().getPlayerIsland(target, island_id).addExpansion(exp_id);
                                            new IslandManager().placeIslands();
                                            p.sendMessage(MirageIslands.utils.setColors("&aДобавлено расширение!"));
                                            MirageIslands.sounds.clientSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

                                        }

                                    }

                                }

                                else if (action.equalsIgnoreCase("remove")) {

                                    if (args.length > 3) {
                                        String island_id = args[3];

                                        if (args.length > 4) {
                                            String exp_id = args[4];

                                            new IslandManager().getPlayerIsland(target, island_id).removeExpansion(exp_id);
                                            new IslandManager().placeIslands();

                                            p.sendMessage(MirageIslands.utils.setColors("&aРасширенеи удалено!"));
                                            MirageIslands.sounds.clientSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                                        }

                                    }

                                }
                            }

                        }
                    }

                } else {

                    new IslandsMainGui(p).Open(0, false);

                }
            } else {

                if (args.length != 0) {

                    if (args[0].equalsIgnoreCase("invite")) {

                        if (args.length > 1) {

                            String action = args[1];
                            switch (action) {

                                case "invite" -> {
                                    if (args.length > 2) {

                                        Player target = Bukkit.getPlayerExact(args[2]);
                                        new InviteManager().invitePlayer(p, target);

                                    }
                                }

                                case "accept" -> new InviteManager().acceptInvitation(p);

                                case "deny" -> new InviteManager().denyInvitation(p);
                            }

                        }

                    }

                } else {

                    new IslandsMainGui(p).Open(0, false);

                }

                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1.0f, 1.0f);
            }
        }
        return true;
    }

    @SuppressWarnings("all")
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, String[] args) {



        if (sender instanceof Player p) {

            List<String> completions = null;

            if (p.hasPermission("mirageislands.command.admin")) {

                List<String> list = Arrays.asList("reload", "teleport", "add", "remove", "expansion", "invite");

                if (args.length == 1) {
                    String input = args[0].toLowerCase();
                    for (String s : list) {
                        if (s.startsWith(input)) {

                            if (completions == null) {
                                completions = new ArrayList();
                            }
                            completions.add(s);
                        }
                    }
                }

                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("teleport")) {
                        String input = args[1].toLowerCase();
                        for (String s : new IslandManager().getPlayerIslandIds(p)) {
                            if (s.startsWith(input)) {

                                if (completions == null) {
                                    completions = new ArrayList();
                                }
                                completions.add(s);
                            }
                        }
                    }

                    if (args[0].equalsIgnoreCase("invite")) {
                        String input = args[1].toLowerCase();
                        for (String s : List.of("send", "accept", "deny")) {
                            if (s.startsWith(input)) {

                                if (completions == null) {
                                    completions = new ArrayList();
                                }
                                completions.add(s);
                            }
                        }
                    }

                }

                if (args.length == 3) {

                    if (args[0].equalsIgnoreCase("add")) {
                        String input = args[2].toLowerCase();
                        for (String s : new IslandManager().getIslandIds()) {
                            if (s.startsWith(input)) {

                                if (completions == null) {
                                    completions = new ArrayList();
                                }
                                completions.add(s);
                            }
                        }
                    }

                    if (args[0].equalsIgnoreCase("remove")) {
                        String input = args[2].toLowerCase();
                        for (String s : new IslandManager().getPlayerIslandIds(p)) {
                            if (s.startsWith(input)) {

                                if (completions == null) {
                                    completions = new ArrayList();
                                }
                                completions.add(s);
                            }
                        }
                    }

                    if (args[0].equalsIgnoreCase("expansion")) {
                        String input = args[2].toLowerCase();
                        for (String s : List.of("add", "remove")) {
                            if (s.startsWith(input)) {

                                if (completions == null) {
                                    completions = new ArrayList();
                                }
                                completions.add(s);
                            }
                        }
                    }
                }

                if (args.length == 4) {

                    if (args[0].equalsIgnoreCase("expansion")) {

                        if (args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("remove")) {

                            String input = args[3].toLowerCase();
                            for (String s : new IslandManager().getPlayerIslandIds(p)) {
                                if (s.startsWith(input)) {

                                    if (completions == null) {
                                        completions = new ArrayList();
                                    }
                                    completions.add(s);
                                }
                            }

                        }

                    }
                }

                if (args.length == 5) {

                    if (args[0].equalsIgnoreCase("expansion")) {

                        if (args[2].equalsIgnoreCase("add")) {

                            if (new IslandManager().getPlayerIsland(p, args[3]) == null) return new ArrayList<>();

                            String input = args[4].toLowerCase();

                            List<String> exp_list = new IslandManager().getPlayerIsland(p, args[3]).getIsland().getExpansions();
                            exp_list.removeAll(new IslandManager().getPlayerIsland(p, args[3]).getExpansions());

                            for (String s : exp_list) {
                                if (s.startsWith(input)) {

                                    if (completions == null) {
                                        completions = new ArrayList();
                                    }
                                    completions.add(s);
                                }
                            }

                        }

                        if (args[2].equalsIgnoreCase("remove")) {

                            if (new IslandManager().getPlayerIsland(p, args[3]) == null) return new ArrayList<>();

                            String input = args[4].toLowerCase();
                            for (String s : new IslandManager().getPlayerIsland(p, args[3]).getExpansions()) {
                                if (s.startsWith(input)) {

                                    if (completions == null) {
                                        completions = new ArrayList();
                                    }
                                    completions.add(s);
                                }
                            }

                        }

                    }


                }
            } else {
                List<String> list = Arrays.asList("invite");

                if (args.length == 1) {
                    String input = args[0].toLowerCase();
                    for (String s : list) {
                        if (s.startsWith(input)) {

                            if (completions == null) {
                                completions = new ArrayList();
                            }
                            completions.add(s);
                        }
                    }
                }

                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("invite")) {
                        String input = args[1].toLowerCase();
                        for (String s : List.of("send", "accept", "deny")) {
                            if (s.startsWith(input)) {

                                if (completions == null) {
                                    completions = new ArrayList();
                                }
                                completions.add(s);
                            }
                        }
                    }

                }
            }


            if (completions != null) {
                Collections.sort(completions);
            }

            return completions;

        }

        return new ArrayList<>();

    }

}
