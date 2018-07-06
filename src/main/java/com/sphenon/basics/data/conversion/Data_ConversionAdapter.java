package com.sphenon.basics.data.conversion;

/****************************************************************************
  Copyright 2001-2018 Sphenon GmbH

  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations
  under the License.
*****************************************************************************/

import com.sphenon.basics.context.*;
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.message.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.locating.*;
import com.sphenon.basics.locating.returncodes.*;

import com.sphenon.basics.data.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.security.*;
import com.sphenon.basics.aspects.*;
import com.sphenon.basics.variatives.*;

import java.io.*;
import java.util.Map;

public class Data_ConversionAdapter implements Data {

    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.data.conversion.Data_ConversionAdapter"); };

    public Data_ConversionAdapter (CallContext context, Data source_data, DataConversion conversion) {
        this.source_data                  = source_data;
        this.conversion                   = conversion;
        this.target_data                  = target_data;
        this.last_conversion              = -1;
    }

    protected Data source_data;

    public Data getSourceData (CallContext context) {
        return this.source_data;
    }

    public void setSourceData (CallContext context, Data source_data) {
        this.source_data = source_data;
        this.target_data = null;
        this.last_conversion = -1;
    }

    protected DataConversion conversion;

    public DataConversion getConversion (CallContext context) {
        return this.conversion;
    }

    public void setConversion (CallContext context, DataConversion conversion) {
        this.conversion = conversion;
        this.target_data = null;
        this.last_conversion = -1;
    }

    protected Data                 target_data;
    protected long                 last_conversion;

    protected synchronized Data convert(CallContext context) {
        long sdlu = this.source_data.getLastUpdate(context).getTime();
        if (this.target_data == null || this.last_conversion == -1 || this.last_conversion < sdlu) {
            this.target_data = conversion.convert(context, source_data);
            this.last_conversion = sdlu;
        }
        return this.target_data;
    }

    public Type getDataType(CallContext context) {
        return convert(context).getDataType(context);
    }

    public java.util.Date getLastUpdate(CallContext context) {
        return source_data.getLastUpdate(context);
    }

    public Locator tryGetOrigin(CallContext context) {
        return source_data.tryGetOrigin(context);
    }

    public Data_MediaObject getDataMediaObject(CallContext context) {
        Data result = convert(context);
        if (result instanceof Data_MediaObject) {
            return ((Data_MediaObject) result);
        } else {
            CustomaryContext.create((Context)context).throwConfigurationError(context, "Result of data conversion '%(conversion)' is not a Data_MediaObject", "conversion", conversion);
            throw (ExceptionConfigurationError) null; // compiler insists
        }
    }

    public Data_Object getDataObject(CallContext context) {
        Data result = convert(context);
        if (result instanceof Data_Object) {
            return ((Data_Object) result);
        } else {
            CustomaryContext.create((Context)context).throwConfigurationError(context, "Result of data conversion '%(conversion)' is not a Data_Object", "conversion", conversion);
            throw (ExceptionConfigurationError) null; // compiler insists
        }
    }
}

