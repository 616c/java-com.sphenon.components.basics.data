// instantiated with jti.pl from OOMap
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

