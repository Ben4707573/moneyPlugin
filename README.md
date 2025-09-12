# ServerDollars (SD) Plugin

A simple PaperMC plugin for 1.21.x that adds a currency called Server Dollars (SD) with the symbol `$`.

Features:
- Per-player balance storage (persisted to `balances.yml`).
- Admin command `/sd` with `add|set|remove` to manage balances.
- Player command `/pay <player> <amount>` to transfer money.
- Player command `/hidebalance` to hide your balance from others and not see others.
- Shows your own balance in the tab-list header.

## Build

Prereqs: Java 21, Maven.

```bash
mvn -q -e -DskipTests package
```

The jar will be in `target/server-dollars-1.0.0-shaded.jar`. Drop it into your server's `plugins/` folder.

## Commands

- `/sd <add|set|remove> <player> <amount>` (permission: `sd.admin`)
- `/pay <player> <amount>`
- `/hidebalance`

## Notes on privacy

- The plugin only shows your own balance in your tab header.
- The `hidden` toggle prevents any feature from exposing your balance to others in the future and can be expanded later.

## Config

`config.yml`:

```yaml
symbol: '$'
```

## Data files

- `plugins/ServerDollars/balances.yml` stores balances and hidden flags.

## Compatibility

Built against Paper API `${paper.api.version}` (1.21.x). Adjust in `pom.xml` if needed for newer patch versions.