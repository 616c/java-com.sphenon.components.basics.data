// instantiated with jti.pl from DataSlotImpl
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;

import com.sphenon.basics.context.*;

public class DataSlotImpl_Byte_
    implements DataSlot_Byte_ {

    Byte data;

    public DataSlotImpl_Byte_ (CallContext context) {
    }

    public DataSlotImpl_Byte_ (CallContext context, Byte data) {
        this.data = data;
    }

    public void set(CallContext context, Byte data) {
        this.data = data;
    }

    public void setObject(CallContext context, Object data) {
        set(context, (Byte)data);
    }

    public Byte get(CallContext context) {
        return this.data;
    }

    public Object getObject(CallContext context) {
        return get(context);
    }
}
