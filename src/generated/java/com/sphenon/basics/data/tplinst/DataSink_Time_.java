// instantiated with jti.pl from DataSink
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;
import com.sphenon.basics.retriever.Time;

import com.sphenon.basics.context.*;

public interface DataSink_Time_ extends com.sphenon.basics.data.DataSink<Time> {
    public void set(CallContext context, Time data);
}
