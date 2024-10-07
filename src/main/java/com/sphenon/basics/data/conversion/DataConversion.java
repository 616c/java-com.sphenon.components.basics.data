package com.sphenon.basics.data.conversion;

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
import com.sphenon.basics.message.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.system.*;
import com.sphenon.basics.data.*;
import com.sphenon.basics.metadata.*;

import java.util.Map;
import java.util.List;

public class DataConversion implements ContextAware {

    public DataConversion(CallContext context) {
    }

    protected DataConverter converter;

    public DataConverter getConverter (CallContext context) {
        return this.converter;
    }

    public DataConverter defaultConverter (CallContext context) {
        return null;
    }

    public void setConverter (CallContext context, DataConverter converter) {
        this.converter = converter;
    }

    protected String converter_id;

    public String getConverterId (CallContext context) {
        return this.converter_id;
    }

    public String defaultConverterId (CallContext context) {
        return null;
    }

    public void setConverterId (CallContext context, String converter_id) {
        this.converter_id = converter_id;
    }

    protected Type target_type;

    public Type getTargetType (CallContext context) {
        return this.target_type;
    }

    public Type defaultTargetType (CallContext context) {
        return null;
    }

    public void setTargetType (CallContext context, Type target_type) {
        this.target_type = target_type;
    }

    protected Map arguments;

    public Map getArguments (CallContext context) {
        return this.arguments;
    }

    public Map defaultArguments (CallContext context) {
        return null;
    }

    public void setArguments (CallContext context, Map arguments) {
        this.arguments = arguments;
    }

    protected List<DataConversion> conversions;

    public List<DataConversion> getConversions (CallContext context) {
        return this.conversions;
    }

    public List<DataConversion> defaultConversions (CallContext context) {
        return null;
    }

    public void setConversions (CallContext context, List<DataConversion> conversions) {
        this.conversions = conversions;
    }

    public Data convert(CallContext context, Data source) {
        Data target = null;
        if (getConverter(context) != null) {
            target = getConverter(context).convert(context, source, getArguments(context));
        } else if (getConverterId(context) != null) {
            target = DataConversionManager.getSingleton(context).convert(context, source, getConverterId(context), getArguments(context));
        } else if (getTargetType(context) != null) {
            target = DataConversionManager.getSingleton(context).tryConvert(context, source, getTargetType(context), getArguments(context));
        } else if (getConversions(context) != null) {
            for (DataConversion conversion : getConversions(context)) {
                target = conversion.convert(context, source);
                source = target;
            }
        }
        return target;
    }

    public String toString(CallContext context) {
        return   "["
               + (converter == null ? "" : ("converter:" + ContextAware.ToString.convert(context, converter))) + "|"
               + (converter_id == null ? "" : ("id:" + converter_id)) + "|"
               + (target_type == null ? "" : ("target:" + target_type.getName(context))) + "|"
            + (arguments == null ? "" : ("arguments:" + StringUtilities.join(context, arguments.entrySet(), ";", true))) + "|"
               + (conversions == null ? "" : ("conversions:" + StringUtilities.join(context, conversions, ";", true)))
               + "]";
    }
}
