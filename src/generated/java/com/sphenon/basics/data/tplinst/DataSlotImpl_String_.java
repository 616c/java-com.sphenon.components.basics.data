// instantiated with jti.pl from DataSlotImpl
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;

import com.sphenon.basics.context.*;

public class DataSlotImpl_String_
    implements DataSlot_String_ {

    String data;

    public DataSlotImpl_String_ (CallContext context) {
    }

    public DataSlotImpl_String_ (CallContext context, String data) {
        this.data = data;
    }

    public void set(CallContext context, String data) {
        this.data = data;
    }

    public void setObject(CallContext context, Object data) {
        set(context, (String)data);
    }

    public String get(CallContext context) {
        return this.data;
    }

    public Object getObject(CallContext context) {
        return get(context);
    }
}
