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
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.locating.*;
import com.sphenon.basics.system.*;

import java.io.*;
import java.nio.CharBuffer;

public class DataUtilities {

    static public void copy(CallContext context, Data_MediaObject data, OutputStream os) {
        try {
            InputStream is = data.getStream(context);
            int sum = 0;

            byte[] buf = new byte[4096];
            int bread;
            while ((bread = is.read(buf, 0, 4096)) != -1) {
                os.write(buf, 0, bread);
                sum += bread;
            }

            is.close();

            System.err.println("copied " + sum + " bytes");
        } catch (IOException ioe) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, ioe, "Copying of bytes from MediaObject failed");
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }
    }

    static public Appendable copy(CallContext context, Data_MediaObject data, Appendable appendable) {
        try {
            InputStream is = data == null ? null : data.getStream(context);
            if (is == null) {
                CustomaryContext.create((Context)context).throwPreConditionViolation(context, "Copying of bytes from MediaObject failed, no source provided");
                throw (ExceptionPreConditionViolation) null; // compiler insists
            }

            InputStreamReader isr = new InputStreamReader(is);
            int sum = 0;

            if (appendable == null) {
                appendable = new StringBuilder();
            }

            char[] buf = new char[4096];
            CharBuffer cbuf = CharBuffer.wrap(buf);
            int cread;
            while ((cread = isr.read(buf, 0, 4096)) != -1) {
                appendable.append(cbuf, 0, cread);
                sum += cread;
            }

            isr.close();
            is.close();

        } catch (IOException ioe) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, ioe, "Copying of bytes from MediaObject failed");
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }
        return appendable;
    }

    static public byte[] extractBytes(CallContext context, Data_MediaObject data) {
        try {
            System.err.println("getting bytes from media object...");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            copy(context, data, baos);
            baos.close();

            byte[] bytes = baos.toByteArray();
            System.err.println("returning " + bytes.length + " bytes");
            return bytes;
        } catch (IOException ioe) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, ioe, "Extraction of bytes from MediaObject failed");
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }
    }

    static public void save(CallContext context, Data_MediaObject data, File file) {
        SystemCommandUtilities.ensureParentFolderExists(context, file);
        try {
            System.err.println("saving media object to file...");
            FileOutputStream fos = new FileOutputStream(file);
            copy(context, data, fos);
            fos.close();
        } catch (IOException ioe) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, ioe, "Saving MediaObject to file failed");
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }
    }
}
