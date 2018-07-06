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

public class Data_MediaObject_File implements Data_MediaObject {
    private File                 file;
    private Variative_File_      var_file;
    private Type                 type;
    private TypeImpl_MediaObject typemo;
    private String               filename;

    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }

    static protected boolean ignore_non_existing_files;

    static {
        CallContext context = com.sphenon.basics.context.classes.RootContext.getInitialisationContext();
        notification_level = NotificationLocationContext.getLevel(context, "com.sphenon.basics.data.Data_MediaObject_File");
        ignore_non_existing_files = DataPackageInitialiser.getConfiguration(context).get(context, "IgnoreNonExistingFiles", true);
    }

    protected TypeImpl_MediaObject getMediaObjectType(CallContext context, Type type) {
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
            TypeImpl_MediaObject st = getMediaObjectType(context, type);
            if (st != null) {
                return st;
            }
        }
        return null;
    }

    protected Data_MediaObject_File(CallContext context, File file, Type type) {
        this.file     = file;
        this.var_file = null;
        this.filename = this.file.getName();

        this.type = (type == null ? TypeManager.getMediaType(context, file) : type);
        this.typemo = getMediaObjectType(context, this.type);
        if (this.typemo == null) {
            this.typemo = (TypeImpl_MediaObject) TypeManager.getMediaType(context, file);
        }
    }

    protected Data_MediaObject_File(CallContext context, Variative_File_ file, Type type, String filename) {
        this.file     = null;
        this.var_file = file;
        this.filename = filename;

        this.type = (type == null ? TypeManager.getMediaType(context, file.getVariant_File_(context)) : type);
        this.typemo = getMediaObjectType(context, this.type);
        if (this.typemo == null) {
            this.typemo = (TypeImpl_MediaObject) TypeManager.getMediaType(context, file.getVariant_File_(context));
        }
    }

    static public Data_MediaObject_File create(CallContext context, File file, Type type) {
        return new Data_MediaObject_File(context, file, type);
    }

    static public Data_MediaObject_File create(CallContext context, Variative_File_ file, Type type, String filename) {
        return new Data_MediaObject_File(context, file, type, filename);
    }

    public Type getDataType(CallContext context) {
        return typemo;
    }

    public String getMediaType(CallContext context) {
        return typemo.getMediaType(context);
    }

    public String getDispositionFilename(CallContext context) {
        return this.filename;
    }

    public java.io.InputStream getInputStream(CallContext context) {
        FileInputStream fis = null;
        File file = getCurrentFile(context);
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException fnfe) {
            if (ignore_non_existing_files) {
                if ((notification_level & Notifier.OBSERVATION) != 0) { CustomaryContext.create(Context.create(context)).sendNotice(context, "File '%(file)' for data media object does not exist: '%(reason)'", "file", file.getPath(), "reason", fnfe); }
                return new java.io.ByteArrayInputStream(new byte[0]);
             } else {
                CustomaryContext.create(Context.create(context)).throwConfigurationError(context, fnfe, "Resource (stream/file) '%(file)' not available", "file", file);
                throw (ExceptionConfigurationError) null; // compiler insists
            }
        }
        return new BufferedInputStream(fis);
    }

    public java.io.InputStream getStream(CallContext context) {
        return this.getInputStream(context);
    }

    public java.io.OutputStream getOutputStream(CallContext context) {
        FileOutputStream fos = null;
        File file = getCurrentFile(context);
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException fnfe) {
            CustomaryContext.create(Context.create(context)).throwConfigurationError(context, fnfe, "Resource (stream/file) '%(file)' not writable", "file", file);
            throw (ExceptionConfigurationError) null; // compiler insists
        }
        return new BufferedOutputStream(fos);
    }

    public Date getLastUpdate(CallContext context) {
        File file = getCurrentFile(context);
        return new Date(file.lastModified());
    }

    public File getCurrentFile(CallContext context) {
        return this.file == null ? this.var_file.getVariant_File_(context) : this.file;
    }

    public Locator tryGetOrigin(CallContext context) {
        File cur_file = getCurrentFile(context);
        return Factory_Locator.tryConstruct(context, "ctn://Space/host/file_system/,//Path/" + cur_file.getAbsolutePath());
    }
}
