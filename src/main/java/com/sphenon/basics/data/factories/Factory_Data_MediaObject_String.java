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

public class Factory_Data_MediaObject_String {

    protected String text;

    public String getText (CallContext context) {
        return this.text;
    }

    public void setText (CallContext context, String text) {
        this.text = text;
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

    public Data_MediaObject_String create (CallContext context) {
        return Data_MediaObject_String.create(context, text, type_extension);
    }

}
