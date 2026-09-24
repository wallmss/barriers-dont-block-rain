package com.example.examplemod.commands;

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
            "on", "off", "status", "rain", "snow", "thunder", "clear", "vanilla", "credito"
    );

    @Override
    public String getCommandName() {
        return "bdbr";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bdbr [menu|on|off|status|rain|snow|thunder|clear|vanilla|credito]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        // Sem argumentos: abre o menu (agendado com um pequeno atraso)
        if (args.length == 0) {
            openMenuWithDelay();
            return;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "menu":
                openMenuWithDelay();
                return;
            case "on":
                WeatherState.setBarrierBypass(true);
                msg(sender, "§a§l✔ §r§aBarreira: §fON");
                return;
            case "off":
                WeatherState.setBarrierBypass(false);
                msg(sender, "§c§l✘ §r§cBarreira: §fOFF");
                return;
            case "status":
                printStatus(sender);
                return;
            case "rain":
                WeatherState.setWeather(WeatherState.ClientWeather.RAIN);
                msg(sender, "§b§l☂ §r§bClima: §fCHUVA");
                return;
            case "snow":
                WeatherState.setWeather(WeatherState.ClientWeather.SNOW);
                msg(sender, "§f§l❄ §r§fClima: §fNEVE");
                return;
            case "thunder":
                WeatherState.setWeather(WeatherState.ClientWeather.THUNDER);
                msg(sender, "§e§l⚡ §r§eClima: §fTROVOADA");
                return;
            case "clear":
                WeatherState.setWeather(WeatherState.ClientWeather.CLEAR);
                msg(sender, "§6§l☀ §r§6Clima: §fLIMPO");
                return;
            case "vanilla":
                WeatherState.setWeather(WeatherState.ClientWeather.VANILLA);
                msg(sender, "§7§l⟲ §r§7Clima: §fVANILLA");
                return;
            case "credito":
                msg(sender, "§6§lBDBR §r§7— §fBarriers Don't Block Rain");
                msg(sender, "§7Feito por §b§lsx");
                msg(sender, "§7GitHub: §fhttps://github.com/wallmss");
                return;
            default:
                error(sender, "Comando inválido: §f" + sub);
                hint(sender);
        }
    }

    /**
     * Abre o GUI com um pequeno atraso para garantir que o chat fechou.
     * Esta é a solução robusta para o problema de GUI abrindo e fechando.
     */
    private static void openMenuWithDelay() {
        final Minecraft mc = Minecraft.getMinecraft();
        new Thread(() -> {
            try {
                // Espera 100 milissegundos (mais que suficiente para o chat fechar)
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            // Agenda para rodar na thread principal do Minecraft
            mc.addScheduledTask(() -> mc.displayGuiScreen(new GuiBdbr()));
        }).start();
    }

    // ---- Daqui pra baixo, mantenha EXATAMENTE o que você já tinha ----

    private void printStatus(ICommandSender sender) {
        String bypass = WeatherState.barrierBypass ? "§a§lON" : "§c§lOFF";
        String weather;
        switch (WeatherState.weather) {
            case RAIN:    weather = "§b§lCHUVA";    break;
            case SNOW:    weather = "§f§lNEVE";     break;
            case THUNDER: weather = "§e§lTROVOADA"; break;
            case CLEAR:   weather = "§6§lLIMPO";    break;
            default:      weather = "§7§lVANILLA";  break;
        }
        String color = WeatherState.hasParticleColor()
                ? "§f#" + WeatherState.getParticleColorHex()
                : "§7default";

        msg(sender, "§6§lBDBR §r§8» §7Barreira: " + bypass
                + "  §8|  §7Clima: §r" + weather
                + "  §8|  §7Cor: §r" + color);
    }

    private static void msg(ICommandSender sender, String text) {
        sender.addChatMessage(new ChatComponentText(text));
    }

    private static void error(ICommandSender sender, String text) {
        sender.addChatMessage(new ChatComponentText("§4§l✘ ERRO §r§4" + text));
    }

    private static void hint(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText(
                "§7Uso: §f/bdbr [menu|on|off|status|rain|snow|thunder|clear|vanilla|credito]"));
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
    public int getRequiredPermissionLevel() {
        return 0;
    }
}