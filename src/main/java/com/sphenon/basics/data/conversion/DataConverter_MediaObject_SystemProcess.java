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
import com.sphenon.basics.message.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.customary.*;

import com.sphenon.basics.data.*;
import com.sphenon.basics.metadata.*;

import java.util.Map;

public class DataConverter_MediaObject_SystemProcess implements DataConverter {

    protected String               id;
    protected String               conversion_command;
    protected TypeImpl_MediaObject target_type;
    protected TypeImpl_MediaObject source_type;
    protected String               filename_substitution_regexp;
    protected String               filename_substitution_subst;

    public DataConverter_MediaObject_SystemProcess (CallContext context, String id, TypeImpl_MediaObject source_type, TypeImpl_MediaObject target_type, String conversion_command, String filename_substitution_regexp, String filename_substitution_subst) {
        this.id = id;
        this.conversion_command = conversion_command;
        this.target_type = target_type;
        this.source_type = source_type;
        this.filename_substitution_regexp = filename_substitution_regexp;
        this.filename_substitution_subst = filename_substitution_subst;
    }

    public String getId (CallContext context) {
        return this.id;
    }

    public Type getSourceType (CallContext context) {
        return this.source_type;
    }

    public Type getTargetType (CallContext context) {
        return this.target_type;
    }

    public Data convert (CallContext call_context, Data source) {
        return convert (call_context, source, null);
    }

    public Data convert (CallContext call_context, Data source, Map arguments) {
        Context context = Context.create(call_context);
        CustomaryContext cc = CustomaryContext.create(context);
        if (! source.getDataType(context).isA(context, this.source_type) || ! (source.getDataType(context) instanceof TypeImpl_MediaObject)) {
            cc.throwPreConditionViolation(context, "Source of data converter is not a '%(expected)' (TypeImpl_MediaObject), but a '%(got)' ('%(gottype)')", "expected", this.source_type.getName(context), "got", source.getDataType(context).getName(context), "gottype", source.getDataType(context).getClass().getName());
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }
        if (! (source instanceof Data_MediaObject)) {
            cc.throwPreConditionViolation(context, "Source of data converter is not a 'Data_MediaObject', but a '%(got)'", "got", source.getClass().getName());
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }

        return new Data_MediaObject_ConversionAdapter_SystemProcess (context, (Data_MediaObject) source, this.conversion_command, this.target_type, this.filename_substitution_regexp, this.filename_substitution_subst, arguments);
    }
}
