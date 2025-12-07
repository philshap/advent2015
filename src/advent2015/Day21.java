package advent2015;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day21 extends Day {
  public Day21() {
    super(21);
  }

  static final String SHOP = """
      Weapons:    Cost  Damage  Armor
      Dagger        8     4       0
      Shortsword   10     5       0
      Warhammer    25     6       0
      Longsword    40     7       0
      Greataxe     74     8       0

      Armor:      Cost  Damage  Armor
      Leather      13     0       1
      Chainmail    31     0       2
      Splintmail   53     0       3
      Bandedmail   75     0       4
      Platemail   102     0       5

      Rings:      Cost  Damage  Armor
      Damage +1    25     1       0
      Damage +2    50     2       0
      Damage +3   100     3       0
      Defense +1   20     0       1
      Defense +2   40     0       2
      Defense +3   80     0       3""";

  int playerHp = 100;

  record Item(int cost, int damage, int armor) {
    Item(List<Integer> data) {
      this(data.get(0), data.get(1), data.get(2));
    }

    static List<Item> readItems(String line) {
      return Support.partition(Support.integers(line).boxed().toList(), 3).map(Item::new).toList();
    }

    static List<Item> readRings(String line) {
      var rings = Support.partition(Support.integers(line).boxed().toList(), 4).map(l -> l.subList(1, 4)).map(Item::new).toList();
      rings = new ArrayList<>(rings);
      // Allow no rings
      rings.add(new Item(0, 0, 0));
      rings.add(new Item(0, 0, 0));
      return rings;
    }
  }

  record Player(int hp, int damage, int armor, int spent) {
    Player(int hp) {
      this(hp, 0, 0, 0);
    }
    Player equip(Item item) {
      return new Player(hp, damage + item.damage, armor + item.armor, spent + item.cost);
    }
    Player equip(List<Item> items) {
      Player player = this;
      for (Item item : items) {
        player = player.equip(item);
      }
      return player;
    }
    Player takeDamage(Player attacker) {
      return new Player(hp - Math.max(1, attacker.damage - armor), damage, armor, spent);
    }
  }

  boolean fight(Player player, Player boss) {
    int turn = 0;
    while (boss.hp > 0 && player.hp > 0) {
      if (turn++ % 2 == 0) {
        boss = boss.takeDamage(player);
      } else {
        player = player.takeDamage(boss);
      }
    }
    return boss.hp <= 0;
  }

  Stream<List<Item>> itemCombos() {
    String[] split = SHOP.split("\n\n");
    List<Item> weapons = Item.readItems(split[0]);
    List<Item> armors = new ArrayList<>(Item.readItems(split[1]));
    // Allow no armor
    armors.add(new Item(0, 0, 0));
    List<Item> rings = Item.readRings(split[2]);
    return Support.cartesianProduct(weapons, armors, rings, rings)
        // Can't use the same ring on both hands
        .filter(items -> items.get(2) != items.get(3));
  }

  @Override
  String part1() {
    var ints = Support.integers(data).boxed().toList();
    Player boss = new Player(ints.get(0), ints.get(1), ints.get(2), 0);
    int cost =
        itemCombos()
            .map(items -> new Player(playerHp).equip(items))
            .filter(player -> fight(player, boss))
            .mapToInt(Player::spent)
            .min().orElseThrow();
    return String.valueOf(cost);
  }

  // 141 too low
  @Override
  String part2() {
    var ints = Support.integers(data).boxed().toList();
    Player boss = new Player(ints.get(0), ints.get(1), ints.get(2), 0);
    int cost = itemCombos()
            .map(items -> new Player(playerHp).equip(items))
            .filter(player -> !fight(player, boss))
            .mapToInt(Player::spent)
            .max().orElseThrow();
    return String.valueOf(cost);
  }

  static void main() {
    Day21 day = new Day21() {
      @Override
      String getData() {
        return """
            Hit Points: 12
            Damage: 7
            Armor: 2""";
      }
    };
    day.playerHp = 8;
    day.run("65", "188");
  }
}
