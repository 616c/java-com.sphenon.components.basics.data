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
import com.sphenon.basics.metadata.Type;
import com.sphenon.basics.metadata.Typed;
import com.sphenon.basics.metadata.TypeManager;
import com.sphenon.basics.data.conversion.returncodes.*;

import com.sphenon.basics.many.returncodes.*;

import java.util.List;

public class VectorAdapter1ToN<T1,T2>
  extends VectorAdapter1ToNBase<T1,T2> {

    private ConversionManager1ToN<T2,T1> conversion_manager;
    private Object[] additional_arguments;

    public VectorAdapter1ToN(CallContext context, Type component_type, ConversionManager1ToN<T2,T1> conversion_manager, GenericVector<T2> source_vector, Object... additional_arguments) {
        super(context, component_type, source_vector);
        this.additional_arguments = additional_arguments;
        this.conversion_manager = conversion_manager;
    }

    public VectorAdapter1ToN(CallContext context, Type component_type, GenericVector<T2> source_vector, Object... additional_arguments) {
        this(context, component_type, ConversionManager1ToNCache.<T2,T1>get(context), source_vector, additional_arguments);
    }

    public void setAdditionalArguments(CallContext context, Object... additional_arguments) {
        this.additional_arguments = additional_arguments;
    }

    protected List<T1> convert(CallContext context, T2 source_item) {
        try {
            if (this.additional_arguments != null && this.additional_arguments.length != 0) {
                return this.conversion_manager.convertToMany(context, source_item, this.additional_arguments);
            } else {
                return this.conversion_manager.convertToMany(context, source_item);
            }
        } catch (ConversionFailure cf) {
            CustomaryContext.create((Context)context).throwProtocolViolation(context, cf, "In a vector adapter, conversion from SourceItemType to List<TargetItemType> failed, which is not expected");
            throw (ExceptionProtocolViolation) null; // compiler insists
        }
    }
}
