// instantiated with jti.pl from DataSink
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;
import com.sphenon.basics.retriever.Meter;

import com.sphenon.basics.context.*;

public interface DataSink_Meter_ extends com.sphenon.basics.data.DataSink<Meter> {
    public void set(CallContext context, Meter data);
}
