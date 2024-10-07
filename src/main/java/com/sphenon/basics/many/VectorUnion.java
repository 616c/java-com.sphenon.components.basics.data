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
import com.sphenon.basics.message.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.metadata.Type;
import com.sphenon.basics.metadata.Typed;
import com.sphenon.basics.metadata.TypedImpl;
import com.sphenon.basics.metadata.TypeManager;
import com.sphenon.basics.data.conversion.returncodes.*;

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;
import com.sphenon.basics.event.*;
import com.sphenon.basics.event.tplinst.*;

import com.sphenon.engines.aggregator.annotations.*;

public class VectorUnion<T>
  implements GenericVector<T>,
             TypedImpl,
             ManagedResource {

    protected GenericVector<GenericVector<T>> vectors;

    public VectorUnion (CallContext context, Type component_type) {
        this.type = component_type;
        this.vectors = null;
    }

    public VectorUnion (CallContext context, Type component_type, GenericVector<GenericVector<T>> vectors) {
        this.type = component_type;
        this.vectors = vectors;
    }

    @OCPIgnore
    public VectorUnion (CallContext context, Type component_type, GenericVector<T>... vectors) {
        this.type = component_type;
        this.vectors = GenericVectorImplList.<GenericVector<T>>create(context, this.type);
        for (GenericVector<T> vector : vectors) {
            this.vectors.append(context, vector);
        }
    }

    protected Type type;

    public Type getType(CallContext context) {
        return type;
    }

    public void setType(CallContext context, Type type) {
        this.type = type;
    }

    public void setVectors (CallContext context, GenericVector<GenericVector<T>> vectors) {
        this.vectors = vectors;
    }

    @OCPIgnore
    public void setVectors (CallContext context, GenericVector<T>... vectors) {
        this.vectors = GenericVectorImplList.<GenericVector<T>>create(context, this.type);
        for (GenericVector<T> vector : vectors) {
            this.vectors.append(context, vector);
        }
    }

    public void addVector(CallContext context, GenericVector<T> vector) {
        if (this.vectors == null) {
            this.vectors = GenericVectorImplList.<GenericVector<T>>create(context, this.type);
        }
        this.vectors.append(context, vector);
    }

    public GenericVector<GenericVector<T>> getImplementationVector(CallContext context) {
        return this.vectors;
    }

    protected class SeekResult {
        public GenericVector<T> vector;
        public long index;
    }

    protected SeekResult getVector (CallContext context, long index) {
        long offset = 0;
        SeekResult sr = new SeekResult();

        for (GenericVector<T> vector : this.vectors.getIterable(context)) {
            sr.vector = vector;
            if (sr.vector == null) { continue; }
            sr.index = index - offset;                

            long size = sr.vector.getSize(context);
            if (sr.index < size) {
                return sr;
            }
            offset += size;
        }
        return sr;
    }

    public T                                    get             (CallContext context, long index) throws DoesNotExist {
        SeekResult sr = this.getVector(context, index);
        if (sr.vector == null) { DoesNotExist.createAndThrow(context, "entry '%(index)'", "index", t.s(index)); }
        return sr.vector.get(context, sr.index);
    }

    public T                                    tryGet          (CallContext context, long index) {
        SeekResult sr = this.getVector(context, index);
        if (sr.vector == null) { return null; }
        return sr.vector.tryGet(context, sr.index);
    }

    public boolean                                     canGet          (CallContext context, long index) {
        SeekResult sr = this.getVector(context, index);
        if (sr.vector == null) { return false; }
        return sr.vector.canGet(context, sr.index);
    }

    public ReferenceToMember<T,ReadOnlyVector<T>>      getReference    (CallContext context, long index) throws DoesNotExist {
        SeekResult sr = this.getVector(context, index);
        if (sr.vector == null) { DoesNotExist.createAndThrow(context, "entry '%(index)'", "index", t.s(index)); }
        return sr.vector.getReference(context, sr.index);
    }

    public ReferenceToMember<T,ReadOnlyVector<T>>      tryGetReference (CallContext context, long index) {
        SeekResult sr = this.getVector(context, index);
        if (sr.vector == null) { return null; }
        return sr.vector.tryGetReference(context, sr.index);
    }

    public T                                           set             (CallContext context, long index, T item) {
        SeekResult sr = this.getVector(context, index);
        return sr.vector.set(context, sr.index, item);
    }

    public void                                        add             (CallContext context, long index, T item) throws AlreadyExists {
        SeekResult sr = this.getVector(context, index);
        sr.vector.add(context, sr.index, item);
    }

    public void                                        prepend         (CallContext context, T item) {
        this.vectors.tryGet(context, 0).prepend(context, item);
    }

    public void                                        append          (CallContext context, T item) {
        this.vectors.tryGet(context, this.vectors.getSize(context)-1).append(context, item);
    }

    public void                                        insertBefore    (CallContext context, long index, T item) throws DoesNotExist {
        SeekResult sr = this.getVector(context, index);
        sr.vector.insertBefore(context, sr.index, item);
    }

    public void                                        insertBehind    (CallContext context, long index, T item) throws DoesNotExist {
        SeekResult sr = this.getVector(context, index);
        sr.vector.insertBehind(context, sr.index, item);
    }

    public T                                    replace         (CallContext context, long index, T item) throws DoesNotExist {
        SeekResult sr = this.getVector(context, index);
        return sr.vector.replace(context, sr.index, item);
    }

    public T                                    unset           (CallContext context, long index) {
        SeekResult sr = this.getVector(context, index);
        return sr.vector.unset(context, sr.index);
    }

    public T                                    remove          (CallContext context, long index) throws DoesNotExist {
        SeekResult sr = this.getVector(context, index);
        return sr.vector.remove(context, sr.index);
    }

    public IteratorItemIndex<T>                 getNavigator    (CallContext context) {
        return new VectorIteratorImpl<T>(context, this);
    }

    public long                                 getSize         (CallContext context) {
        long size = 0;
        if (this.vectors != null) {
            for (long v=0; v<this.vectors.getSize(context); v++) {
                GenericVector<T> vector = this.vectors.tryGet(context, v);
                if (vector != null) { size += vector.getSize(context); }
            }
        }
        return size;
    }

    protected class IteratorAdapter<T> implements java.util.Iterator<T> {
        protected IteratorItemIndex<T> iterator;
        protected CallContext context;
        public IteratorAdapter(CallContext context, IteratorItemIndex<T> iterator) {
            this.iterator = iterator;
            this.context = context;
        }
        public boolean hasNext() { return iterator.canGetCurrent(this.context); }
        public void remove() { throw new UnsupportedOperationException(); }
        public T next() {
            if (iterator.canGetCurrent(this.context) == false) {
                throw new java.util.NoSuchElementException();
            }
            T current = iterator.tryGetCurrent(this.context);
            iterator.next(this.context);
            return current;
        }
    }

    public java.util.Iterator<T> getIterator (CallContext context) {
        return new IteratorAdapter(context, this.getNavigator(context));
    }

    public VectorIterable<T>     getIterable     (CallContext context) {
        return new VectorIterable<T>(context, this);
    }

    public void release(CallContext context) {
        if (this.vectors != null && this.vectors instanceof ManagedResource) {
            ((ManagedResource)(this.vectors)).release(context);
        }
    }
}
