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

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;
import com.sphenon.basics.event.*;
import com.sphenon.basics.event.tplinst.*;

public class VectorProxy<T>
  implements GenericVector<T>,
             VectorReorderable<T>,
             ManagedResource {
    private GenericVector<T> vector;

    public VectorProxy(CallContext context, GenericVector<T> vector) {
        this.vector = vector;
    }

    public void setVector (CallContext context, GenericVector<T> vector) {
        this.vector = vector;
    }

    protected GenericVector<T> getVector(CallContext context) {
        return this.vector;
    }

    public T                                       get             (CallContext context, long index) throws DoesNotExist {
        return this.getVector(context).get(context, index);
    }

    public T                                       tryGet          (CallContext context, long index) {
        return this.getVector(context).tryGet(context, index);
    }

    public boolean                                 canGet          (CallContext context, long index) {
        return this.getVector(context).canGet(context, index);
    }

    public ReferenceToMember<T,ReadOnlyVector<T>>  getReference    (CallContext context, long index) throws DoesNotExist {
        return this.getVector(context).getReference(context, index);
    }

    public ReferenceToMember<T,ReadOnlyVector<T>>  tryGetReference (CallContext context, long index) {
        return this.getVector(context).tryGetReference(context, index);
    }

    public T                                       set             (CallContext context, long index, T item) {
        return this.getVector(context).set(context, index, item);
    }

    public void                                    add             (CallContext context, long index, T item) throws AlreadyExists {
        this.getVector(context).add(context, index, item);
    }

    public void                                    prepend         (CallContext context, T item) {
        this.getVector(context).prepend(context, item);
    }

    public void                                    append          (CallContext context, T item) {
        this.getVector(context).append(context, item);
    }

    public void                                    insertBefore    (CallContext context, long index, T item) throws DoesNotExist {
        this.getVector(context).insertBefore(context, index, item);
    }

    public void                                    insertBehind    (CallContext context, long index, T item) throws DoesNotExist {
        this.getVector(context).insertBehind(context, index, item);
    }

    public T                                       replace         (CallContext context, long index, T item) throws DoesNotExist {
        return this.getVector(context).replace(context, index, item);
    }

    public T                                       unset           (CallContext context, long index) {
        return this.getVector(context).unset(context, index);
    }

    public T                                       remove          (CallContext context, long index) throws DoesNotExist {
        return this.getVector(context).remove(context, index);
    }

    public IteratorItemIndex<T>                    getNavigator    (CallContext context) {
        return this.getVector(context).getNavigator(context);
    }

    public long                                    getSize         (CallContext context) {
        return this.getVector(context).getSize(context);
    }

    public java.util.Iterator<T>                   getIterator     (CallContext context) {
        return this.getVector(context).getIterator(context);
    }

    public VectorIterable<T>                       getIterable     (CallContext context) {
        return new VectorIterable<T>(context, this);
    }

    public void                                    release         (CallContext context) {
        if (this.vector != null && this.vector instanceof ManagedResource) {
            ((ManagedResource)(this.vector)).release(context);
        }
    }

    public boolean                                 isReorderable   (CallContext context) {
        return (this.getVector(context) instanceof VectorReorderable && ((VectorReorderable)(this.getVector(context))).isReorderable(context));
    }

    public void                                    move            (CallContext context, long from_index, long to_index) throws DoesNotExist {
        ((VectorReorderable)(this.getVector(context))).move(context, from_index, to_index);
    }
}
