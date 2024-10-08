package com.sphenon.basics.variatives;

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
import com.sphenon.basics.notification.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.data.*;
import com.sphenon.basics.operations.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.expression.classes.*;
import com.sphenon.basics.expression.returncodes.*;

import java.util.regex.*;

public class DynamicStringProcessor_StringPool implements ExpressionEvaluator {

    public DynamicStringProcessor_StringPool (CallContext context) {
        this.result_attribute = new Class_ActivityAttribute(context, "Result", "Object", "-", "*");
        this.activity_interface = new Class_ActivityInterface(context);
        this.activity_interface.addAttribute(context, this.result_attribute);
    }

    protected Class_ActivityInterface activity_interface;
    protected ActivityAttribute result_attribute;

    public String[] getIds(CallContext context) {
        return new String[] { "stringpool" };
    }

    static protected Pattern string_id_placeholder;
    static protected boolean initialised;

    public Object evaluate(CallContext context, String string, Scope scope, DataSink<Execution> execution_sink) {
        if (initialised == false) {
            initialised = true;
            String regexp = null;
            try {
                regexp = "%\\{([^\\}:]*)(?::([^\\}]*))?\\}";
                string_id_placeholder = Pattern.compile(regexp);
            } catch (PatternSyntaxException pse) {
                CustomaryContext.create(Context.create(context)).throwAssertionProvedFalse(context, pse, "Syntax error in com.sphenon.basics.humanlanguages in regular expression '%(regexp)'", "regexp", regexp);
                throw (ExceptionAssertionProvedFalse) null; // compiler insists
            }
        }

        Matcher m = string_id_placeholder.matcher(string);
        StringBuffer sb = new StringBuffer();

        while (m.find()) {
            String string_id = m.group(1);
            String isolangs = m.group(2);
            m.appendReplacement(sb, "");
            sb.append(StringContext.get((Context) context).getString(context, string_id, isolangs));
        }
        m.appendTail(sb);

        return sb.toString();
    }

    public ActivityClass parse(CallContext context, ExpressionSource expression_source) throws EvaluationFailure {
        return new ActivityClass_ExpressionEvaluator(context, this, expression_source, this.activity_interface, this.result_attribute);
    }
}
