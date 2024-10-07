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
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.message.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.expression.*;

import com.sphenon.basics.data.*;
import com.sphenon.basics.data.tplinst.*;

import java.util.regex.*;
import java.util.Map;

public class DataConversionManager {
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.data.conversion.DataConversionManager"); };

    static protected DataConversionManager singleton;

    static public DataConversionManager getSingleton(CallContext context) {
        if (singleton == null) {
            singleton = new DataConversionManager(context);
        }
        return singleton;
    }

    protected OOMap_DataConverter_Type_Type_ registry;

    protected DataConversionManager (CallContext context) {
        registry = new OOMapImpl_DataConverter_Type_Type_(context);
    }

    public void register (CallContext call_context, DataConverter converter) {
        register(call_context, converter, true, true);
    }

    public void register (CallContext call_context, DataConverter converter, boolean register_by_id, boolean register_by_type) {
        Context context = Context.create(call_context);
        CustomaryContext cc = CustomaryContext.create(context);

        Type source_type = converter.getSourceType(context);
        Type target_type = converter.getTargetType(context);

        if ((this.notification_level & Notifier.DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.DIAGNOSTICS, "Registering data converter '%(converter)' from '%(source)' to '%(target)'", "source", source_type.getName(context), "target", target_type.getName(context), "converter", converter); }

        if (register_by_type) {
            registry.set(context, source_type, target_type, converter);
        }

        if (register_by_id) {
            if (converter.getId(context) != null) {
                if (converter_by_id == null) {
                    converter_by_id = new java.util.Hashtable();
                }
                converter_by_id.put(converter.getId(context), converter);
            }
        }
    }

    public Data tryConvert(CallContext context, Data source, Type target_type) {
        return tryConvert(context, source, target_type, null);
    }

    public Data tryConvert(CallContext context, Data source, Type target_type, Map arguments) {
        Type source_type = source.getDataType(context);
        if (source_type.isA(context, target_type)) {
            return source;
        }

        DataConverter converter = registry.tryGet(context, source_type, target_type);

        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "Looking for converter from '%(source)' to '%(target)', result '%(converter)'", "source", source.getDataType(context).getName(context), "target", target_type.getName(context), "converter", converter); }

        if (converter == null) {
            return null;
        }

        return converter.convert(context, source, arguments);
    }

    static protected java.util.Hashtable converter_by_id;
    static protected String specres = "^([A-Za-z0-9_-]+)(?:\\((.*)\\))?$";
    static protected RegularExpression specre = new RegularExpression(specres);
    static protected String parares = "([A-Za-z0-9_-]+)=\"([^\\\\\"]*(?:\\[\\\\\"][^\\\\\"]*)*)\"(?:,|$)";
    static protected RegularExpression parare = new RegularExpression(parares);
    static protected RegularExpression repre = new RegularExpression("\\\\([\\\\\"])", "$1");

    static public Data convert(CallContext context, Data source, String converter_spec) {
        return convert(context, source, converter_spec, null);
    }

    static public Data convert(CallContext context, Data source, String converter_spec, Map arguments) {
        DataConverter converter = null;

        Matcher m = specre.getMatcher(context, converter_spec);
        if ( ! m.matches()) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, "DataConverter specification '%(sepc)' does not match '%(specres)'", "spec", converter_spec, "regexp", specres);
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }

        String converter_id = m.group(1);
        java.util.Hashtable args = arguments == null ? new java.util.Hashtable() : new java.util.Hashtable(arguments);
        if (m.group(2) != null) {
            Matcher m2 = parare.getMatcher(context, m.group(2));
            while (m2.find()) {
                args.put(m2.group(1), repre.replaceAll(context, m2.group(2)));
            }
        }

        if (converter_by_id == null) {
            converter_by_id = new java.util.Hashtable();
        } else {
            converter = (DataConverter) converter_by_id.get(converter_id);
        }

        if (converter == null) {
            String entry = DataPackageInitialiser.getConfiguration(context).get(context, "conversion.DataConverter." + converter_id, (String) null);
            if (entry == null) {
                CustomaryContext.create((Context)context).throwPreConditionViolation(context, "DataConverter '%(id)' is not configured (no property entry)", "id", converter_id);
                throw (ExceptionPreConditionViolation) null; // compiler insists
            }
            converter = DataPackageInitialiser.processEntry(context, "DataConverter." + converter_id, entry);
            if (converter == null) {
                CustomaryContext.create((Context)context).throwPreConditionViolation(context, "DataConverter '%(id)' is configured, but not supported yet", "id", converter_id);
                throw (ExceptionPreConditionViolation) null; // compiler insists
            }
            converter_by_id.put(converter_id, converter);
        }

        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendTrace(context, Notifier.DIAGNOSTICS, "Converting from '%(source)' with converter '%(converter)'", "source", source.getDataType(context).getName(context), "converter", converter); }

        return converter.convert(context, source, args);
    }

    static public DataConverter getConverter(CallContext context, String id) {
        return converter_by_id == null ? null : (DataConverter) converter_by_id.get(id);
    }
}
