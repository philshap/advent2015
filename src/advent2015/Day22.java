package advent2015;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Day22 extends Day {
  public Day22() {
    super(22);
  }

  int playerHp = 50;
  int playerMana = 500;

  static boolean hardMode = false;

  enum Spell {
    MISSILE(53, 4, 0) {
      @Override
      Combat cast(Combat c) {
        return new Combat(c.playerHp, c.mana - cost, c.manaSpent + cost, c.bossHp - effect, c.bossDamage, c.shield, c.poison, c.recharge, c.turn);
      }
    },
    DRAIN(73, 2, 0) {
      @Override
      Combat cast(Combat c) {
        return new Combat(c.playerHp + effect, c.mana - cost, c.manaSpent + cost, c.bossHp - effect, c.bossDamage, c.shield, c.poison, c.recharge, c.turn);
      }
    },
    SHIELD(113, 7, 6) {
      @Override
      boolean canCast(Combat combat) {
        return super.canCast(combat) && combat.shield == 0;
      }

      @Override
      Combat cast(Combat c) {
        return new Combat(c.playerHp, c.mana - cost, c.manaSpent + cost, c.bossHp, c.bossDamage, duration, c.poison, c.recharge, c.turn);
      }
    },
    POISON(173, 3, 6) {
      @Override
      boolean canCast(Combat combat) {
        return super.canCast(combat) && combat.poison == 0;
      }
      @Override
      Combat cast(Combat c) {
        return new Combat(c.playerHp, c.mana - cost, c.manaSpent + cost, c.bossHp, c.bossDamage, c.shield, duration, c.recharge, c.turn);
      }
    },
    RECHARGE(229, 101, 5) {
      @Override
      boolean canCast(Combat combat) {
        return super.canCast(combat) && combat.recharge == 0;
      }
      @Override
      Combat cast(Combat c) {
        return new Combat(c.playerHp, c.mana - cost, c.manaSpent + cost, c.bossHp, c.bossDamage, c.shield, c.poison, duration, c.turn);
      }
    };
    final int cost;
    final int effect;
    final int duration;

    Spell(int cost, int effect, int duration) {
      this.cost = cost;
      this.effect = effect;
      this.duration = duration;
    }

    boolean canCast(Combat combat) {
      return combat.mana >= cost;
    }

    abstract Combat cast(Combat combat);
  }

  record Combat(int playerHp, int mana, int manaSpent, int bossHp, int bossDamage, int shield, int poison, int recharge, int turn) {
    Combat(int playerHp, int mana, int bossHp, int bossDamage) {
      this(playerHp, mana, 0, bossHp, bossDamage, 0, 0, 0, 0);
    }
    boolean playerWins() {
      return bossHp <= 0;
    }

    List<Combat> next() {
      //  before turn effects
      int newPlayerHp = playerHp;
      if (hardMode) {
        newPlayerHp--;
        if (newPlayerHp <= 0) {
          return List.of();
        }
      }
      int newBossHp = bossHp;
      int newMana = mana;
      int newShield = shield;
      int newPoison = poison;
      int newRecharge = recharge;
      // Before turn effects
      if (newShield != 0) {
        newShield--;
      }
      if (newPoison != 0) {
        newBossHp -= Spell.POISON.effect;
        newPoison--;
      }
      if (newRecharge != 0) {
        newMana += Spell.RECHARGE.effect;
        newRecharge--;
      }
      Combat nc = new Combat(newPlayerHp, newMana, manaSpent, newBossHp, bossDamage, newShield, newPoison, newRecharge, turn + 1);

      if (nc.playerWins()) {
        return List.of(nc);
      }
      if (turn % 2 == 0) {
        // Player turn
        return Arrays.stream(Spell.values())
            .filter(s -> s.canCast(nc))
            .map(s -> s.cast(nc)).toList();
      } else {
        // Boss turn
        newPlayerHp = nc.playerHp - Math.max(1, bossDamage - (nc.shield != 0 ? Spell.SHIELD.effect : 0));
        if (newPlayerHp <= 0) {
          return List.of();
        }
        return List.of(new Combat(newPlayerHp, nc.mana, nc.manaSpent, nc.bossHp, nc.bossDamage, nc.shield, nc.poison, nc.recharge, nc.turn));
      }
    }
  }

  int playerWinsMinimumMana(String data) {
    var ints = Support.integers(data).boxed().toList();
    Combat combat = new Combat(playerHp, playerMana, ints.getFirst(), ints.getLast());
    PriorityQueue<Combat> queue = new PriorityQueue<>(Comparator.comparingInt(Combat::manaSpent));
    queue.add(combat);
    while (!queue.peek().playerWins()) {
      queue.addAll(queue.remove().next());
    }
    return queue.remove().manaSpent;
  }

  @Override
  String part1() {
    return String.valueOf(playerWinsMinimumMana(data));
  }

  @Override
  String part2() {
    hardMode = true;
    return String.valueOf(playerWinsMinimumMana(data));
  }

  static void main() {
    Day22 day = new Day22() {
      @Override
      String getData() {
        return """
            Hit Points: 14
            Damage: 8""";
      }
    };
    day.playerHp = 19;
    day.playerMana = 250;
    day.run("226", "588");
  }
}
