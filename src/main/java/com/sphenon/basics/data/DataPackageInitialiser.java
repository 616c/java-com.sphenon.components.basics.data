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
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.message.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.variatives.*;

import com.sphenon.basics.data.conversion.*;

import java.util.StringTokenizer;


public class DataPackageInitialiser {
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.data.DataPackageInitialiser"); };

    static protected boolean initialised = false;

    static {
        initialise(RootContext.getRootContext());
    }

    static protected Configuration config;
    static public Configuration getConfiguration (CallContext context) { return config; }
    
    static public synchronized void initialise (CallContext context) {
        
        if (initialised == false) {
            initialised = true;

            Configuration.initialise(context);
            MetaDataPackageInitialiser.initialise(context);

            config = Configuration.create(RootContext.getInitialisationContext(), "com.sphenon.basics.data");

            Configuration.loadDefaultProperties(context, com.sphenon.basics.data.DataPackageInitialiser.class);

            loadDataConverter(context);

            ExpressionEvaluatorRegistry.registerDynamicStringEvaluator(context, new com.sphenon.basics.variatives.DynamicStringProcessor_StringPool(context));
            ExpressionEvaluatorRegistry.registerDynamicStringEvaluator(context, new com.sphenon.basics.variatives.DynamicStringProcessor_Variative(context));
        }
    }

    static protected void loadDataConverter (CallContext call_context) {
        Context context = Context.create(call_context);
        CustomaryContext cc = CustomaryContext.create(context);

        String entry;
        int entry_number = 0;
        while ((entry = config.get(context, "conversion.DataConverter." + ++entry_number, (String) null)) != null) {
            DataConverter dc = processEntry(context, "DataConverter." + entry_number, entry);
            if (dc != null) {
                DataConversionManager.getSingleton(context).register(context, dc);
            }
        }
    }

    static public DataConverter processEntry(CallContext call_context, String property, String entry) {
        Context context = Context.create(call_context);
        CustomaryContext cc = CustomaryContext.create(context);
        try {
            StringTokenizer t = new StringTokenizer(entry, "|");
            if (! t.hasMoreTokens()) {
                cc.throwConfigurationError(context, "While parsing property '%(property)' = '%(entry)', no source type found', ", "property", property, "entry", entry);
                throw (ExceptionConfigurationError) null; // compiler insists
            }
            String source_type = java.net.URLDecoder.decode(t.nextToken(), "UTF-8");
            if (! t.hasMoreTokens()) {
                cc.throwConfigurationError(context, "While parsing property '%(property)' = '%(entry)', no target type found', ", "property", property, "entry", entry);
                throw (ExceptionConfigurationError) null; // compiler insists
            }
            String target_type = java.net.URLDecoder.decode(t.nextToken(), "UTF-8");
            if (! t.hasMoreTokens()) {
                cc.throwConfigurationError(context, "While parsing property '%(property)' = '%(entry)', no conversion method found', ", "property", property, "entry", entry);
                throw (ExceptionConfigurationError) null; // compiler insists
            }
            String conversion_method = java.net.URLDecoder.decode(t.nextToken(), "UTF-8");
            if (conversion_method.equals("execute")) {
                if (! t.hasMoreTokens()) {
                    cc.throwConfigurationError(context, "While parsing property '%(property)' = '%(entry)', no conversion command found', ", "property", property, "entry", entry);
                    throw (ExceptionConfigurationError) null; // compiler insists
                }
                String conversion_command = java.net.URLDecoder.decode(t.nextToken(), "UTF-8");
                if (! t.hasMoreTokens()) {
                    cc.throwConfigurationError(context, "While parsing property '%(property)' = '%(entry)', no filename substitution regexp found', ", "property", property, "entry", entry);
                    throw (ExceptionConfigurationError) null; // compiler insists
                }
                String filename_substitution_regexp = java.net.URLDecoder.decode(t.nextToken(), "UTF-8");
                if (! t.hasMoreTokens()) {
                    cc.throwConfigurationError(context, "While parsing property '%(property)' = '%(entry)', no filename substitution subst found', ", "property", property, "entry", entry);
                    throw (ExceptionConfigurationError) null; // compiler insists
                }
                String filename_substitution_subst = java.net.URLDecoder.decode(t.nextToken(), "UTF-8");
                return new DataConverter_MediaObject_SystemProcess (context,
                                                                    null,
                                                                    (TypeImpl_MediaObject) TypeManager.getMediaType(context, source_type),
                                                                    (TypeImpl_MediaObject) TypeManager.getMediaType(context, target_type), 
                                                                    conversion_command, 
                                                                    filename_substitution_regexp, 
                                                                    filename_substitution_subst);
            } else {
                if ((notification_level & Notifier.MONITORING) != 0) { cc.sendWarning(context, "Conversion method '%(method)' is not implemented", "method", conversion_method); }
                return null;
            }
        } catch (java.io.UnsupportedEncodingException uee) {
            return null;
        }
    }
}
