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

public class Data_MediaObject_ConversionAdapter extends Data_ConversionAdapter implements Data_MediaObject {

    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.data.conversion.Data_MediaObject_ConversionAdapter"); };

    public Data_MediaObject_ConversionAdapter (CallContext context, Data source_data, DataConversion conversion) {
        super(context, source_data, conversion);
    }

    public String getMediaType(CallContext context) {
        return getDataMediaObject(context).getMediaType(context);
    }

    public String getDispositionFilename(CallContext context) {
        return getDataMediaObject(context).getDispositionFilename(context);
    }

    public InputStream getInputStream(CallContext context) {
        return getDataMediaObject(context).getInputStream(context);
    }

    public InputStream getStream(CallContext context) {
        return getDataMediaObject(context).getInputStream(context);
    }

    public OutputStream getOutputStream(CallContext context) {
        CustomaryContext.create((Context)context).throwLimitation(context, "Data_MediaObject_ConversionAdapter is not writable");
        throw (ExceptionLimitation) null; // compilernsists
    }
}

