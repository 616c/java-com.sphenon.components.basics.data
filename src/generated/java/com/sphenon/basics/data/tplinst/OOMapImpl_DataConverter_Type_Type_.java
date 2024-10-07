// instantiated with jti.pl from OOMapImpl
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;
import com.sphenon.basics.data.*;
import com.sphenon.basics.data.conversion.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.tplinst.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.message.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;

import com.sphenon.basics.many.returncodes.*;

import java.util.Hashtable;

public class OOMapImpl_DataConverter_Type_Type_
  implements OOMap_DataConverter_Type_Type_
{
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.data.tplinst.OOMapImpl_DataConverter_Type_Type_"); };

    protected java.util.Hashtable map1;

    protected class Entry {
        private DataConverter                                  actual_item;
        private DataConverter                                  cached_item;
        private java.util.Vector                          child_entries;
        private java.util.Vector                          parent_entries;
        private OOMapImpl_DataConverter_Type_Type_ oomap;
        private Type                                index1;
        private Type                                index2;
        private int                                       path_length1;
        private int                                       path_length2;
        private Type                                cached_index1;
        private Type                                cached_index2;

        public Entry (CallContext context, OOMapImpl_DataConverter_Type_Type_ oomap, Type index1, Type index2) {
            CustomaryContext cc = (((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) ? CustomaryContext.create((Context) context) : null);

            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "new Entry in OOMapImpl_DataConverter_Type_Type_ : (%(index1),%(index2))", "index1", index1, "index2", index2); }

            this.oomap          = oomap;
            this.index1         = index1;
            this.index2         = index2;
            this.child_entries  = new java.util.Vector();
            this.parent_entries = new java.util.Vector();
            this.actual_item    = null;
            this.path_length1   = 9999;
            this.path_length2   = 9999;
            this.cached_item    = null;
            this.cached_index1  = null;
            this.cached_index2  = null;

            Iterator_Type_ supertypes1;
            Iterator_Type_ supertypes2;
            Type supertype1;
            Type supertype2;

            supertypes1 = index1.getSuperTypes(context).getNavigator(context);
            while ((supertype1 = supertypes1.tryGetCurrent(context)) != null) {
                if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "parent loop 1: (%(index1),%(index2))", "index1", supertype1, "index2", index2); }
                Entry parent = this.oomap._getEntry(context, supertype1, index2);
                this.parent_entries.add(parent);
                parent.addChildEntry(context, this);
                supertypes1.next(context);
            }

            supertypes2 = index2.getSuperTypes(context).getNavigator(context);
            while ((supertype2 = supertypes2.tryGetCurrent(context)) != null) {
                if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "parent loop 2: (%(index1),%(index2))", "index1", index1, "index2", supertype2); }
                Entry parent = this.oomap._getEntry(context, index1, supertype2);
                this.parent_entries.add(parent);
                parent.addChildEntry(context, this);
                supertypes2.next(context);
            }

            supertypes1 = index1.getSuperTypes(context).getNavigator(context);
            while ((supertype1 = supertypes1.tryGetCurrent(context)) != null) {
                supertypes2 = index2.getSuperTypes(context).getNavigator(context);
                while ((supertype2 = supertypes2.tryGetCurrent(context)) != null) {
                    if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "parent loop 3: (%(index1),%(index2))", "index1", supertype1, "index2", supertype2); }
                    Entry parent = this.oomap._getEntry(context, supertype1, supertype2);
                    this.parent_entries.add(parent);
                    parent.addChildEntry(context, this);
                    supertypes2.next(context);
                }
                supertypes1.next(context);
            }
            this.update(context);
        }

        public boolean isActualEntry() {
            return this.actual_item == null ? false : true;
        }

        public void setItem(CallContext context, DataConverter item) {
            this.actual_item   = item;
            this.cached_item   = null;
            this.cached_index1 = null;
            this.cached_index2 = null;
            if (item == null) {
                this.path_length1 = 9999;
                this.path_length2 = 9999;
                this.update(context);
            } else {
                this.path_length1 = 0;
                this.path_length2 = 0;
                this.updateChildEntries(context);
            }
        }

        public DataConverter getItem(CallContext context) {
            return this.actual_item != null ? this.actual_item : this.cached_item;
        }

        public void addChildEntry (CallContext context, Entry child_entry) {
            this.child_entries.add(child_entry);
        }

        public int getPathLength1(CallContext context) {
            return this.path_length1;
        }

        public int getPathLength2(CallContext context) {
            return this.path_length2;
        }

        public Type getIndexType1(CallContext context) {
            return this.actual_item != null ? this.index1 : this.cached_index1;
        }

        public Type getIndexType2(CallContext context) {
            return this.actual_item != null ? this.index2 : this.cached_index2;
        }

        public void update (CallContext context) {
            CustomaryContext cc = (((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) ? CustomaryContext.create((Context) context) : null);

            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "update Entry in OOMapImpl_DataConverter_Type_Type_: current path 1 length %(pathlen1), current path 2 length %(pathlen2)", "pathlen1", t.s(this.path_length1), "pathlen2", t.s(this.path_length2)); }
            if (this.actual_item != null) {
                if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "update: no need, got actual entry "); }
                return;
            }
            boolean changed = false;
            for (int i=0; i < this.parent_entries.size(); i++) {
                Entry parent_entry = (Entry) this.parent_entries.elementAt(i);
                DataConverter parent_item = parent_entry.getItem(context);
                if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) {
                    Object[][] attributes = { { "number", t.s(i) }, { "pathlen1", t.s(this.path_length1) }, { "pathlen2", t.s(this.path_length2) }, { "index1", this.index1 == null ? null : this.index1.getName(context) }, { "index2", this.index2 == null ? null : this.index2.getName(context) }, { "cachedindex1", this.cached_index1 == null ? null : this.cached_index1.getName(context) }, { "cachedindex2", this.cached_index2 == null ? null : this.cached_index2.getName(context) }, { "parentpathlen1", t.s(parent_entry.getPathLength1(context)) }, { "parentpathlen2", t.s(parent_entry.getPathLength2(context)) }, { "parentindex1", parent_entry.getIndexType1(context) }, { "parentindex2", parent_entry.getIndexType2(context) } };
                    cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "update: parent #%(number), path len 1 '%(pathlen1)', path len 2 '%(pathlen2)', index 1 '%(index1)', index 2 '%(index2)', cached index 1 '%(cachedindex1)', cached index 2 '%(cachedindex2)', parent path len 1 '%(parentpathlen1)', parent path len 2 '%(parentpathlen2)', parent index 1 '%(parentindex1)', parent index 2 '%(parentindex2)'", attributes);
                }
                if (parent_item != null) {
                    if (    this.cached_item != parent_item
                         && (    this.cached_item == null
                              || (    this.path_length1 > parent_entry.getPathLength1(context) + 1
                                   && (this.cached_index1 == null || ! this.cached_index1.isA(context, parent_entry.getIndexType1(context)))
                                 )
                              || (    this.cached_index1 != null && this.cached_index1 != parent_entry.getIndexType1(context) && parent_entry.getIndexType1(context).isA(context, this.cached_index1))
                              || (    (    this.index1 == parent_entry.getIndexType1(context)
                                        || this.path_length1 == parent_entry.getPathLength1(context) + 1
                                      )
                                   && (    (    this.path_length2 > parent_entry.getPathLength2(context) + 1
                                             && (this.cached_index2 == null || ! this.cached_index2.isA(context, parent_entry.getIndexType2(context)))
                                           )
                                        || (    this.cached_index2 != null && this.cached_index2 != parent_entry.getIndexType2(context) && parent_entry.getIndexType2(context).isA(context, this.cached_index2))
                                      )
                                 )
                            )
                       ) {
                        if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "update: parent #%(number) cached, new path 1 length %(pathlen1), new path 2 length %(pathlen2)", "number", t.s(i), "pathlen1", t.s(parent_entry.getPathLength1(context) + 1), "pathlen2", t.s(parent_entry.getPathLength2(context) + 1)); }
                        this.cached_item   = parent_item;
                        this.path_length1  = parent_entry.getPathLength1(context) + (this.index1 != parent_entry.getIndexType1(context) ? 1 : 0);
                        this.path_length2  = parent_entry.getPathLength2(context) + (this.index2 != parent_entry.getIndexType2(context) ? 1 : 0);
                        this.cached_index1 = parent_entry.getIndexType1(context);
                        this.cached_index2 = parent_entry.getIndexType2(context);
                        changed = true;
                    }
                }
            }
            if (changed) {
                this.updateChildEntries(context);
            }
        }

        public void updateChildEntries (CallContext context) {
            for (int i=0; i < this.child_entries.size(); i++) {
                ((Entry) this.child_entries.elementAt(i)).update(context);
            }
        }
    }

    public OOMapImpl_DataConverter_Type_Type_ (CallContext context)
    {
        map1 = new java.util.Hashtable ();
    }

    public OOMapImpl_DataConverter_Type_Type_ (CallContext context, java.util.Hashtable map1)
    {
        this.map1 = map1;
    }

    private Entry _getEntry  (CallContext context, Type index1, Type index2)
    {
        CustomaryContext cc = (((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) ? CustomaryContext.create((Context) context) : null);

        if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "get Entry in OOMapImpl_DataConverter_Type_Type_ : (%(index1),%(index2))", "index1", index1, "index2", index2); }
        java.util.Hashtable map2 = (java.util.Hashtable) map1.get(index1);
        if (map2 == null) {
            if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "get Entry in OOMapImpl_DataConverter_Type_Type_ : (%(index1),%(index2)): not in map1, creating new Hashtable...", "index1", index1, "index2", index2); }
            map2 = new java.util.Hashtable();
            map1.put(index1, map2);
        }
        Entry entry = (Entry) map2.get(index2);
        if (entry == null) {
            if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "get Entry in OOMapImpl_DataConverter_Type_Type_ : (%(index1),%(index2)): not in map2, creating new Entry...", "index1", index1, "index2", index2); }
            entry = new Entry (context, this, index1, index2);
            map2.put(index2, entry);
        }
        if ((this.notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "got Entry in OOMapImpl_DataConverter_Type_Type_ : (%(index1),%(index2)): %(entry)", "index1", index1, "index2", index2, "entry", entry); }
        return entry;
    }

    public DataConverter get     (CallContext context, Type index1, Type index2) throws DoesNotExist
    {
        DataConverter item = this.tryGet(context, index1, index2);
        if (item == null) DoesNotExist.createAndThrow(context);
        return item;
    }

    public DataConverter tryGet  (CallContext context, Type index1, Type index2)
    {
        return this._getEntry(context, index1, index2).getItem(context);
    }

    public boolean  canGet  (CallContext context, Type index1, Type index2)
    {
        if (this.tryGet(context, index1, index2) == null) return false;
        return true;
    }

    public void     set     (CallContext context, Type index1, Type index2, DataConverter item)
    {
        Entry entry = this._getEntry(context, index1, index2);
        entry.setItem(context, item);
    }

    public void     add     (CallContext context, Type index1, Type index2, DataConverter item) throws AlreadyExists
    {
        Entry entry = this._getEntry(context, index1, index2);
        if (entry.isActualEntry()) AlreadyExists.createAndThrow (context);
        entry.setItem(context, item);
    }

    public void     replace (CallContext context, Type index1, Type index2, DataConverter item) throws DoesNotExist
    {
        Entry entry = this._getEntry(context, index1, index2);
        if (!entry.isActualEntry()) DoesNotExist.createAndThrow (context);
        entry.setItem(context, item);
    }

    public void     unset   (CallContext context, Type index1, Type index2)
    {
        Entry entry = this._getEntry(context, index1, index2);
        entry.setItem(context, null);
    }

    public void     remove  (CallContext context, Type index1, Type index2) throws DoesNotExist
    {
        Entry entry = this._getEntry(context, index1, index2);
        if (!entry.isActualEntry()) DoesNotExist.createAndThrow (context);
        entry.setItem(context, null);
    }

    public boolean canGetExactMatch (CallContext context, Type index1, Type index2) {
        return this._getEntry(context, index1, index2).isActualEntry();
    }
}
