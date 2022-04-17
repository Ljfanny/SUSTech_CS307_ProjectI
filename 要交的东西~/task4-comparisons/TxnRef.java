package proj.one;

//支持事务引用
public class TxnRef<T> {
    volatile VersionedRef curRef;
    public TxnRef(T value){
        this.curRef = new VersionedRef(value, 0L);
    }
    public T getValue(Txn txn){
        return txn.get(this);
    }
    public void setValue(T value, Txn txn){
        txn.set(this, value);
    }
}
