package com.sphenon.basics.variatives.classes;

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
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.variatives.*;

public class StringPoolConfiguration extends StringPoolAccessor {

    private Configuration config;
    
    protected StringPoolConfiguration (CallContext cc, String prefix) {
    	setConfigurationPrefix(cc, prefix);
    }

    protected StringPoolConfiguration (CallContext cc) {
    }

    static public StringPoolConfiguration create (CallContext cc, String prefix) {
        return new StringPoolConfiguration (cc, prefix);
    }

    static public StringPoolConfiguration precreate (CallContext cc) {
        return new StringPoolConfiguration (cc);
    }
    
    public void setConfigurationPrefix (CallContext cc, String prefix) {
        this.config = Configuration.create(cc, prefix);
    }

    public String getConfigurationPrefix(CallContext cc) {
        return this.config.getClientId(cc);
    }

    public String getStringFromDataPool (CallContext cc, String key) {
        return (String) config.get(cc, key, (String) null);
    }
}
