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
import com.sphenon.basics.exception.*;
import com.sphenon.basics.customary.*;

import com.sphenon.basics.data.*;

import java.util.Vector;
import java.io.*;

public class SimpleTable {

    protected String name;
    protected String[] column_names;
    protected boolean[] summarise;
    protected Vector<String[][]> data;
    protected String[][] last_row;
    protected Integer[] summary;

    public SimpleTable(CallContext context, String name, String... column_names) {
        this.name = name;
        this.column_names = column_names;
        this.summary      = new Integer[column_names.length];
        this.summarise    = new boolean[column_names.length];
        for (int i=0; i<column_names.length; i++) {
            String cn = this.column_names[i];
            if (cn.charAt(cn.length()-1) == '+') {
                this.summarise[i] = true;
                this.summary[i] = 0;
                this.column_names[i] = cn.substring(0, cn.length()-1);
            }
        }
        this.data = new Vector<String[][]>();
    }

    public Integer[] getSummary(CallContext context) {
        return this.summary;
    }

    public int getNumberOfLines(CallContext context) {
        return data.size();
    }

    public void addRow(CallContext context, Object... row) {
        String[][] new_row = new String[row.length][];
        this.data.add(new_row);
        this.last_row = new_row;
        int c=0;
        for (Object cell : row) {
            String[] new_cell = new String[3];
            new_row[c] = new_cell;
            new_cell[0] = (cell == null ? "" : cell.toString());
            if (cell instanceof Integer) {
                if (this.summarise[c]) {
                    summary[c] += (Integer) cell;
                }
                new_cell[1] = "float";
            } else {
                if (new_cell[0].isEmpty() == false && new_cell[0].charAt(0) == '=') {
                    new_cell[1] = "formula";
                } else {
                    new_cell[1] = "string";
                }
            }
            c++;
        }
    }

    public void setStyleOfLastRow(CallContext context, String... row_style) {
        int c=0;
        for (String cell_style : row_style) {
            this.last_row[c++][2] = cell_style;
        }
    }

    public void appendToLastRow(CallContext context, Object... row) {
        String[][] new_row = new String[last_row.length + row.length][];
        this.data.set(this.data.size() - 1, new_row);
        int c=0;
        for (; c<last_row.length; c++) {
            new_row[c] = last_row[c];
        }
        for (Object cell : row) {
            String[] new_cell = new String[3];
            new_row[c] = new_cell;
            new_cell[0] = (cell == null ? "" : cell.toString());
            if (cell instanceof Integer) {
                if (this.summarise[c]) {
                    summary[c] += (Integer) cell;
                }
                new_cell[1] = "float";
            } else {
                new_cell[1] = "string";
            }
            c++;
        }
        this.last_row = new_row;
    }

    public void sumRow(CallContext context, Object... row) {
        String key = (String) row[0];

        String[][] row_to_update = null;

        for (String[][] data_row : this.data) {
            if (data_row[0][0].equals(key)) {
                row_to_update = data_row;
                break;
            }
        }

        if (row_to_update == null) {
            row_to_update = new String[row.length][];
            String[] new_cell = new String[3];
            row_to_update[0] = new_cell;
            new_cell[0] = key;
            new_cell[1] = "string";
            this.data.add(row_to_update);
            this.last_row = row_to_update;
        }

        int c=0;
        for (Object cell : row) {
            if (c > 0 && cell instanceof Integer) {
                if (row_to_update[c] == null) {
                    String[] new_cell = new String[3];
                    row_to_update[c] = new_cell;
                    new_cell[0] = "0";
                    new_cell[1] = "float";
                }
                row_to_update[c][0] = new Integer((row_to_update[c] == null || row_to_update[c][0] == null ? 0 : new Integer(row_to_update[c][0])) + (Integer) cell).toString();
            }
            if (cell instanceof Integer) {
                if (this.summarise[c]) {
                    summary[c] += (Integer) cell;
                }
            }
            c++;
        }
    }

    public void writeCSV(CallContext context, String folder_name) {
        try {
            File f = new File(folder_name);
            f.mkdirs();

            FileOutputStream fos = new FileOutputStream(new File(f, this.name + ".csv"));
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter(bw);

            boolean first = true;
            for (String column_name : this.column_names) {
                pw.print((first ? "" : ";") + "\"" + column_name + "\"");
                first = false;
            }
            pw.println("");

            for (String[][] row : this.data) {
                first = true;
                for (String[] cell : row) {
                    pw.print((first ? "" : ";"));
                    if (cell[1].equals("string")) {
                        pw.print("\"");
                    }
                    pw.print(cell[0]);
                    if (cell[1].equals("string")) {
                        pw.print("\"");
                    }
                    first = false;
                }
                pw.println("");
            }

            first = true;
            for (Integer sum : this.summary) {
                pw.print((first ? "" : ";") + "\"" + (sum == null ? "" : sum) + "\"");
                first = false;
            }
            pw.println("");

            pw.close();
            bw.close();
            osw.close();
            fos.close();

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

    public void writeODS(CallContext context, PrintWriter pw) {
        pw.println("      <table:table table:name=\"" + this.name + "\" table:print=\"false\">");
        pw.println("        <office:forms form:automatic-focus=\"false\" form:apply-design-mode=\"false\"/>");

        pw.println("        <table:table-row>");
        for (String column_name : this.column_names) {
            pw.println("          <table:table-cell office:value-type=\"string\">");
            pw.println("            <text:p>" + column_name + "</text:p>");
            pw.println("          </table:table-cell>");
        }
        pw.println("        </table:table-row>");

        for (String[][] row : this.data) {
            pw.println("        <table:table-row>");
            for (String[] cell : row) {
                pw.print("          <table:table-cell " + (cell[2] == null ? "" : ("table:style-name=\"" + cell[2] + "\" ")));
                if (cell[1].equals("formula")) {
                    pw.println("table:formula=\"oooc:" + cell[0] + "\" office:value-type=\"float\"/>");
                } else {
                    pw.println("office:value-type=\"" + cell[1] + "\"" + (cell[1].equals("float") ? (" office:value=\"" + cell[0] + "\"") : "") + ">");
                    pw.println("            <text:p>" + cell[0] + "</text:p>");
                    pw.println("          </table:table-cell>");
                }
            }
            pw.println("        </table:table-row>");
        }

        pw.println("        <table:table-row>");
        for (Integer sum : this.summary) {
            pw.println("          <table:table-cell office:value-type=\"float\" office:value=\"" + (sum == null ? "" : sum) + "\">");
            pw.println("            <text:p>" + (sum == null ? "" : sum) + "</text:p>");
            pw.println("          </table:table-cell>");
        }
        pw.println("        </table:table-row>");

        pw.print("      </table:table>\n");
    }
}
