// instantiated with jti.pl from DataSink
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;
import com.sphenon.basics.retriever.TrafficLight;

import com.sphenon.basics.context.*;

public interface DataSink_TrafficLight_ extends com.sphenon.basics.data.DataSink<TrafficLight> {
    public void set(CallContext context, TrafficLight data);
}
