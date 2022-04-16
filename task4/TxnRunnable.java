package proj.one;

@FunctionalInterface
public interface TxnRunnable {
    void run(Txn txn);
}
