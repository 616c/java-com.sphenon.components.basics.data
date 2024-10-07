// instantiated with jti.pl from DataSlotImpl
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;
import com.sphenon.basics.retriever.Time;

import com.sphenon.basics.context.*;

public class DataSlotImpl_Time_
    implements DataSlot_Time_ {

    Time data;

    public DataSlotImpl_Time_ (CallContext context) {
    }

    public DataSlotImpl_Time_ (CallContext context, Time data) {
        this.data = data;
    }

    public void set(CallContext context, Time data) {
        this.data = data;
    }

    public void setObject(CallContext context, Object data) {
        set(context, (Time)data);
    }

    public Time get(CallContext context) {
        return this.data;
    }

    public Object getObject(CallContext context) {
        return get(context);
    }
}
