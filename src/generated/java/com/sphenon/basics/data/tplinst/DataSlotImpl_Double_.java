// instantiated with jti.pl from DataSlotImpl
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;

import com.sphenon.basics.context.*;

public class DataSlotImpl_Double_
    implements DataSlot_Double_ {

    Double data;

    public DataSlotImpl_Double_ (CallContext context) {
    }

    public DataSlotImpl_Double_ (CallContext context, Double data) {
        this.data = data;
    }

    public void set(CallContext context, Double data) {
        this.data = data;
    }

    public void setObject(CallContext context, Object data) {
        set(context, (Double)data);
    }

    public Double get(CallContext context) {
        return this.data;
    }

    public Object getObject(CallContext context) {
        return get(context);
    }
}
