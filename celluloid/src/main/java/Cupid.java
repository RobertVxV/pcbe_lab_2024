import java.util.ArrayList;
import java.util.List;

class Cupid extends Thread {
    private final List<SexualCell> waitingCells = new ArrayList<>();

    public void registerCell(SexualCell cell) {
        synchronized (waitingCells) {
            waitingCells.add(cell);
            waitingCells.notifyAll(); // notify any waiting cells
        }
    }

    public void run() {
        while (true) {
            synchronized (waitingCells) {
                // if there are enough cells to pair
                while (waitingCells.size() < 2) {
                    try {
                        waitingCells.wait(); // wait for cells to register
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                // take a pair of cells to reproduce
                SexualCell cell1 = waitingCells.removeFirst();
                SexualCell cell2 = waitingCells.removeFirst();
                cell1.makeChild(cell2);
            }
        }
    }
}
