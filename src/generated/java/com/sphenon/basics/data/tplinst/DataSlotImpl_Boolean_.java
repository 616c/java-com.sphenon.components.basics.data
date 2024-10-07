// instantiated with jti.pl from DataSlotImpl
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;

import com.sphenon.basics.context.*;

public class DataSlotImpl_Boolean_
    implements DataSlot_Boolean_ {

    Boolean data;

    public DataSlotImpl_Boolean_ (CallContext context) {
    }

    public DataSlotImpl_Boolean_ (CallContext context, Boolean data) {
        this.data = data;
    }

    public void set(CallContext context, Boolean data) {
        this.data = data;
    }

    public void setObject(CallContext context, Object data) {
        set(context, (Boolean)data);
    }

    public Boolean get(CallContext context) {
        return this.data;
    }

    public Object getObject(CallContext context) {
        return get(context);
    }
}
