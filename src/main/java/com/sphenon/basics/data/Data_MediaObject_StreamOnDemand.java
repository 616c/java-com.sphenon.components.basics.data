package com.sphenon.basics.data;

/****************************************************************************
  Copyright 2001-2024 Sphenon GmbH

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
import com.sphenon.basics.exception.*;
import com.sphenon.basics.message.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.system.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.tplinst.*;
import com.sphenon.basics.data.*;
import com.sphenon.basics.operations.*;
import com.sphenon.basics.operations.classes.*;
import com.sphenon.basics.operations.factories.*;
import com.sphenon.basics.variatives.*;
import com.sphenon.basics.variatives.tplinst.*;
import com.sphenon.basics.locating.*;
import com.sphenon.basics.locating.factories.*;
import java.io.*;
import java.util.Date;

abstract public class Data_MediaObject_StreamOnDemand implements Data_MediaObject {
    static final public Class _class = Data_MediaObject_StreamOnDemand.class;

    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(_class); };

    private   TypeImpl_MediaObject    typemo;
    private   String                  stringmo;
    private   String                  filename;
    protected String                  encoding;
    protected boolean                 repeatable;

    protected Worker                  worker;

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

    public Data_MediaObject_StreamOnDemand(CallContext context) {
    }

    public Type    getType                (CallContext context) { return TypeManager.getMediaTypeRoot(context); }
    public String  getDispositionFilename (CallContext context) { return null; }
    public String  getInfo                (CallContext context) { return "stream producer"; }
    public Date    getLastUpdate          (CallContext context) { return new Date(); }
    public Locator tryGetOrigin           (CallContext context) { return null; /* Factory_Locator.tryConstruct(context, this.origin_locator); */ }

    public String getEncoding(CallContext context) {
        return this.encoding;
    }

    public void setEncoding(CallContext context, String encoding) {
        this.encoding = encoding;
    }

    public boolean getRepeatable(CallContext context) {
        return this.repeatable;
    }

    public void setRepeatable(CallContext context, boolean repeatable) {
        this.repeatable = repeatable;
    }

    abstract public void produceData (CallContext context, OutputStream os);

    public Type getDataType(CallContext context) {
        if (this.typemo == null) {
            Type type     = this.getType(context);
            this.filename = this.getDispositionFilename(context);
            this.typemo   = this.getMediaObjectType(context, type, this.filename);
        }
        return this.typemo;
    }

    public String getMediaType(CallContext context) {
        if (this.stringmo == null) {
            this.getDataType(context);
            this.stringmo = this.typemo.getMediaType(context);
        }
        return this.stringmo;
    }

    public java.io.InputStream getInputStream(CallContext context) {
        if (this.worker != null && this.getRepeatable(context)) {
            this.close(context);
        }
        if (this.worker == null) {
            this.worker = new Worker(context, this.getInfo(context), false);
        }
        return this.worker.getInputStream(context);
    }

    public java.io.InputStream getStream(CallContext context) {
        return this.getInputStream(context);
    }

    public java.io.OutputStream getOutputStream(CallContext context) {
        CustomaryContext.create((Context)context).throwLimitation(context, "Data_MediaObject_StreamOnDemand is not writable");
        throw (ExceptionLimitation) null; // compilernsists
    }

    public String toString(CallContext context) {
        try {
            StringWriter      sw  = new StringWriter();
            InputStream       is  = this.getInputStream(context);
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
    
    protected void close(CallContext context) {
        if (this.worker != null) {
            this.worker.close(context);
            this.worker = null;
        }
    }

    protected void finalize () throws Throwable {
        this.close(RootContext.getDestructionContext());
        super.finalize();
    }

    protected class Worker extends PipedStreamWorker {
        public Worker(CallContext context, String info, boolean synchronous) {
            super(context, synchronous);
            this.start(context, "[" + getInfo(context) + "]");
        }

        public void work(CallContext context, Execution_BasicSequence es) {
            try {
                produceData(context, this.getOutputStream(context));
                es.addExecution(context, Factory_Execution.createExecutionSuccess(context, "Production of stream data [" + getInfo(context) + "] succeeded"));
            } catch (Throwable throwable) {
                es.addExecution(context, Factory_Execution.createExecutionFailure(context, throwable, "Could not produce stream data [" + getInfo(context) + "]"));
            }
        }
    }
}
