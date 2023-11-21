    import java.util.HashMap;
    import java.util.Map;
    import java.util.Scanner;

    class Player {

        static final int WIDTH = 16000;
        static final int HEIGHT = 9000;
        static final int ASH_SPEED = 1000;
        static final int ZOMBIE_SPEED = 400;
        static final int ASH_ATTACK_DISTANCE = 2000;
        static final int ZOMBIE_ATTACK_DISTANCE = 400;

        public static double dist(int x1, int y1, int x2, int y2) {
            return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        }

        static class Action {
            int x, y;
            Map<Integer, int[]> humans = new HashMap<>();
            Map<Integer, int[]> zombies = new HashMap<>();
            Map<Integer, Integer> zombieAttackHuman = new HashMap<>();

            public void readSituation(Scanner scanner) {
                x = scanner.nextInt();
                y = scanner.nextInt();
                int humanCount = scanner.nextInt();
                humans.clear();
                for (int i = 0; i < humanCount; i++) {
                    int humanId = scanner.nextInt();
                    int humanX = scanner.nextInt();
                    int humanY = scanner.nextInt();
                    humans.put(humanId, new int[]{humanX, humanY});
                }
                zombies.clear();
                int zombieCount = scanner.nextInt();
                for (int i = 0; i < zombieCount; i++) {
                    int zombieId = scanner.nextInt();
                    int zombieX = scanner.nextInt();
                    int zombieY = scanner.nextInt();
                    int zombieXNext = scanner.nextInt();
                    int zombieYNext = scanner.nextInt();
                    zombies.put(zombieId, new int[]{zombieX, zombieY, zombieXNext, zombieYNext});
                }
                zombieAttackHuman.clear();
                for (Map.Entry<Integer, int[]> humanEntry : humans.entrySet()) {
                    for (Map.Entry<Integer, int[]> zombieEntry : zombies.entrySet()) {
                        int[] human = humanEntry.getValue();
                        int[] zomb = zombieEntry.getValue();
                        if ((zomb[2] > human[0] - 600 || zomb[2] < human[0] + 600) && 
                            (zomb[3] > human[1] - 600 || zomb[3] < human[1] + 600)) {
                            zombieAttackHuman.put(zombieEntry.getKey(), humanEntry.getKey());
                        }
                    }
                }
            }

            public void executeStrategicAction() {
                int[] zomb = getClosest();
                if (zomb != null) {
                    System.out.println(zomb[0] + " " + zomb[1]);
                }
            }

            public int[] getClosest() {
                double distanceToHuman = WIDTH;
                double distanceToHero = WIDTH;
                int[] closestToHuman = null;
                int[] closestToHero = null;
                for (Map.Entry<Integer, int[]> zombieEntry : zombies.entrySet()) {
                    int[] human = humans.get(zombieAttackHuman.get(zombieEntry.getKey()));
                    int[] zomb = zombieEntry.getValue();
                    double dis = dist(zomb[0], zomb[1], human[0], human[1]);
                    double heroDistance = dist(x, y, human[0], human[1]);
                    if (distanceToHuman > dis && (heroDistance - ASH_ATTACK_DISTANCE) / ASH_SPEED < 
                        (dis + ZOMBIE_ATTACK_DISTANCE) / ZOMBIE_SPEED) {
                        distanceToHuman = dis;
                        closestToHuman = zomb;
                    }
                    if (distanceToHero > heroDistance) {
                        distanceToHero = heroDistance;
                        closestToHero = zomb;
                    }
                }
                if (closestToHuman != null) {
                    return closestToHuman;
                } else {
                    return closestToHero;
                }
            }
        }

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            Action act = new Action();
            while (true) {
                act.readSituation(scanner);
                act.executeStrategicAction();
            }
        }
    }
