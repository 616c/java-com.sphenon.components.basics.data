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

public class ConversionManagerCache<T2,T1>
    implements ConversionManager<T2,T1>
{
    protected KeyWeakHashMap<T2,T1> cache;

    protected ConversionManagerCache(CallContext context) {
        this.cache = new KeyWeakHashMap<T2,T1>();
    }

    static public<T2,T1> ConversionManagerCache<T2,T1> get(CallContext context) {
        return new ConversionManagerCache<T2,T1>(context);
    }

    public T1 convert(CallContext context, T2 source_item, Object... additional_arguments) throws ConversionFailure {
        T1 result = this.cache.get(source_item);
        if (result == null) {
            try {
                result = ((Converter<T2,T1>)(additional_arguments[0])).convert(context, source_item);
            } catch (Throwable t) {
                ConversionFailure.createAndThrow(context, t, "Could not convert source '%(source)'", "source", source_item);
            }
            this.cache.put(source_item, result);
        }
        return result;
    }

    public T2 reconvert(CallContext context, T1 source_item, Object... additional_arguments) throws ConversionFailure {
        T2 result = null;
        CustomaryContext.create((Context)context).throwLimitation(context, "don't know how to reconvert '%(source)'", "source", source_item);
        return result;
    }
}
