package com.sphenon.basics.data.classes;

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
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.locating.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.retriever.*;

import com.sphenon.basics.data.*;
import com.sphenon.basics.data.tplinst.*;

import java.util.Map;
import java.util.HashMap;

public class Class_DataSlot<T> 
             implements DataSlot<T>,
                        DataSink_WithChoiceSet
             {

    protected T value;

    public Class_DataSlot(CallContext context, T value) {
        this.value = value;
    }
     
    public Object getObject(CallContext context) {
        return get(context);
    }

    public void setObject(CallContext context, Object data) {
        set(context, (T) data);
    }

    public T get(CallContext context) {
        return this.value;
    }

    public void set(CallContext context, T value) {
        this.value = value;
    }

    protected Retriever choice_set_retriever;

    public Retriever getChoiceSetRetriever (CallContext context) {
        return this.choice_set_retriever;
    }

    public void setChoiceSetRetriever (CallContext context, Retriever choice_set_retriever) {
        this.choice_set_retriever = choice_set_retriever;
    }
}
