/*
 * Copyright (C) 2023 ScreamingSandals
 *
 * This file is part of Screaming BedWars.
 *
 * Screaming BedWars is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Screaming BedWars is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Screaming BedWars. If not, see <https://www.gnu.org/licenses/>.
 */

package org.screamingsandals.bedwars.commands.admin;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import org.screamingsandals.bedwars.lang.LangKeys;
import org.screamingsandals.lib.lang.Message;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.sender.CommandSender;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class Pos1Command extends BaseAdminSubCommand {
    public Pos1Command() {
        super("pos1");
    }

    @Override
    public void construct(CommandManager<CommandSender> manager, Command.Builder<CommandSender> commandSenderWrapperBuilder) {
        manager.command(
                commandSenderWrapperBuilder
                        .handler(commandContext -> editMode(commandContext, (sender, game) -> {
                            var loc = sender.as(Player.class).getLocation();

                            if (game.getWorld() == null) {
                                game.setWorld(loc.getWorld());
                            }
                            if (!game.getWorld().equals(loc.getWorld())) {
                                sender.sendMessage(Message.of(LangKeys.ADMIN_ARENA_EDIT_ERRORS_MUST_BE_IN_SAME_WORLD).defaultPrefix());
                                return;
                            }
                            if (game.getPos2() != null) {
                                if (Math.abs(game.getPos2().getBlockY() - loc.getBlockY()) <= 5) {
                                    sender.sendMessage(Message.of(LangKeys.ADMIN_ARENA_EDIT_ERRORS_INVALID_BOUNDS).defaultPrefix());
                                    return;
                                }
                            }
                            game.setPos1(loc);
                            sender.sendMessage(
                                    Message
                                    .of(LangKeys.ADMIN_ARENA_EDIT_SUCCESS_POS1_SET)
                                    .defaultPrefix()
                                    .placeholder("arena", game.getName())
                                    .placeholder("x", loc.getBlockX())
                                    .placeholder("y", loc.getBlockY())
                                    .placeholder("z", loc.getBlockZ())
                            );
                        }))
        );
    }
}