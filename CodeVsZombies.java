import java.util.*;
import java.io.*;
import java.math.*;

class Player {

    static final int WIDTH = 16000;
    static final int HEIGHT = 9000;
    static final int ASH_SPEED = 1000;
    static final int ZOMBIE_SPEED = 400;
    static final int ASH_ATTACK_DISTANCE = 2000;

    public static double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    static class Action {
        int ashX, ashY;
        Map<Integer, int[]> humans = new HashMap<>();
        Map<Integer, int[]> zombies = new HashMap<>();

        public void readSituation(Scanner scanner) {
            ashX = scanner.nextInt();
            ashY = scanner.nextInt();
            readHumans(scanner);
            readZombies(scanner);
        }

        private void readHumans(Scanner scanner) {
            int humanCount = scanner.nextInt();
            humans.clear();
            for (int i = 0; i < humanCount; i++) {
                int humanId = scanner.nextInt();
                int humanX = scanner.nextInt();
                int humanY = scanner.nextInt();
                humans.put(humanId, new int[]{humanX, humanY});
            }
        }

        private void readZombies(Scanner scanner) {
            int zombieCount = scanner.nextInt();
            zombies.clear();
            for (int i = 0; i < zombieCount; i++) {
                int zombieId = scanner.nextInt();
                int zombieX = scanner.nextInt();
                int zombieY = scanner.nextInt();
                int zombieXNext = scanner.nextInt();
                int zombieYNext = scanner.nextInt();
                zombies.put(zombieId, new int[]{zombieX, zombieY, zombieXNext, zombieYNext});
            }
        }

        public void executeStrategicAction() {
            int[] targetZombie = findZombieClosestToAnyHuman();
            if (targetZombie != null) {
                System.out.println(targetZombie[0] + " " + targetZombie[1]);
            } else {
                System.out.println(ashX + " " + ashY); // Stay in place if no zombie found
            }
        }

        private int[] findZombieClosestToAnyHuman() {
            int[] bestTargetZombie = null;
            double bestScore = Double.MAX_VALUE;

            for (int[] human : humans.values()) {
                int[] closestZombie = findClosestZombieToHuman(human);
                if (closestZombie != null) {
                    double distanceToHuman = calculateDistance(closestZombie[0], closestZombie[1], human[0], human[1]);
                    double distanceToAsh = calculateDistance(ashX, ashY, closestZombie[0], closestZombie[1]);
                    double score = distanceToHuman + distanceToAsh;

                    if (canAshSaveHuman(closestZombie, human) && score < bestScore) {
                        bestScore = score;
                        bestTargetZombie = closestZombie;
                    }
                }
            }
            return bestTargetZombie;
        }

        private int[] findClosestZombieToHuman(int[] human) {
            double minDistance = Double.MAX_VALUE;
            int[] closestZombie = null;
            for (int[] zombie : zombies.values()) {
                double distance = calculateDistance(human[0], human[1], zombie[0], zombie[1]);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestZombie = zombie;
                }
            }
            return closestZombie;
        }

        private boolean canAshSaveHuman(int[] zombie, int[] human) {
            double distanceToHuman = calculateDistance(zombie[0], zombie[1], human[0], human[1]);
            double turnsForZombieToReachHuman = distanceToHuman / ZOMBIE_SPEED;
            double turnsForAshToReachZombie = calculateDistance(ashX, ashY, zombie[0], zombie[1]) / ASH_SPEED;
            return turnsForAshToReachZombie < turnsForZombieToReachHuman + (ASH_SPEED / ZOMBIE_SPEED);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Action action = new Action();
        while (true) {
            action.readSituation(scanner);
            action.executeStrategicAction();
        }
    }
}
