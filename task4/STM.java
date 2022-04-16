package proj.one;
public final class STM {
    private STM() {
    }

    static final Object commitLock = new Object();
    public static void atomic(TxnRunnable action) {
        boolean committed = false;
        while (!committed){
            STMTxn txn = new STMTxn();
            action.run(txn);
            committed = txn.commit();
        }

    }
}

