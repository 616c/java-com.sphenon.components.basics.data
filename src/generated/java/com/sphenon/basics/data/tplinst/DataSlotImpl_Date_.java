// instantiated with jti.pl from DataSlotImpl
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;
import java.util.Date;

import com.sphenon.basics.context.*;

public class DataSlotImpl_Date_
    implements DataSlot_Date_ {

    Date data;

    public DataSlotImpl_Date_ (CallContext context) {
    }

    public DataSlotImpl_Date_ (CallContext context, Date data) {
        this.data = data;
    }

    public void set(CallContext context, Date data) {
        this.data = data;
    }

    public void setObject(CallContext context, Object data) {
        set(context, (Date)data);
    }

    public Date get(CallContext context) {
        return this.data;
    }

    public Object getObject(CallContext context) {
        return get(context);
    }
}
