package com.sphenon.basics.data;

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
import com.sphenon.basics.many.tplinst.*;

public interface DataSink_WithInteractionRestrictions {
    public boolean disableCreate(CallContext context);
    public boolean disableSelect(CallContext context);
    public boolean disableEdit(CallContext context);
    public boolean disableDelete(CallContext context); // i.e., removal from collections, possibly including deletion depending on cascading behaviour
    public boolean disableReorder(CallContext context);
}
