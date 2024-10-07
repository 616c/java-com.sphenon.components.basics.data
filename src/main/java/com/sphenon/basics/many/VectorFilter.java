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

public class VectorFilter<T>
  implements GenericVector<T> {
    private GenericVector<T> vector;
    protected boolean caching;
    protected java.util.Vector<Long> index_map;
    protected long orig_index;

    public VectorFilter(CallContext context, GenericVector<T> vector) {
        this.vector = vector;
    }

    public VectorFilter(CallContext context, GenericVector<T> vector, boolean caching) {
        this.vector = vector;
        this.caching = caching;
    }

    public void setVector (CallContext context, GenericVector<T> vector) {
        this.vector = vector;
    }

    protected GenericVector<T> getVector(CallContext context) {
        return this.vector;
    }

    protected boolean isMatching(CallContext context, T item, long orig_index) {
        return this.isMatching(context, item);
    }

    protected boolean isMatching(CallContext context, T item) {
        return true;
    }

    protected long _getIndex(CallContext context, long filter_index) {
        long index = 0;
        if (caching) {
            if (index_map == null) {
                index_map = new java.util.Vector<Long>();
                orig_index = 0;
            }
            index = index_map.size();
            if (filter_index < index) {
                return index_map.get((int) filter_index);
            }
        } else {
            orig_index = 0;
        }
        long size = this.vector.getSize(context);
        if (size <= filter_index) {
            return -1;
        }
        for (; orig_index < size; orig_index++) {
            T item = this.vector.tryGet(context, orig_index);
            if (isMatching(context, item, orig_index)) {
                if (caching) {
                    index_map.add(orig_index);
                }
                if (index == filter_index) { return orig_index++; }
                index++;
            }
        }
        return -1;
    }

    public T get (CallContext context, long index) throws DoesNotExist {
        T result = this.tryGet(context, index);
        if (result == null) {
            DoesNotExist.createAndThrow (context);
            throw (DoesNotExist) null; // compiler insists
        }
        return result;
    }

    public T tryGet (CallContext context, long index) {
        index = _getIndex(context, index);
        return index == -1 ? null : this.vector.tryGet(context, index);
    }

    public boolean  canGet (CallContext context, long index) {
        index = _getIndex(context, index);
        return index != -1 ? true : false;
    }

    public VectorReferenceToMember<T> getReference    (CallContext context, long index) throws DoesNotExist {
        if ( ! canGet(context, index)) {
            DoesNotExist.createAndThrow (context);
            throw (DoesNotExist) null; // compiler insists
        }
        return new VectorReferenceToMember<T>(context, this, index);
    }

    public VectorReferenceToMember<T> tryGetReference (CallContext context, long index) {
        if ( ! canGet(context, index)) { return null; }
        return new VectorReferenceToMember<T>(context, this, index);
    }

    public T set (CallContext context, long filter_index, T item) {
        long index = _getIndex(context, filter_index);
        if (index == -1) {
            index = this.vector.getSize(context) + filter_index - this.getSize(context);
        }
        return this.vector.set(context, index, item);
    }

    public void add (CallContext context, long index, T item) throws AlreadyExists {
        if (index < this.getSize(context)) { AlreadyExists.createAndThrow (context); }
        this.set(context, index, item);
    }

    public void prepend (CallContext context, T item) {
        this.vector.prepend(context, item);
    }

    public void append (CallContext context, T item) {
        this.vector.append(context, item);
    }

    public void insertBefore (CallContext context, long index, T item) throws DoesNotExist {
        index = _getIndex(context, index);
        this.vector.insertBefore(context, index, item);
    }

    public void insertBehind (CallContext context, long index, T item) throws DoesNotExist {
        index = _getIndex(context, index);
        this.vector.insertBehind(context, index, item);
    }

    public T replace (CallContext context, long index, T item) throws DoesNotExist {
        index = _getIndex(context, index);
        return this.vector.replace(context, index, item);
    }

    public T unset (CallContext context, long index) {
        index = _getIndex(context, index);
        return this.vector.unset(context, index);
    }

    public T remove (CallContext context, long index) throws DoesNotExist {
        index = _getIndex(context, index);
        return this.vector.remove(context, index);
    }

    public IteratorItemIndex<T> getNavigator (CallContext context) {
        return new VectorIteratorImpl<T> (context, this);
    }

    public long getSize (CallContext context) {
        long index = 0;
        if (caching) {
            if (index_map == null) {
                index_map = new java.util.Vector<Long>();
                orig_index = 0;
            }
            index = index_map.size();
        } else {
            orig_index = 0;
        }
        long size = this.vector.getSize(context);
        for (; orig_index < size; orig_index++) {
            T item = this.vector.tryGet(context, orig_index);
            if (isMatching(context, item, orig_index)) {
                if (caching) {
                    index_map.add(orig_index);
                }
                index++;
            }
        }
        return index;
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
        return new IteratorAdapter<T>(context, this.getNavigator(context));
    }



    public VectorIterable<T> getIterable (CallContext context) {
        return new VectorIterable<T>(context, this);
    }

    public void release(CallContext context) {
        if (this.getVector(context) != null && this.getVector(context) instanceof ManagedResource) {
            ((ManagedResource)(this.getVector(context))).release(context);
        }
    }
}
