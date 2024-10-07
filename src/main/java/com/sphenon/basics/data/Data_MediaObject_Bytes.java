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
import com.sphenon.basics.exception.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.tplinst.*;
import com.sphenon.basics.variatives.*;
import com.sphenon.basics.variatives.tplinst.*;
import com.sphenon.basics.locating.*;
import java.io.*;
import java.util.Date;

public class Data_MediaObject_Bytes implements Data_MediaObject {
    protected byte [] bytes;
    protected ByteArrayOutputStream baos;
    protected TypeImpl_MediaObject typemo;
    protected String disposition_name;
    protected String encoding;

    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }

    static {
        CallContext context = com.sphenon.basics.context.classes.RootContext.getInitialisationContext();
        notification_level = NotificationLocationContext.getLevel(context, "com.sphenon.basics.data.Data_MediaObject_String");
    }

    protected Data_MediaObject_Bytes(CallContext context, byte [] bytes, String type_extension) {
        // System.err.println("created media object with " + (bytes != null ? bytes.length : 0) + " bytes");
        this.typemo = (type_extension == null ? null : (TypeImpl_MediaObject) TypeManager.getMediaType (context, type_extension));
        this.bytes = bytes;
        this.disposition_name = "plain.txt";
    }

    protected Data_MediaObject_Bytes(CallContext context, byte[] bytes) {
        this.bytes = bytes;
    }

    static public Data_MediaObject_Bytes create(CallContext context, byte[] bytes, String type_extension) {
        return new Data_MediaObject_Bytes(context, bytes, type_extension);
    }

    static public Data_MediaObject_Bytes create(CallContext context, byte[] bytes) {
        return new Data_MediaObject_Bytes(context, bytes);
    }

    public byte [] getBytes(CallContext context) {
        if (this.baos != null) {
            this.bytes = baos.toByteArray();
        }
        return this.bytes;
    }

    public Type getDataType(CallContext context) {
        return typemo;
    }

    public void setDataType(CallContext context, TypeImpl_MediaObject data_type) {
        this.typemo = data_type;
    }

    public String getMediaType(CallContext context) {
        return typemo.getMediaType(context);
    }

    public String getDispositionFilename(CallContext context) {
        return disposition_name;
    }

    public void setDispositionFilename(CallContext context, String name) {
        this.disposition_name = name;
    }

    public String getEncoding(CallContext context) {
        return this.encoding;
    }

    public void setEncoding(CallContext context, String encoding) {
        this.encoding = encoding;
    }

    public java.io.InputStream getInputStream(CallContext context) {
        ByteArrayInputStream bais = new ByteArrayInputStream(this.getBytes(context));
        return new BufferedInputStream(bais);
    }

    public java.io.InputStream getStream(CallContext context) {
        return this.getInputStream(context);
    }

    public java.io.OutputStream getOutputStream(CallContext context) {
        this.baos = new ByteArrayOutputStream();
        return new BufferedOutputStream(baos);
    }

    public Date getLastUpdate(CallContext context) {
        return new Date();
    }

    public Locator tryGetOrigin(CallContext context) {
        return null;
    }
}
