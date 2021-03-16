package by.makarchuk.multithread.entity;

import by.makarchuk.multithread.action.GenerateID;
import by.makarchuk.multithread.exception.WrongInputParam;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Port {
    private static final int BERTH_CAPACITY = 7;
    private static Port instance = new Port();
    private Queue<Berth> freeBerths = new LinkedList<>();
    private Queue<Berth> busyBirths = new LinkedList<>();
    private final Lock lock = new ReentrantLock(true);
    private final Condition berthIsFree = lock.newCondition();

    public static Port getInstance() {
        return instance;
    }

    private Port() {
        initBerths();
    }

    public void freeBerth(Berth berth){
        try{
            lock.lock();
            freeBerths.offer(berth);
            busyBirths.remove();
            berthIsFree.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public Berth getFreeBerth() throws WrongInputParam {
        lock.lock();
        try {
            while (freeBerths.isEmpty()) {
                berthIsFree.await();
            }
            Berth berth = freeBerths.poll();
            busyBirths.offer(berth);
            return berth;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        throw new WrongInputParam();
    }

    private void initBerths() {
        for (int i = 0; i < BERTH_CAPACITY; i++) {
            int id = GenerateID.getUniqBerthID();
            freeBerths.add(new Berth(id));
        }
    }
}
