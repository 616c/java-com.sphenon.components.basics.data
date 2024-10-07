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
import com.sphenon.basics.expression.*;
import com.sphenon.basics.encoding.*;
import com.sphenon.basics.variatives.classes.*;
import com.sphenon.basics.data.*;
import com.sphenon.basics.operations.*;
import com.sphenon.basics.expression.classes.*;
import com.sphenon.basics.expression.returncodes.*;

import java.util.regex.*;

public class DynamicStringProcessor_Variative implements ExpressionEvaluator {

    public DynamicStringProcessor_Variative (CallContext context) {
        this.result_attribute = new Class_ActivityAttribute(context, "Result", "Object", "-", "*");
        this.activity_interface = new Class_ActivityInterface(context);
        this.activity_interface.addAttribute(context, this.result_attribute);
    }

    protected Class_ActivityInterface activity_interface;
    protected ActivityAttribute result_attribute;

    public String[] getIds(CallContext context) {
        return new String[] { "variative" };
    }

    public class MyStringFinderOperations extends HumanLanguageVariantFinder.StringFinderOperations {
        protected String[][] variants;
        protected String string;
        public MyStringFinderOperations(CallContext context, String[][] variants, String string) {
            super(context);
            this.variants = variants;
            this.string = string;
        }
        public String getExactVariant(CallContext context, String isolang) {
            if (isolang.equals("*debug*")) {
                return "[dynamicstring:" + string + "]";
            }
            if (variants != null) {
                for (String[] v : variants) {
                    if (v != null && v[0].equals(isolang)) { return v.length > 1 ? v[1] : ""; }
                }
            }
            return null;
        }
    }

    static protected Pattern variative_string_placeholder;
    static protected boolean initialised;

    public Object evaluate(CallContext context, String string, Scope scope, DataSink<Execution> execution_sink) {
        if (initialised == false) {
            initialised = true;
            String regexp = null;
            try {
                regexp = "%\\{([^\\}]*)\\}";
                variative_string_placeholder = Pattern.compile(regexp);
            } catch (PatternSyntaxException pse) {
                CustomaryContext.create(Context.create(context)).throwAssertionProvedFalse(context, pse, "Syntax error in com.sphenon.basics.humanlanguages in regular expression '%(regexp)'", "regexp", regexp);
                throw (ExceptionAssertionProvedFalse) null; // compiler insists
            }
        }

        String isolang = null;
        if (scope != null) {
            Scope.Result result = scope.tryGetWithNull(context, "humanlanguage");
            if (result != null) {
                isolang = (String) (result.value);
            }
        }
        Matcher m = variative_string_placeholder.matcher(string);
        StringBuffer sb = new StringBuffer();

        while (m.find()) {
            String     variant_string  = m.group(1);
            String[]   variant_entries = variant_string.split(",");
            String[][] variants        = variant_entries == null ? null : new String[variant_entries.length][2];
        
            if (variant_entries != null) {
                int i=0;
                for (String ve : variant_entries) {
                    variants[i] = ve.split("=");
                    if (variants[i].length > 1) {
                        variants[i][1] = Encoding.recode(context, variants[i][1], Encoding.URI, Encoding.UTF8);
                    }
                    i++;
                }
            }
            m.appendReplacement(sb, "");
            String human_variant = isolang != null ? HumanLanguageVariantFinder.findVariant(context, new MyStringFinderOperations(context, variants, string), isolang) : HumanLanguageVariantFinder.findVariant(context, new MyStringFinderOperations(context, variants, string));
            sb.append(human_variant);
        }
        m.appendTail(sb);

        return sb.toString();
    }

    public ActivityClass parse(CallContext context, ExpressionSource expression_source) throws EvaluationFailure {
        return new ActivityClass_ExpressionEvaluator(context, this, expression_source, this.activity_interface, this.result_attribute);
    }
}
