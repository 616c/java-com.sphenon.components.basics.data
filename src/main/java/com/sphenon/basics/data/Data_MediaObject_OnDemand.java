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

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.sphenon.basics.context.CallContext;
import com.sphenon.basics.metadata.MIMEType;

// [review: MIMEType code below (complex/redundant) (author: b.)]
// - in Data_MediaObject_File: do not calculate values in constructor, but ondemand
// - possibly all the overriding stuff here can be eliminated then
// - specifically see below ([extensionmarker]) - looks redundant
//   since TypeManager provides this (at least it should)


public abstract class Data_MediaObject_OnDemand extends Data_MediaObject_File {
    private String         media_type;
    String                 disposition_name;
    protected File         generated_file;

    public Data_MediaObject_OnDemand(CallContext ctx, File generated_file, String disposition_name, String media_type) {
        super(ctx, generated_file, null);
        this.generated_file = generated_file;
        this.disposition_name = disposition_name != null ? disposition_name : super.getDispositionFilename(ctx);
        this.media_type = null;
        getMediaType(ctx);
    }
  

    public String getDispositionFilename(CallContext ctx) {
        return disposition_name;
    }

    public Date getLastUpdate(CallContext ctx) {
        return new Date();
    }

    public String getMediaType(CallContext ctx) {
        if (media_type == null) {
            // [extensionmarker] s.o.
            String filename = generated_file.getName();
            int pos = filename.lastIndexOf('.');
            if (pos != -1) {
                String extension = filename.substring(pos+1);
                MIMEType mt = MIMEType.getMIMEType(ctx, extension);
                if (mt != null) {
                    this.media_type = mt.getMIME(ctx);
                }
            }
            if (this.media_type == null) {
                this.media_type = super.getMediaType(ctx);
            }
        }
        return media_type;
    }
    
    protected CallContext input_stream_context;
    
    public java.io.InputStream getStream(CallContext context) {
        return this.getInputStream(context);
    }

    abstract public java.io.OutputStream getOutputStream(CallContext context);

    public File getCurrentFile(CallContext ctx) {
        generateFileOnce(ctx, generated_file);
        return super.getCurrentFile(ctx);
    }

    public void close(CallContext ctx) {
    }

    private boolean generation_has_been_run = false;

    public void generateFileOnce(CallContext ctx, File generated_file) {
        if (!generated_file.exists() || !generation_has_been_run) {
            generateFile(ctx, generated_file);
            generation_has_been_run = true;
        }
    }
  
    abstract protected void generateFile(CallContext ctx, File generated_file);
}
