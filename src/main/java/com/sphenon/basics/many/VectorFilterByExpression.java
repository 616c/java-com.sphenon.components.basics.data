package com.sphenon.basics.many;

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
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.Notifier;
import com.sphenon.basics.notification.NotificationLevel;
import com.sphenon.basics.notification.NotificationContext;
import com.sphenon.basics.notification.NotificationLocationContext;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.expression.returncodes.*;
import com.sphenon.basics.metadata.Type;
import com.sphenon.basics.metadata.Typed;
import com.sphenon.basics.metadata.TypeManager;
import com.sphenon.basics.data.conversion.returncodes.*;

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;

public class VectorFilterByExpression<T>
  extends VectorFilter<T> {

    protected Expression expression;

    public VectorFilterByExpression (CallContext context, GenericVector<T> vector, String expression) {
        super(context, vector);
        this.expression = new Expression(context, expression);
    }

    public VectorFilterByExpression (CallContext context, GenericVector<T> vector, String expression, boolean caching) {
        super(context, vector, caching);
        this.expression = new Expression(context, expression);
    }

    protected boolean isMatching(CallContext context, T item) {
        Object condo = null;
        try {
            condo = this.expression.evaluate(context, "item", item);
        } catch (EvaluationFailure ef) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, ef, "Evaluation of expression '%(expression)' failed", "expression", this.expression.getExpression(context));
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }
        try {
            return (Boolean) condo;
        } catch (ClassCastException cce) {
            CustomaryContext.create((Context)context).throwConfigurationError(context, cce, "Expression '%(expression)' in VectorFilterByExpression does not evaluate to a Boolean instance, but to a '%(got)'", "got", condo.getClass(), "expression", this.expression.getExpression(context));
            throw (ExceptionConfigurationError) null; // compiler insists
        }
    }
}
