package com.sphenon.basics.data.classes;

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
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.locating.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.expression.returncodes.*;
import com.sphenon.basics.customary.*;

import com.sphenon.basics.data.*;
import com.sphenon.basics.data.tplinst.*;

import java.util.Map;
import java.util.HashMap;

public class DataSource_Expression extends DataSourceBase {

    public DataSource_Expression (CallContext context) {
    }

    protected String expression;

    public String getExpression (CallContext context) {
        return this.expression;
    }

    public void setExpression (CallContext context, String expression) {
        this.expression = expression;
    }

    protected Map<String,DataSource> arguments;

    public Map<String,DataSource> getArguments (CallContext context) {
        return this.arguments;
    }

    public void setArguments (CallContext context, Map<String,DataSource> arguments) {
        this.arguments = arguments;
    }

    public Object get(CallContext context) {
        HashMap<String,Object> resolved_arguments = new HashMap<String,Object>();
        for (String key : this.arguments.keySet()) {
            resolved_arguments.put(key, arguments.get(key).getObject(context));
        }
        try {
            return Expression.evaluate(context, this.expression, null, resolved_arguments);
        } catch (EvaluationFailure ef) {
            CustomaryContext.create((Context)context).throwConfigurationError(context, ef, "Could not evaluate expression in data source '%(expression)'", "expression", this.expression);
            throw (ExceptionConfigurationError) null; // compiler insists
        }
    }
}
