// instantiated with jti.pl from DataSlotImpl
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;
import com.sphenon.basics.retriever.TrafficLight;

import com.sphenon.basics.context.*;

public class DataSlotImpl_TrafficLight_
    implements DataSlot_TrafficLight_ {

    TrafficLight data;

    public DataSlotImpl_TrafficLight_ (CallContext context) {
    }

    public DataSlotImpl_TrafficLight_ (CallContext context, TrafficLight data) {
        this.data = data;
    }

    public void set(CallContext context, TrafficLight data) {
        this.data = data;
    }

    public void setObject(CallContext context, Object data) {
        set(context, (TrafficLight)data);
    }

    public TrafficLight get(CallContext context) {
        return this.data;
    }

    public Object getObject(CallContext context) {
        return get(context);
    }
}
