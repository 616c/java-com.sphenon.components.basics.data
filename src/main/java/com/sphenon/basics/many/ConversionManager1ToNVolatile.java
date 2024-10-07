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
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;
import com.sphenon.basics.data.conversion.returncodes.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.debug.*;
import com.sphenon.basics.metadata.Type;

import java.util.List;

public class ConversionManager1ToNVolatile<T2,T1>
    implements ConversionManager1ToN<T2,T1>
{
    protected ConversionManager1ToNVolatile(CallContext context) {
    }

    static public<T2,T1> ConversionManager1ToNVolatile<T2,T1> get(CallContext context) {
        return new ConversionManager1ToNVolatile<T2,T1>(context);
    }

    public List<T1> convertToMany(CallContext context, T2 source_item, Object... additional_arguments) throws ConversionFailure {
        List<T1> result = null;
        result = ((Converter<T2,List<T1>>)(additional_arguments[0])).convert(context, source_item);
        return result;
    }
}
