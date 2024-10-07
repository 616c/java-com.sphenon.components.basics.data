package com.sphenon.basics.data.factories;

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

import com.sphenon.basics.data.*;

public class Factory_Data_MediaObject_Bytes {

    protected String hex_data;

    public String getHexData(CallContext context) {
        return this.hex_data;
    }

    public void setHexData(CallContext context, String hex_data) {
        this.hex_data = hex_data;
    }

    protected String type_extension;

    public String getTypeExtension (CallContext context) {
        return this.type_extension;
    }

    public String defaultTypeExtension (CallContext context) {
        return "txt";
    }

    public void setTypeExtension (CallContext context, String type_extension) {
        this.type_extension = type_extension;
    }

    public Data_MediaObject_Bytes create (CallContext context) {
        byte[] bytes = convertToBytes(context, this.hex_data);
        return Data_MediaObject_Bytes.create(context, bytes, type_extension);
    }

    static protected byte[] convertToBytes(CallContext context, String hex_string) {
        int size = hex_string.length() / 2;
        byte[] bytes = new byte[size];
        hex_string = hex_string.toUpperCase();
        for (int i=0, j=0; i < size; i++, j+=2) {
            char c1 = hex_string.charAt(j);
            char c2 = hex_string.charAt(j+1);
            char c = (char) ((c1 - (c1 > 64 ? 55 : 48)) * 16 + (c2 - (c2 > 64 ? 55 : 48)));
            bytes[i] = (byte) c;
        }
        return bytes;
    }
}
