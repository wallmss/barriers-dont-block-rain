package com.example.examplemod.commands;

import com.example.examplemod.BdbrConfig;
import com.example.examplemod.WeatherState;
import com.example.examplemod.gui.GuiBdbr;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;
import java.util.List;

public class CommandBdbr extends CommandBase {

    private static final List<String> SUBCOMMANDS = Arrays.asList(
            "on", "off", "all", "status", "rain", "snow", "thunder", "clear", "vanilla",
            "rainbow", "mix", "solid", "credits"
    );

    @Override
    public String getCommandName() { return "bdbr"; }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bdbr [menu|on|off|all|status|rain|snow|thunder|clear|vanilla|rainbow|mix|solid|credits]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) { openMenuWithDelay(); return; }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "menu": openMenuWithDelay(); return;
            case "on":
                WeatherState.setBarrierBypass(true);
                msg(sender, "§a§l\u2714 §r§aBarrier: §fON"); break;
            case "off":
                WeatherState.setBarrierBypass(false);
                msg(sender, "§c§l\u2718 §r§cBarrier: §fOFF"); break;
            case "all":
                WeatherState.setWeatherThroughBlocks(!WeatherState.weatherThroughBlocks);
                msg(sender, (WeatherState.weatherThroughBlocks ? "§a§l\u2714 " : "§c§l\u2718 ")
                        + "§fThrough blocks: §r" + (WeatherState.weatherThroughBlocks ? "§a§lON" : "§c§lOFF"));
                break;
            case "status": printStatus(sender); break;
            case "rain":    WeatherState.setWeather(WeatherState.ClientWeather.RAIN);    msg(sender, "§b§l\u2602 §r§bWeather: §fRAIN");    break;
            case "snow":    WeatherState.setWeather(WeatherState.ClientWeather.SNOW);    msg(sender, "§f§l\u2744 §r§fWeather: §fSNOW");    break;
            case "thunder": WeatherState.setWeather(WeatherState.ClientWeather.THUNDER); msg(sender, "§e§l\u26A1 §r§eWeather: §fTHUNDER"); break;
            case "clear":   WeatherState.setWeather(WeatherState.ClientWeather.CLEAR);   msg(sender, "§6§l\u2600 §r§6Weather: §fCLEAR");   break;
            case "vanilla": WeatherState.setWeather(WeatherState.ClientWeather.VANILLA); msg(sender, "§7§l\u27F2 §r§7Weather: §fVANILLA"); break;
            case "rainbow":
                WeatherState.setColorMode(WeatherState.ColorMode.RAINBOW);
                msg(sender, "§d§l\uD83C\uDF08 §r§dColor mode: §fRAINBOW"); break;
            case "mix":
                WeatherState.setColorMode(WeatherState.ColorMode.MIX);
                msg(sender, "§d§l\u2728 §r§dColor mode: §fMIX"); break;
            case "solid":
                WeatherState.setColorMode(WeatherState.ColorMode.SOLID);
                msg(sender, "§7§l\u25CF §r§7Color mode: §fSOLID"); break;
            case "credits":
                msg(sender, "§6§lBDBR §r§7— §fBarriers Don't Block Rain");
                msg(sender, "§7Made by §b§lsx");
                msg(sender, "§7GitHub: §fhttps://github.com/wallmss");
                break;
            default:
                error(sender, "Unknown command: §f" + sub);
                hint(sender);
                return;
        }

        BdbrConfig.save();
    }

    private static void openMenuWithDelay() {
        final Minecraft mc = Minecraft.getMinecraft();
        new Thread(() -> {
            try { Thread.sleep(100); }
            catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
            mc.addScheduledTask(() -> mc.displayGuiScreen(new GuiBdbr()));
        }).start();
    }

    private void printStatus(ICommandSender sender) {
        String bypass  = WeatherState.barrierBypass        ? "§a§lON" : "§c§lOFF";
        String through = WeatherState.weatherThroughBlocks ? "§a§lON" : "§c§lOFF";
        String weather;
        switch (WeatherState.weather) {
            case RAIN:    weather = "§b§lRAIN";    break;
            case SNOW:    weather = "§f§lSNOW";    break;
            case THUNDER: weather = "§e§lTHUNDER"; break;
            case CLEAR:   weather = "§6§lCLEAR";   break;
            default:      weather = "§7§lVANILLA"; break;
        }
        String mode = "§7" + WeatherState.getColorMode().name();
        msg(sender, "§6§lBDBR §r§8\u00BB §7Barrier: " + bypass
                + "  §8|  §7Blocks: " + through
                + "  §8|  §7Weather: §r" + weather
                + "  §8|  §7Mode: §r" + mode
                + "  §8|  §7Opacity: §f" + Math.round(WeatherState.rainStrength * 100) + "%"
                + "  §8|  §7Speed: §f" + String.format("%.2fx", WeatherState.colorSpeed));
    }

    private static void msg(ICommandSender sender, String text) {
        sender.addChatMessage(new ChatComponentText(text));
    }
    private static void error(ICommandSender sender, String text) {
        sender.addChatMessage(new ChatComponentText("§4§l\u2718 ERROR §r§4" + text));
    }
    private static void hint(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText(
                "§7Usage: §f/bdbr [menu|on|off|all|status|rain|snow|thunder|clear|vanilla|rainbow|mix|solid|credits]"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            String[] all = new String[SUBCOMMANDS.size() + 1];
            all[0] = "menu";
            for (int i = 0; i < SUBCOMMANDS.size(); i++) all[i + 1] = SUBCOMMANDS.get(i);
            return getListOfStringsMatchingLastWord(args, all);
        }
        return null;
    }

    @Override
    public int getRequiredPermissionLevel() { return 0; }
}