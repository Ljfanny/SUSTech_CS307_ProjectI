package proj.one;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

//STM事务实现类
public final class STMTxn implements Txn{
    //事务ID生成器
    private static AtomicLong txnSeq = new AtomicLong(0);
    private Map<TxnRef,VersionedRef> inTxnMap = new HashMap<>();
    private Map<TxnRef, Object> writeMap = new HashMap<>();
    private long txnId;
    STMTxn(){
        txnId = txnSeq.incrementAndGet();
    }
    @Override
    public <T> T get(TxnRef<T> ref) {
        if (!inTxnMap.containsKey(ref)){
            inTxnMap.put(ref,ref.curRef);
        }
        return (T)inTxnMap.get(ref).value;
    }

    @Override
    public <T> void set(TxnRef<T> ref, T value) {
        if (!inTxnMap.containsKey(ref)){
            inTxnMap.put(ref,ref.curRef);
        }
        writeMap.put(ref,value);
    }
    //提交事务
    boolean commit(){
        synchronized (STM.commitLock){
            boolean isValid = true;
            for(Map.Entry<TxnRef,VersionedRef> entry: inTxnMap.entrySet()){
                VersionedRef curRef = entry.getKey().curRef;
                VersionedRef readRef = entry.getValue();
                if (curRef.version != readRef.version){
                    isValid = false;
                    break;
                }
            }
            if (isValid){
                writeMap.forEach((k,v)->{
                    k.curRef = new VersionedRef(v, txnId);
                });
            }
            return isValid;
        }
    }
}
