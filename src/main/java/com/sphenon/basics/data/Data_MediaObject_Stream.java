package com.sphenon.basics.data;

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
import com.sphenon.basics.exception.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.tplinst.*;
import com.sphenon.basics.variatives.*;
import com.sphenon.basics.variatives.tplinst.*;
import com.sphenon.basics.locating.*;
import com.sphenon.basics.locating.factories.*;
import java.io.*;
import java.util.Date;

public class Data_MediaObject_Stream implements Data_MediaObject {
    private InputStream               stream;
    private Variative_InputStream_    var_stream;
    private Type                      type;
    private TypeImpl_MediaObject      typemo;
    private String                    stringmo;
    private String                    filename;
    private String                    origin_locator;
    private Date                      last_update;

    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }

    static {
        CallContext context = com.sphenon.basics.context.classes.RootContext.getInitialisationContext();
        notification_level = NotificationLocationContext.getLevel(context, "com.sphenon.basics.data.Data_MediaObject_Stream");
    }

    protected TypeImpl_MediaObject getMediaObjectType(CallContext context, Type type, String filename) {
        if (type == null) {
            if (filename != null && filename.matches("^(?:.*/)?[^/]+\\.[^/.]+$")) {
                return (TypeImpl_MediaObject) TypeManager.getMediaType(context, filename.substring(filename.lastIndexOf('.') + 1));
            } else {
                return (TypeImpl_MediaObject) TypeManager.getMediaType(context, "unknown");
            }
        }
        if (type instanceof TypeImpl_MediaObject) {
            return (TypeImpl_MediaObject) type;
        }
        Vector_Type_long_ sts = type.getSuperTypes(context);
        for (long i=0; i<sts.getSize(context); i++) {
            Type st = sts.tryGet(context, i);
            if (st instanceof TypeImpl_MediaObject) {
                return (TypeImpl_MediaObject) st;
            }
        }
        for (long i=0; i<sts.getSize(context); i++) {
            TypeImpl_MediaObject st = getMediaObjectType(context, type, null);
            if (st != null) {
                return st;
            }
        }
        return null;
    }

    protected Data_MediaObject_Stream(CallContext context, InputStream stream, Type type, String filename, String origin_locator, Date last_update) {
        this.stream     = stream;
        this.var_stream = null;
        this.filename   = filename;
        this.type = type;
        this.typemo = getMediaObjectType(context, this.type, null);
        this.stringmo = typemo.getMediaType(context);
        this.origin_locator = origin_locator;
        this.last_update = last_update;
    }

    protected Data_MediaObject_Stream(CallContext context, Variative_InputStream_ stream, Type type, String filename, String origin_locator, Date last_update) {
        this.stream     = null;
        this.var_stream = stream;
        this.filename = filename;
        this.type = type;
        this.typemo = getMediaObjectType(context, this.type, null);
        this.stringmo = typemo.getMediaType(context);
        this.origin_locator = origin_locator;
        this.last_update = last_update;
    }

    protected Data_MediaObject_Stream(CallContext context, Data_MediaObject data) {
        this.stream     = data.getStream(context);
        this.var_stream = null;
        this.filename   = data.getDispositionFilename(context);
        this.type       = null;
        this.typemo     = null;
        this.stringmo   = data.getMediaType(context);
        this.origin_locator = null;
        this.last_update = data.getLastUpdate(context);
    }

    static public Data_MediaObject_Stream create(CallContext context, InputStream stream, String filename) {
        return new Data_MediaObject_Stream(context, stream, null, filename, null, null);
    }

    static public Data_MediaObject_Stream create(CallContext context, InputStream stream, Type type, String filename) {
        return new Data_MediaObject_Stream(context, stream, type, filename, null, null);
    }

    static public Data_MediaObject_Stream create(CallContext context, InputStream stream, Type type, String filename, String origin_locator) {
        return new Data_MediaObject_Stream(context, stream, type, filename, origin_locator, null);
    }

    static public Data_MediaObject_Stream create(CallContext context, Variative_InputStream_ stream, Type type, String filename) {
        return new Data_MediaObject_Stream(context, stream, type, filename, null, null);
    }

    static public Data_MediaObject_Stream create(CallContext context, Variative_InputStream_ stream, Type type, String filename, String origin_locator) {
        return new Data_MediaObject_Stream(context, stream, type, filename, origin_locator, null);
    }

    static public Data_MediaObject_Stream create(CallContext context, Data_MediaObject data) {
        return new Data_MediaObject_Stream(context, data);
    }

    public Type getDataType(CallContext context) {
        return typemo;
    }

    public String getMediaType(CallContext context) {
        return stringmo;
    }

    public String getDispositionFilename(CallContext context) {
        return this.filename;
    }

    public java.io.InputStream getInputStream(CallContext context) {
        return getCurrentStream(context);
    }

    public java.io.InputStream getStream(CallContext context) {
        return this.getInputStream(context);
    }

    public java.io.OutputStream getOutputStream(CallContext context) {
        CustomaryContext.create((Context)context).throwLimitation(context, "Data_MediaObject_Stream is not writable");
        throw (ExceptionLimitation) null; // compilernsists
    }

    public Date getLastUpdate(CallContext context) {
        return this.last_update != null ? this.last_update : new Date();
    }

    protected InputStream getCurrentStream(CallContext context) {
        return this.stream == null ? this.var_stream.getVariant_InputStream_(context) : this.stream;
    }

    public void setStream(CallContext context, InputStream stream) {
        this.stream     = stream;
        this.var_stream = null;
    }

    public void setStream(CallContext context, Variative_InputStream_ stream) {
        this.stream     = null;
        this.var_stream = stream;
    }

    public String toString(CallContext context) {
        try {
            StringWriter sw = new StringWriter();
            InputStream is = getCurrentStream(context);
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            int c;
            while ((c = isr.read ()) != -1) { sw.write (c); }
            sw.close();
            isr.close();
            is.close();
            return sw.toString();
        } catch (IOException ioe) {
            CustomaryContext.create((Context)context).throwEnvironmentFailure(context, ioe, "Upload failed");
            throw (ExceptionEnvironmentFailure) null; // compiler insists
        }
    }

    public Locator tryGetOrigin(CallContext context) {
        return Factory_Locator.tryConstruct(context, this.origin_locator);
    }
    
    protected void finalize () throws Throwable {
        if (this.stream != null) {
            try { this.stream.close(); } catch (Throwable ex) {}
        }
        if (this.var_stream != null) {
            // context?? noch gueltig?
        }
        super.finalize();
    }
    
}
