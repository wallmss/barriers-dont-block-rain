# ☂ Barriers Don't Block Rain (BDBR)

A lightweight **client-side** Forge mod for **Minecraft 1.8.9** that gives you
full control over rain and snow rendering — including making weather fall
**through barrier blocks**.

Built for PvP boxing edits and cinematic content, where arenas are often fully
enclosed with barrier walls and ceilings, and vanilla weather simply doesn't
render inside.

---

## ✨ Features

| Feature | Description |
|---|---|
| ☂ **Weather override** | Force Clear / Rain / Snow / Thunder, client-side only |
| 🎨 **Custom rain/snow color** | Solid, Rainbow (HSV fade), or Mix (cycle between presets) |
| 🎚 **Opacity slider** | Control how visible the falling streaks are |
| ⏩ **Color speed slider** | Adjust the speed of the rainbow/mix cycle |
| 🧱 **Barrier bypass** | Rain/snow falls through barrier blocks — perfect for enclosed PvP arenas |
| 🌍 **Blocks bypass** | Optional mode that makes weather fall through *any* block |
| 🖼 **Live preview** | See the current color/mode update in real time inside the menu |
| 💾 **Persistent config** | All settings save automatically to `config/bdbr.json` |

---

## 🎮 Usage

Open the in-game menu with:
/bdbr

Everything is done through the GUI — no need to remember commands. But if you
prefer the chat, all of these work too:

| Command | Description |
|---|---|
| `/bdbr` | Open the configuration menu |
| `/bdbr on` / `off` | Enable/disable barrier bypass |
| `/bdbr all` | Toggle "blocks bypass" (weather through ANY block) |
| `/bdbr rain` | Force rain (client-side) |
| `/bdbr snow` | Force snow (client-side) |
| `/bdbr thunder` | Force thunderstorm (client-side) |
| `/bdbr clear` | Force clear weather |
| `/bdbr vanilla` | Return to server weather |
| `/bdbr rainbow` | Switch color mode to Rainbow |
| `/bdbr mix` | Switch color mode to Mix |
| `/bdbr solid` | Switch color mode to Solid |
| `/bdbr status` | Show current state |
| `/bdbr credits` | Show credits |

### Color modes

- **Solid** — a single color for rain/snow streaks
- **Rainbow** — smooth HSV cycle through every hue
- **Mix** — smooth transition between any number of chosen preset colors
  (click a preset while in Mix mode to add/remove it from the cycle)

All modes respect the **Color Speed** slider.

---

## 📦 Installation

1. Install **Minecraft Forge 1.8.9** ([download](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.8.9.html))
2. Drop the `.jar` into your `mods/` folder
3. Launch the game

> **Client-side only.** Works in singleplayer and on multiplayer servers
> (Lunar, MinemenClub, Hypixel, etc.) without any server-side installation.

---

## 🔧 How it works

BDBR is built entirely with **SpongePowered Mixin** and modifies only the
client renderer. No server packets, no gameplay changes.

Core hooks used:

| Hook | Purpose |
|---|---|
| `EntityRenderer#renderRainSnow` | Rain/snow streak color + opacity |
| `World#getPrecipitationHeight` | Barrier bypass |
| `World#getRainStrength` / `getThunderStrength` | Weather override |
| `EntityFX#renderParticle` | Particle splash color |

---

## 📸 Screenshots / Showcase

> Coming soon.

---

## 👤 Author

Made by **sx** (Wallace Senna)
- 🎮 IGN: `wsenna`
- 🐙 GitHub: [@wallmss](https://github.com/wallmss)
- 💬 Discord: [`445779629542866944`](https://discord.com/users/445779629542866944)

---

## 📄 License

This project is licensed under the **MIT License** — see [LICENSE](LICENSE)
for details.

---

## ⭐ Credits

Inspired by Lunar Client's Apollo `ModWeatherChanger` and `ModParticleChanger`
modules — and built to solve a gap those modules don't cover: making
weather actually visible inside barrier-walled PvP arenas.