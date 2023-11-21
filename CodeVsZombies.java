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
            int[] targetZombie = findZombieOptimallyPositioned();
            if (targetZombie != null) {
                System.out.println(targetZombie[0] + " " + targetZombie[1]);
            } else {
                System.out.println(ashX + " " + ashY);
            }
        }

        private int[] findZombieOptimallyPositioned() {
            double closestDistance = Double.MAX_VALUE;
            int[] optimallyPositionedZombie = null;
            for (Map.Entry<Integer, int[]> zombieEntry : zombies.entrySet()) {
                int[] zombie = zombieEntry.getValue();
                int[] closestHuman = findClosestHumanToZombie(zombie);
                double distanceToHuman = calculateDistance(zombie[0], zombie[1], closestHuman[0], closestHuman[1]);
                double distanceToAsh = calculateDistance(ashX, ashY, zombie[0], zombie[1]);
                double combinedDistance = distanceToHuman + distanceToAsh;
                if (combinedDistance < closestDistance) {
                    closestDistance = combinedDistance;
                    optimallyPositionedZombie = zombie;
                }
            }
            return optimallyPositionedZombie;
        }

        private int[] findClosestHumanToZombie(int[] zombie) {
            double minDistance = Double.MAX_VALUE;
            int[] closestHuman = null;
            for (int[] human : humans.values()) {
                double distance = calculateDistance(zombie[0], zombie[1], human[0], human[1]);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestHuman = human;
                }
            }
            return closestHuman;
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
