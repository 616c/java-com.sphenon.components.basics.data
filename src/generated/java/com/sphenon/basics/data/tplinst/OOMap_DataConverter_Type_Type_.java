// instantiated with jti.pl from OOMap

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
// please do not modify this file directly
package com.sphenon.basics.data.tplinst;

import com.sphenon.basics.data.*;
import com.sphenon.basics.data.*;
import com.sphenon.basics.data.conversion.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.tplinst.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;

public interface OOMap_DataConverter_Type_Type_
{
    public DataConverter      get     (CallContext context, Type index1, Type index2) throws DoesNotExist;
    public DataConverter      tryGet  (CallContext context, Type index1, Type index2);
    public boolean       canGet  (CallContext context, Type index1, Type index2);

    public void          set     (CallContext context, Type index1, Type index2, DataConverter item);
    public void          add     (CallContext context, Type index1, Type index2, DataConverter item) throws AlreadyExists;
    public void          replace (CallContext context, Type index1, Type index2, DataConverter item) throws DoesNotExist;
    public void          unset   (CallContext context, Type index1, Type index2);
    public void          remove  (CallContext context, Type index1, Type index2) throws DoesNotExist;

    public boolean       canGetExactMatch (CallContext context, Type index1, Type index2);
}

