# Zombie Shooter – Top-Down Shooter in Java Swing

A wave-based top-down shooter built with **Java Swing** in **March 2025** as the term project for **BIL 211 (Object-Oriented Programming)** at TOBB ETÜ. The player survives increasingly difficult waves of zombies, unlocking new weapons as the waves progress.

## Features

- **Wave system:** each wave spawns more zombies (`5 + 2 × wave`), and new zombie types join as difficulty rises.
- **5 weapons with distinct mechanics:**

  | Weapon | Clip | Fire rate (rpm) | Mechanic |
  |---|---|---|---|
  | Pistol | 12 | 120 | Straight shot, unlimited reserve |
  | Rifle | 30 | 600 | Random ±15° spread |
  | Shotgun | 5 | 60 | 9 pellets spread evenly over a 45° arc |
  | Sniper | 5 | 30 | Piercing bullets |
  | Rocket Launcher | 1 | 10 | Explosive rounds |

- **4 zombie types:** Normal, Crawler (leaps at the player when close), Tank (slow, high health, hits hard), Spitter.
- **Pause menu** with resume, save, load and exit.
- **Save / load** through Java object serialization (`FileIO`, `GameState`).
- Reload with ammo management, fire-rate limits per weapon, HUD showing weapon, ammo, health and score.

## OOP design

- `Weapon` is an **abstract class**; each weapon overrides `fire(...)` and returns a list of `Bullet`s, so shooting logic stays polymorphic (`GamePanel` doesn't know which weapon it's firing).
- `Zombie` is an **abstract class**; subclasses override `update(Player)` for their own movement behaviour and `checkCollisionWithPlayer` for their damage.
- `GameState` + `FileIO` separate persistence from game logic.

```
Weapon (abstract)          Zombie (abstract)
├── Pistol                 ├── NormalZombie
├── Rifle                  ├── CrawlerZombie
├── Shotgun                ├── TankZombie
├── Sniper                 └── SpitterZombie
└── RocketLauncher
```

## How to run

Requires JDK 17+. Run from the project root so the `images/` folder is found:

```bash
javac -d out src/*.java
java -cp out Game
```

## Controls

| Key | Action |
|---|---|
| W / A / S / D | Move |
| Mouse click | Shoot |
| R | Reload |
| Esc | Pause menu |
| F5 / F9 | Quick save / quick load |

## Bug fixes (November 2026)

Before publishing, I went back to the original code and fixed the bugs that broke the game, without changing its structure:

- Save didn't work because `Zombie` and `Player` weren't `Serializable`; images are now `transient` and reloaded after loading.
- The player kept moving after a key was released, and keyboard input stopped working after clicking a menu button (focus issue).
- Zombies dealt damage every frame, so a Tank killed the player almost instantly; added a short damage cooldown.
- A bullet removed for leaving the screen was still checked for collisions in the same loop iteration.
- The Save button was hidden in the pause menu (inverted condition).
- Aligned a few details with the assignment spec: start with the Pistol, health and score in the top-right corner, piercing Sniper bullets, Tank/Spitter health values.

## Known limitations

- Rocket explosions and the Spitter's acid attack are flagged in the code but not implemented.
- Ammo drops from zombies are not implemented.
- The game loop runs on a `java.util.Timer` thread instead of `javax.swing.Timer`.
