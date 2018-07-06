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
import java.io.*;
import java.util.Date;

public class Data_MediaObject_CharSequence implements Data_MediaObject {
    private CharSequence text;
    protected ByteArrayOutputStream baos;
    private TypeImpl_MediaObject typemo;
    private String disposition_name;

    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }

    static {
        CallContext context = com.sphenon.basics.context.classes.RootContext.getInitialisationContext();
        notification_level = NotificationLocationContext.getLevel(context, "com.sphenon.basics.data.Data_MediaObject_CharSequence");
    }

    protected Data_MediaObject_CharSequence(CallContext context, CharSequence text, String type_extension) {
        this.typemo = (type_extension == null ? null : (TypeImpl_MediaObject) TypeManager.getMediaType (context, type_extension));
        this.text = text;
        this.disposition_name = "plain.txt";
    }

    static public Data_MediaObject_CharSequence create(CallContext context, CharSequence text, String type_extension) {
        return new Data_MediaObject_CharSequence(context, text, type_extension);
    }

    public String getString(CallContext context) {
        try {
            if( baos != null ){
                this.text = baos.toString("UTF-8");
            }
        } catch (UnsupportedEncodingException uee) {
            CustomaryContext.create(Context.create(context)).throwConfigurationError(context, uee, "Java version does not support utf-8");
            throw (ExceptionConfigurationError) null; // compiler insists
        }
        return this.text.toString();
    }

    public TypeImpl_MediaObject getDataType(CallContext context) {
        return typemo;
    }

    public void setDataType(CallContext context, TypeImpl_MediaObject typemo) {
        this.typemo = typemo;
    }

    public String getMediaType(CallContext context) {
        return typemo.getMediaType(context);
    }

    public String getDispositionFilename(CallContext context) {
        return disposition_name;
    }

    public void setDispositionFilename(CallContext context, String name) {
        this.disposition_name=name;
    }
    
    public java.io.InputStream getInputStream(CallContext context) {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(this.getString(context).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            CustomaryContext.create(Context.create(context)).throwConfigurationError(context, uee, "Java version does not support utf-8");
            throw (ExceptionConfigurationError) null; // compiler insists
        }
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
