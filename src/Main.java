import java.util.Random;

public class Main {
    public static int bossHealth = 1200;
    public static int bossDamage = 80;
    public static String bossDefence;
    public static int[] heroesHealth = {290, 270, 250, 270, 450, 250, 300, 270};
    public static int[] heroesDamage = {20, 15, 10, 0, 5, 10, 0, 15};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Golem", "Lucky", "Witcher", "Thor"};
    public static int roundNumber = 0;
    public static int indexOfMedic = 3;
    public static int indexOfGolem = 4;
    public static int indexOfLucky = 5;
    public static int indexOfWitcher = 6;
    public static boolean isStunned = false;

    public static void main(String[] args) {
        while (!isGameOver()) {
            playRound();
        }
    }

    public static void playRound() {
        roundNumber++;
        System.out.println("ROUND " + roundNumber + " -----------------");
        System.out.println();
        chooseBossDefence();
        printStatistics();
        System.out.println();
        if (!isStunned) {
            bossAttacks();
        }
        else {
            isStunned = false;
            System.out.println("Boss is stunned");
        }
        System.out.println();
        heroesAttack();
        heroesHeal();
        System.out.println();
    }

    public static boolean isGameOver() {
        if (bossHealth <= 0) {
            System.out.println("Boss is defeated");
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("All heroes are dead");
            System.out.println("Boss won!!!");
            return true;
        }
        return false;
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length);
        bossDefence = heroesAttackType[randomIndex];
        if (bossDefence == "Medic" || bossDefence == "Witcher") {
            chooseBossDefence();
        }
    }

    public static void bossAttacks() {
        double absorbedDamage = bossDamage * 0.2;

        for (int i = 0; i < heroesHealth.length; i++) {
            int trueDamage = bossDamage;
            int golemDamage = 0;
            if (heroesHealth[i] > 0) {
                if (i == indexOfLucky) {
                    if (new Random().nextBoolean()) {
                        System.out.println("⨠ Lucky dodged");
                        continue;
                    }
                }
                if (heroesHealth[indexOfGolem] > 0) {
                    if (i != indexOfGolem) {
                        trueDamage -= absorbedDamage;
                        heroesHealth[indexOfGolem] = (int) Math.max(heroesHealth[indexOfGolem] - absorbedDamage, 0);
                        if (heroesHealth[indexOfGolem] <= 0 && heroesHealth[indexOfWitcher] > 0) {
                            heroesHealth[indexOfGolem] = heroesHealth[indexOfWitcher];
                            System.out.println("Witcher gifted " + heroesAttackType[i] + " his own life");
                        }
                    }
                }
                heroesHealth[i] = Math.max((heroesHealth[i] - trueDamage), 0);
                System.out.println("BOSS did " + trueDamage + " damage to " + heroesAttackType[i]);
                if (heroesHealth[i] == 0 && heroesHealth[indexOfWitcher] > 0) {
                    heroesHealth[i] = heroesHealth[indexOfWitcher];
                    heroesHealth[indexOfWitcher] = 0;
                    System.out.println("⩲⩲ Witcher gifted " + heroesAttackType[i] + " his own life");
                }
            } else {
                System.out.println(heroesAttackType[i] + " is dead");
            }
        }
    }


    public static void heroesAttack() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                int damage = heroesDamage[i];
                if (heroesAttackType[i] == "Thor"){
                    if(new Random().nextBoolean()){
                        isStunned = true;
                        if (isStunned){
                            System.out.println("⨂⨂ Thor stunned Boss for the next round");
                        }
                    }
                }
                if (heroesAttackType[i] == bossDefence) {
                    Random random = new Random();
                    int coefficient = random.nextInt(9) + 2; // 2,3,4,5,6,7,8,9,10
                    damage = heroesDamage[i] * coefficient;
                    System.out.println("!!! " + heroesAttackType[i] + " did critical damage: " + damage);
                } else if (heroesAttackType[i] != "Medic" || heroesAttackType[i] != "Witcher") {
                    System.out.println("! " + heroesAttackType[i] + " did damage: " + damage);
                }
                bossHealth = Math.max(bossHealth - damage, 0);
            }

        }
    }

    public static void heroesHeal() {
        if (heroesHealth[indexOfMedic] > 0) {
            for (int i = 0; i < heroesHealth.length; i++) {
                if (heroesHealth[i] > 0 && heroesHealth[i] < 100) {
                    int heal = 40;
                    heroesHealth[i] += heal;
                    System.out.println("↑♥ " + heroesAttackType[i] + " got heal: " + heal + "HP");
                    break;
                }
            }
        }
    }

    public static void printStatistics() {
        System.out.println("BOSS | Health: " + bossHealth + "HP" + " | Damage: " + bossDamage
                + " | Weakness: " + (bossDefence == null ? "No weakness" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " | Health: " + heroesHealth[i] + "HP");
        }
    }
}