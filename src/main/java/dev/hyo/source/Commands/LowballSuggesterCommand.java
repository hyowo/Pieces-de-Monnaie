package dev.hyo.source.Commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.hyo.source.Main;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;
import java.util.List;

public class LowballSuggesterCommand extends CommandBase {
    // TODO: Change this crap to a GUI + profile management system

    @Override
    public String getCommandName() {
        return "lowball";
    }

    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return null;
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("lowball", "/lb");
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args) {
        sender.addChatMessage(new ChatComponentText(ChatFormatting.GREEN + "THIS MOD IS STILL IN DEVELOPMENT. ALWAYS CHECK YOUR TRADE OFFERS!"));
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText(ChatFormatting.AQUA + "/lowball toggle - Toggles the lowball suggester on or off."));
            sender.addChatMessage(new ChatComponentText(ChatFormatting.AQUA + "/lowball set <item/enchant> <0-100> - Sets the deduction percentage for the item or enchantments."));
            return;
        }
        String command = args[0];
        if (command.equalsIgnoreCase("toggle")) {
            Main.lowballSuggestion.toggleEnabled();
            sender.addChatMessage(new ChatComponentText("Toggled Lowball Suggester.\nLowball Suggester is now " + (Main.lowballSuggestion.isEnabled() ? "enabled." : "disabled.")));
            return;
        }
        if (command.equalsIgnoreCase("set")) {
            if (args.length < 3) {
                sender.addChatMessage(new ChatComponentText(ChatFormatting.RED + "Usage: /lowball set <item/enchant> <0-100>"));
                return;
            }
            String type = args[1];
            String percent = args[2];
            try {
                int percentInt = Integer.parseInt(percent);
                if (percentInt < 0 || percentInt > 100) {
                    sender.addChatMessage(new ChatComponentText(ChatFormatting.RED + "Percentage must be between 0 and 100."));
                    return;
                }
            } catch (NumberFormatException e){
                sender.addChatMessage(new ChatComponentText(ChatFormatting.RED + "Percentage must be an integer."));
                return;
            }
            if (type.equalsIgnoreCase("item")) {
                Main.lowballSuggestion.SetItemPercent(Integer.parseInt(percent));
                sender.addChatMessage(new ChatComponentText("Set item percentage to " + percent + "%."));
                return;
            }
            if (type.equalsIgnoreCase("enchant")) {
                Main.lowballSuggestion.SetEnchantPercent(Integer.parseInt(percent));
                sender.addChatMessage(new ChatComponentText("Set enchant percentage to " + percent + "%."));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(final ICommandSender sender) {
        return true;
    }
}
