package com.sphenon.basics.variatives;

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
import com.sphenon.basics.message.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.variatives.*;
import com.sphenon.basics.variatives.tplinst.*;

public class StringContext extends SpecificContext {

    static protected StringContext default_singleton;
    protected boolean is_default_singelton;

    static public StringContext get(Context context) {
        StringContext string_context = (StringContext) context.getSpecificContext(StringContext.class);
        if (string_context != null) {
            return string_context;
        }
        return default_singleton == null ? (default_singleton = new StringContext(context, true)) : default_singleton;
    }

    static public StringContext create(Context context) {
        StringContext string_context = new StringContext(context, false);
        context.setSpecificContext(StringContext.class, string_context);
        return string_context;
    }

    protected StringContext (Context context, boolean is_default_singelton) {
        super(context);
        this.is_default_singelton = is_default_singelton;
        this.string_pool = null;
    }

    protected StringPool string_pool;

    public void setStringPool(CallContext context, StringPool string_pool) {
        if (is_default_singelton) {
            CustomaryContext.create((Context) context).throwPreConditionViolation(context, "Cannot modify default singelton StringContext");
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }
        this.string_pool = string_pool;
    }

    public StringPool getStringPool(CallContext cc) {
        if (is_default_singelton) { return null; }
        StringContext string_context;
        return (this.string_pool != null ?
                     this.string_pool
                  : (string_context = (StringContext) this.getCallContext(StringContext.class)) != null ?
                       string_context.getStringPool(cc)
                     : null
               );
    }

    public String getString(CallContext cc, String id) {
        StringPool pool = this.getStringPool(cc);
        if (pool == null) { return null; }
        return pool.getString(cc, id);
    }

    public String getString(CallContext cc, String id, String isolang) {
        StringPool pool = this.getStringPool(cc);
        if (pool == null) { return null; }
        return pool.getString(cc, id, isolang);
    }
}
