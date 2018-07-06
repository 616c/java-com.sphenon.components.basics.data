package com.sphenon.basics.data.classes;

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

import com.sphenon.basics.data.*;

import java.util.Vector;
import java.io.*;

public class SimpleSheet {

    protected String name;

    public SimpleSheet(CallContext context, String name) {
        this.name = name;
        this.tables = new Vector<SimpleTable>();
    }

    protected Vector<SimpleTable> tables;

    public Vector<SimpleTable> getTables(CallContext context) {
        return this.tables;
    }

    public SimpleTable addTable(CallContext context, String table_name, String... column_names) {
        SimpleTable table = new SimpleTable(context, table_name, column_names);
        this.tables.add(table);
        return table;
    }

    public void writeCSV(CallContext context, String folder_name) {
        for (SimpleTable table : this.tables) {
            table.writeCSV(context, folder_name + "/" + this.name);
        }
    }

    public void writeODS(CallContext context, String folder_name) {
        try {
            File f = new File(folder_name + "/" + this.name);
            f.mkdirs();

            {
                FileOutputStream fos = new FileOutputStream(new File(f, "content.xml"));
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                BufferedWriter bw = new BufferedWriter(osw);
                PrintWriter pw = new PrintWriter(bw);

                pw.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\" xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\" xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\" xmlns:presentation=\"urn:oasis:names:tc:opendocument:xmlns:presentation:1.0\" xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\" xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" xmlns:math=\"http://www.w3.org/1998/Math/MathML\" xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\" xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\" xmlns:ooow=\"http://openoffice.org/2004/writer\" xmlns:oooc=\"http://openoffice.org/2004/calc\" xmlns:dom=\"http://www.w3.org/2001/xml-events\" xmlns:xforms=\"http://www.w3.org/2002/xforms\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" office:version=\"1.0\">\n  <office:scripts/>\n  <office:font-face-decls>\n    <style:font-face style:name=\"Albany AMT\" svg:font-family=\"&apos;Albany AMT&apos;\" style:font-family-generic=\"swiss\" style:font-pitch=\"variable\"/>\n    <style:font-face style:name=\"DejaVu Sans\" svg:font-family=\"&apos;DejaVu Sans&apos;\" style:font-family-generic=\"system\" style:font-pitch=\"variable\"/>\n    <style:font-face style:name=\"Lucidasans\" svg:font-family=\"Lucidasans\" style:font-family-generic=\"system\" style:font-pitch=\"variable\"/>\n  </office:font-face-decls>\n  <office:automatic-styles>\n    <style:style style:name=\"cellnormal\" style:family=\"table-cell\" style:parent-style-name=\"Default\">\n      <style:text-properties fo:font-size=\"10pt\" style:font-size-asian=\"10pt\" style:font-size-complex=\"10pt\"/>\n    </style:style>\n    <style:style style:name=\"cellprices\" style:family=\"table-cell\" style:parent-style-name=\"Default\">\n      <style:text-properties fo:color=\"#e67814\" fo:font-size=\"10pt\" style:font-size-asian=\"10pt\" style:font-size-complex=\"10pt\"/>\n    </style:style>\n    <style:style style:name=\"cellprice\" style:family=\"table-cell\" style:parent-style-name=\"Default\">\n      <style:text-properties fo:color=\"#0000ff\" fo:font-size=\"10pt\" fo:font-weight=\"bold\" style:font-size-asian=\"10pt\" style:font-weight-asian=\"bold\" style:font-size-complex=\"10pt\" style:font-weight-complex=\"bold\"/>\n    </style:style>\n    <style:style style:name=\"cellgrey\" style:family=\"table-cell\" style:parent-style-name=\"Default\">\n      <style:text-properties fo:color=\"#cccccc\" fo:font-size=\"10pt\" style:font-size-asian=\"10pt\" style:font-size-complex=\"10pt\"/>\n    </style:style>\n    <style:style style:name=\"cellsttp\" style:family=\"table-cell\" style:parent-style-name=\"Default\">\n      <style:table-cell-properties fo:background-color=\"#e6f28b\"/>\n      <style:text-properties fo:font-size=\"10pt\" style:font-size-asian=\"10pt\" style:font-size-complex=\"10pt\"/>\n    </style:style>\n    <style:style style:name=\"cellsttpprices\" style:family=\"table-cell\" style:parent-style-name=\"Default\">\n      <style:table-cell-properties fo:background-color=\"#e6f28b\"/>\n      <style:text-properties fo:color=\"#e67814\" fo:font-size=\"10pt\" style:font-size-asian=\"10pt\" style:font-size-complex=\"10pt\"/>\n    </style:style>\n    <style:style style:name=\"cellpricecat\" style:family=\"table-cell\" style:parent-style-name=\"Default\">\n      <style:table-cell-properties fo:background-color=\"#ffff99\"/>\n      <style:text-properties fo:color=\"#0000ff\" fo:font-weight=\"bold\" style:font-weight-asian=\"bold\" style:font-weight-complex=\"bold\"/>\n    </style:style>\n    <style:style style:name=\"cellpricescat\" style:family=\"table-cell\" style:parent-style-name=\"Default\">\n      <style:table-cell-properties fo:background-color=\"transparent\"/>\n      <style:text-properties fo:color=\"#33a3a3\"/>\n    </style:style>\n  </office:automatic-styles>\n  <office:body>\n    <office:spreadsheet>\n");

                for (SimpleTable table : this.tables) {
                    table.writeODS(context, pw);
                }

                pw.print("    </office:spreadsheet>\n  </office:body>\n</office:document-content>\n");

                pw.close();
                bw.close();
                osw.close();
                fos.close();
            }

            {
                File mif = new File(f, "META-INF");
                mif.mkdirs();
                FileOutputStream fos = new FileOutputStream(new File(mif, "manifest.xml"));
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                BufferedWriter bw = new BufferedWriter(osw);
                PrintWriter pw = new PrintWriter(bw);

                pw.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<manifest:manifest xmlns:manifest=\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0\">\n <manifest:file-entry manifest:media-type=\"application/vnd.oasis.opendocument.spreadsheet\" manifest:full-path=\"/\"/>\n <manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"content.xml\"/>\n</manifest:manifest>\n");

                pw.close();
                bw.close();
                osw.close();
                fos.close();
            }

            {
                FileOutputStream fos = new FileOutputStream(new File(f, "mimetype"));
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                BufferedWriter bw = new BufferedWriter(osw);
                PrintWriter pw = new PrintWriter(bw);

                pw.print("application/vnd.oasis.opendocument.spreadsheet\n");

                pw.close();
                bw.close();
                osw.close();
                fos.close();
            }

        } catch (FileNotFoundException fnfe) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, fnfe, "Cannot write to file '%(filename)'", "filename", folder_name);
            throw (ExceptionPreConditionViolation) null; // compiler insists
        } catch (UnsupportedEncodingException uee) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, uee, "Cannot write to file '%(filename)'", "filename", folder_name);
            throw (ExceptionPreConditionViolation) null; // compiler insists
        } catch (IOException ioe) {
            CustomaryContext.create((Context)context).throwPreConditionViolation(context, ioe, "Cannot write to file '%(filename)'", "filename", folder_name);
            throw (ExceptionPreConditionViolation) null; // compiler insists
        }
    }
}
