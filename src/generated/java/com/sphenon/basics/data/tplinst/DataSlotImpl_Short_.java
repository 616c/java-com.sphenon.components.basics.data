// instantiated with jti.pl from DataSlotImpl
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;

import com.sphenon.basics.context.*;

public class DataSlotImpl_Short_
    implements DataSlot_Short_ {

    Short data;

    public DataSlotImpl_Short_ (CallContext context) {
    }

    public DataSlotImpl_Short_ (CallContext context, Short data) {
        this.data = data;
    }

    public void set(CallContext context, Short data) {
        this.data = data;
    }

    public void setObject(CallContext context, Object data) {
        set(context, (Short)data);
    }

    public Short get(CallContext context) {
        return this.data;
    }

    public Object getObject(CallContext context) {
        return get(context);
    }
}
