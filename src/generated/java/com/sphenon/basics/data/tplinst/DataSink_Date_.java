// instantiated with jti.pl from DataSink
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;
import java.util.Date;

import com.sphenon.basics.context.*;

public interface DataSink_Date_ extends com.sphenon.basics.data.DataSink<Date> {
    public void set(CallContext context, Date data);
}
