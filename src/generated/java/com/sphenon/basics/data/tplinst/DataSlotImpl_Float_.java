// instantiated with jti.pl from DataSlotImpl
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;

import com.sphenon.basics.context.*;

public class DataSlotImpl_Float_
    implements DataSlot_Float_ {

    Float data;

    public DataSlotImpl_Float_ (CallContext context) {
    }

    public DataSlotImpl_Float_ (CallContext context, Float data) {
        this.data = data;
    }

    public void set(CallContext context, Float data) {
        this.data = data;
    }

    public void setObject(CallContext context, Object data) {
        set(context, (Float)data);
    }

    public Float get(CallContext context) {
        return this.data;
    }

    public Object getObject(CallContext context) {
        return get(context);
    }
}
