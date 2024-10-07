// instantiated with jti.pl from DataSlotImpl
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;
import com.sphenon.basics.retriever.Meter;

import com.sphenon.basics.context.*;

public class DataSlotImpl_Meter_
    implements DataSlot_Meter_ {

    Meter data;

    public DataSlotImpl_Meter_ (CallContext context) {
    }

    public DataSlotImpl_Meter_ (CallContext context, Meter data) {
        this.data = data;
    }

    public void set(CallContext context, Meter data) {
        this.data = data;
    }

    public void setObject(CallContext context, Object data) {
        set(context, (Meter)data);
    }

    public Meter get(CallContext context) {
        return this.data;
    }

    public Object getObject(CallContext context) {
        return get(context);
    }
}
