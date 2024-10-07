// instantiated with jti.pl from DataSlotImpl
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;

import com.sphenon.basics.context.*;

public class DataSlotImpl_Integer_
    implements DataSlot_Integer_ {

    Integer data;

    public DataSlotImpl_Integer_ (CallContext context) {
    }

    public DataSlotImpl_Integer_ (CallContext context, Integer data) {
        this.data = data;
    }

    public void set(CallContext context, Integer data) {
        this.data = data;
    }

    public void setObject(CallContext context, Object data) {
        set(context, (Integer)data);
    }

    public Integer get(CallContext context) {
        return this.data;
    }

    public Object getObject(CallContext context) {
        return get(context);
    }
}
