// instantiated with jti.pl from DataSlotImpl
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;

import com.sphenon.basics.context.*;

public class DataSlotImpl_Long_
    implements DataSlot_Long_ {

    Long data;

    public DataSlotImpl_Long_ (CallContext context) {
    }

    public DataSlotImpl_Long_ (CallContext context, Long data) {
        this.data = data;
    }

    public void set(CallContext context, Long data) {
        this.data = data;
    }

    public void setObject(CallContext context, Object data) {
        set(context, (Long)data);
    }

    public Long get(CallContext context) {
        return this.data;
    }

    public Object getObject(CallContext context) {
        return get(context);
    }
}
