// instantiated with jti.pl from DataSlotImpl
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;

import com.sphenon.basics.context.*;

public class DataSlotImpl_Character_
    implements DataSlot_Character_ {

    Character data;

    public DataSlotImpl_Character_ (CallContext context) {
    }

    public DataSlotImpl_Character_ (CallContext context, Character data) {
        this.data = data;
    }

    public void set(CallContext context, Character data) {
        this.data = data;
    }

    public void setObject(CallContext context, Object data) {
        set(context, (Character)data);
    }

    public Character get(CallContext context) {
        return this.data;
    }

    public Object getObject(CallContext context) {
        return get(context);
    }
}
