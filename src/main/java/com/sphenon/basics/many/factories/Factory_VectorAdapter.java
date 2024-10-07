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
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.data.*;
import com.sphenon.basics.data.conversion.returncodes.*;

import com.sphenon.basics.many.returncodes.*;
import com.sphenon.basics.many.tplinst.*;

public class Factory_VectorAdapter<T1,T2> {


    public Factory_VectorAdapter(CallContext context) {
    }

    public VectorAdapter<T1,T2> create (CallContext context) {
        return this.source_vector != null ? new VectorAdapter<T1,T2>(context, this.component_type, this.conversion_manager, this.source_vector, this.additional_arguments)
                                          : new VectorAdapter<T1,T2>(context, this.component_type, this.conversion_manager, this.source_vector_ds, this.additional_arguments);
    }

    protected GenericVector<T2> source_vector;

    public void setSourceVector (CallContext context, GenericVector<T2> source_vector) {
        this.source_vector = source_vector;
    }

    public GenericVector<T2> getSourceVector (CallContext context) {
        return this.source_vector;
    }

    public GenericVector<T2> defaultSourceVector (CallContext context) {
        return null;
    }

    protected DataSource<GenericVector<T2>> source_vector_ds;

    public void setSourceVectorDS (CallContext context, DataSource<GenericVector<T2>> source_vector_ds) {
        this.source_vector_ds = source_vector_ds;
    }

    public DataSource<GenericVector<T2>> getSourceVectorDS (CallContext context) {
        return this.source_vector_ds;
    }

    public DataSource<GenericVector<T2>> defaultSourceVectorDS (CallContext context) {
        return null;
    }

    protected ConversionManager<T2,T1> conversion_manager;

    public void setConversionManager(CallContext context, ConversionManager<T2,T1> conversion_manager) {
        this.conversion_manager = conversion_manager;
    }

    private Object[] additional_arguments;

    public void setAdditionalArguments(CallContext context, Object... additional_arguments) {
        this.additional_arguments = additional_arguments;
    }

    protected Type component_type;

    public void set_ComponentType(CallContext context, Type component_type) {
        this.component_type = component_type;
    }

    static public Type get_GenericComponentTypeMethod(CallContext context, Type type) {
        if ((type instanceof TypeParametrised) == false) { return null; }
        Vector_Object_long_ ps = ((TypeParametrised) type).getParameters(context);
        if (ps == null || ps.getSize(context) != 2) { return null; }
        Object p = ps.tryGet(context, 0);
        if ((p instanceof JavaType) || (p instanceof TypeParametrised)) {
            return (Type) p;
        }
        return null;
    }
}
