package com.sphenon.basics.data;

/****************************************************************************
  Copyright 2001-2018 Sphenon GmbH

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
import com.sphenon.basics.customary.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.encoding.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.tplinst.*;
import com.sphenon.basics.variatives.*;
import com.sphenon.basics.variatives.tplinst.*;
import com.sphenon.basics.locating.*;
import com.sphenon.ad.adcore.*;
import java.util.Date;

public class Data_Object_Object implements Data_Object {
    static final public Class _class = Data_Object_Object.class;

    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(_class); };

    protected Data_Object_Object(CallContext context, Object object) {
        this.object = object;
    }

    static public Data_Object_Object create(CallContext context, Object object) {
        return new Data_Object_Object(context, object);
    }

    private Object object;
    private String instance_identifier;
    private Locator locator;
    private Type type;

    public Object getObject(CallContext context) {
        return this.object;
    }

    public Type getDataType(CallContext context) {
        if (this.type == null) {
            this.type = TypeManager.get(context, this.object);
        }
        return this.type;
    }

    public String getInstanceIdentifier(CallContext context) {
        if (this.instance_identifier == null) {
            this.instance_identifier = InstanceIdentifier.get(context, this.object);
        }
        return this.instance_identifier;
    }

    public Date getLastUpdate(CallContext context) {
        return new Date();
    }

    public Locator tryGetOrigin(CallContext context) {
        if (this.locator == null && this.object instanceof WithLocation) {
            Location l = ((WithLocation) this.object).tryGetLocation(context);
            if (l != null) {
                java.util.List<Locator> locators = l.getLocators(context);
                if (locators != null && locators.isEmpty() == false) {
                    this.locator = locators.get(0);
                }
            }
        }
        return this.locator;
    }
}
