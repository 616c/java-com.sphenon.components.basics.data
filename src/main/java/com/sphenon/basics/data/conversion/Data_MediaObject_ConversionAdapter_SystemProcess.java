package com.sphenon.basics.data.conversion;

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
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.message.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.locating.*;
import com.sphenon.basics.locating.returncodes.*;

import com.sphenon.basics.data.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.security.*;
import com.sphenon.basics.aspects.*;
import com.sphenon.basics.variatives.*;

import java.io.*;
import java.util.regex.*;
import java.util.Map;
import java.util.Hashtable;

public class Data_MediaObject_ConversionAdapter_SystemProcess implements Data_MediaObject {
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.data.conversion.Data_MediaObject_ConversionAdapter_SystemProcess"); };

    protected Data_MediaObject     source_data;
    protected String               conversion_command;
    protected TypeImpl_MediaObject target_type;
    protected RegularExpression    filename_substitution;
    protected String               filename_substitution_regexp;
    protected String               filename_substitution_subst;
    protected String               disposition_filename;
    protected Map                  arguments;
    protected RegularExpression    parrep = new RegularExpression("([^\\$]*(?:\\$+[^\\{\\$][^\\$]*)*)(?:\\$\\{([^\\}]+)\\})?");
    protected RegularExpression    grantre = new RegularExpression("grant\\((#?)([A-Za-z0-9_-]+)\\)\\?([^:]*):(.*)");
    protected RegularExpression    pardefrep = new RegularExpression("([^:!]+)(?:(?:(:)(.*))|(?:(!)(.*)))?");
    protected RegularExpression    ctnre = new RegularExpression("((?:ctn|oorl):[^,]+),((?:ctn|oorl):[^,]+),(.*)");
    protected RegularExpression    stringre = new RegularExpression("string:(.*)");
    protected RegularExpression    jsre = new RegularExpression("js:(.*)");

    public Data_MediaObject_ConversionAdapter_SystemProcess (CallContext context, Data_MediaObject source_data, String conversion_command, TypeImpl_MediaObject target_type, String filename_substitution_regexp, String filename_substitution_subst, Map arguments) {
        this.source_data                  = source_data;
        this.conversion_command           = conversion_command;
        this.target_type                  = target_type;
        this.arguments                    = arguments;
        this.filename_substitution_regexp = filename_substitution_regexp;
        this.filename_substitution_subst  = filename_substitution_subst;
    }

    public Type getDataType(CallContext context) {
        return target_type;
    }

    public String getMediaType(CallContext context) {
        return target_type.getMediaType(context);
    }

    public String getDispositionFilename(CallContext call_context) {
        Context context = Context.create(call_context);
        CustomaryContext cc = CustomaryContext.create(context);

        if (this.disposition_filename == null) {
            String fnsub = replaceParameters(context, filename_substitution_subst);

            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Deriving filename: source name '%(sourcename)', regexp '%(regexp)', subexp '%(subexp)', subexp2 '%(subexp2)'", "sourcename", source_data.getDispositionFilename(context), "regexp", filename_substitution_regexp, "subexp", filename_substitution_subst, "subexp2", fnsub); }

            this.filename_substitution = new RegularExpression(context, filename_substitution_regexp, fnsub);
            this.disposition_filename = filename_substitution.replaceFirst(context, source_data.getDispositionFilename(context));

            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "Deriving filename: result '%(result)'", "result", this.disposition_filename); }
        }
        return this.disposition_filename;
    }

    public java.util.Date getLastUpdate(CallContext call_context) {
        return source_data.getLastUpdate(call_context);
    }

    protected Authority authority = null;
    protected Authority getAuthority(CallContext context) {
        if (this.authority == null) {
            this.authority = SecuritySessionData.get(context).getAuthority(context);
        }
        return this.authority;
    }

    protected String replaceParameters(CallContext call_context, String text) {
        Context context = Context.create(call_context);
        CustomaryContext cc = CustomaryContext.create(context);

        String isolang = com.sphenon.basics.variatives.classes.StringPoolAccessor.getLanguage(context).split("[,+]")[0];
        String result = "";
        Matcher m = parrep.getMatcher(context, text);
        Matcher m2;
        while (m.find()) {
            String g1 = m.group(1);
            String g2 = m.group(2);
            if ((g1 == null || g1.length() == 0) && (g2 == null || g2.length() == 0)) {
                break;
            }
            result += m.group(1);
            Hashtable parameters = null;
            if (g2 != null) {
                if (g2.equals("lang")) {
                    result += (isolang == null ? "en" : isolang);
                } else if ((m2 = grantre.getMatcher(context, g2)).matches()) {
                    boolean is_resource = (m2.group(1) != null && m2.group(1).equals("#"));
                    String resource_id = (is_resource ? m2.group(2) : null);
                    String security_class = (is_resource ? null : m2.group(2));
                    String parameter1 = m2.group(3);
                    String parameter2 = m2.group(4);
                    result += (getAuthority(context).isAccessGranted (context, resource_id, security_class, AccessType.READ) ? parameter1 : parameter2);
                } else if ((m2 = ctnre.getMatcher(context, g2)).matches()) {
                    String ctn = m2.group(1);
                    String base = m2.group(2);
                    String type = m2.group(3);
                    result += com.sphenon.basics.locating.Locator.tryGetTextLocatorValue(context, ctn, base, type);
                } else if ((m2 = stringre.getMatcher(context, g2)).matches()) {
                    String id = m2.group(1);
                    result += StringContext.get((Context) context).getString(context, id).replace("_","__").replace(" ","_");
                } else if ((m2 = jsre.getMatcher(context, g2)).matches()) {
                    String expression = m2.group(1);
                    if (parameters == null) {
                        parameters = new Hashtable();
                        parameters.put("source_data", source_data);
                    }
                    result += Configuration.evaluateJavaScript(context, expression, parameters);
                } else {
                    (m2 = pardefrep.getMatcher(context, g2)).matches(); // matched immer
                    String val = arguments == null ? null : ((String) arguments.get(m2.group(1)));
                    if (val == null) {
                        if (m2.group(2) != null && m2.group(2).equals(":")) {
                            val = m2.group(3);
                            if (val == null) {
                                val = "";
                            }
                        } else if (m2.group(4) != null && m2.group(4).equals("!")) {
                            String g3 = m2.group(5);
                            Matcher m3;
                            if ((m3 = ctnre.getMatcher(context, g3)).matches()) {
                                String ctn = m3.group(1);
                                String base = m3.group(2);
                                String type = m3.group(3);
                                val = com.sphenon.basics.locating.Locator.tryGetTextLocatorValue(context, ctn, base, type);
                            } else if ((m3 = stringre.getMatcher(context, g3)).matches()) {
                                String id = m3.group(1);
                                val = StringContext.get((Context) context).getString(context, id);
                            } else if ((m3 = jsre.getMatcher(context, g3)).matches()) {
                                String expression = m3.group(1);
                                if (parameters == null) {
                                    parameters = new Hashtable();
                                    parameters.put("source_data", source_data);
                                }
                                val = Configuration.evaluateJavaScript(context, expression, parameters);
                            } else {
                                cc.throwConfigurationError(context, "Default value for parameter '%(parname)' for external conversion process '%(command)' is not recognised", "command", conversion_command, "parname", g2);
                                throw (ExceptionConfigurationError) null; // compiler insists
                            }
                        } else {
                            cc.throwConfigurationError(context, "Parameter '%(parname)' for external conversion process '%(command)' is not available", "command", conversion_command, "parname", g2);
                            throw (ExceptionConfigurationError) null; // compiler insists
                        }
                    }
                    result += val;
                }
            }
        }
        return result;
    }

    protected class StreamConverter extends PipedInputStream {
        protected Context context;
        protected CustomaryContext cc;

        protected String convcmd;

        protected java.lang.Process process;
        protected Thread            process_listener;
        protected Thread            process_supplier;

        protected InputStream       source_is;;
        protected InputStream       process_stdout;
        protected InputStream       process_stderr;
        protected OutputStream      process_stdin;
        protected int               exit_value;
        protected String            err;

        protected PipedOutputStream out;

        protected File              cache_file;
        protected FileOutputStream  cache_out;

        public StreamConverter (CallContext call_context, File cache_file_par) {
            context = Context.create(call_context);
            cc = CustomaryContext.create(context);

            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "creating StreamConverter..."); }

            this.cache_file = cache_file_par;
            if (this.cache_file != null) {
                if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "StreamConverter, converting and writing to cache..."); }
                if (! this.cache_file.getParentFile().exists() && ! this.cache_file.getParentFile().mkdirs()) {
                    if ((notification_level & Notifier.MONITORING) != 0) { cc.sendSevereWarning(context, "StreamConverter, cache is not working (could not create parent directories of cache file), proceeding, but without caching; this may decrease performance significiantly"); }                    
                    this.cache_out = null;
                } else {
                    if (this.cache_file.exists() && ! this.cache_file.delete()) {
                        if ((notification_level & Notifier.MONITORING) != 0) { cc.sendSevereWarning(context, "StreamConverter, cache is not working (could not delete previous version of cache file), proceeding, but without caching; this may decrease performance significiantly"); }                    
                        this.cache_out = null;
                    } else {
                        try {
                            this.cache_out = new FileOutputStream(this.cache_file);
                        } catch (java.io.FileNotFoundException fnfe) {
                            if ((notification_level & Notifier.MONITORING) != 0) { cc.sendSevereWarning(context, "StreamConverter, cache is not working (could not open cache file [reason: '%(reason)']), proceeding, but without caching; this may decrease performance significiantly", "reason", fnfe); }
                            this.cache_out = null;
                        }
                    }
                }
            } else {
                this.cache_out = null;
            }

            try {
                out = new PipedOutputStream(this);
            } catch (java.io.IOException ioe) {
                cc.throwAssertionProvedFalse(context, ioe, "Piped Output Stream could not be created");
                throw (ExceptionAssertionProvedFalse) null; // compiler insists
            }
            
            convcmd = replaceParameters(context, conversion_command);

            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "StreamConverter, starting process '%(command)'...", "command", convcmd); }

            try {
                this.process = java.lang.Runtime.getRuntime().exec(convcmd, null, new java.io.File(Locator.createLocator(context, "ctn://Space/temporary").tryGetTextLocatorValue(context, Locator.createLocator(context, "ctn://Space/host/file_system"), "File")));
            } catch (InvalidLocator il) {
                cc.throwConfigurationError(context, il, "External conversion process '%(command)' could not be started", "command", conversion_command);
                throw (ExceptionConfigurationError) null; // compiler insists
            } catch (java.io.IOException ioe) {
                cc.throwConfigurationError(context, ioe, "External conversion process '%(command)' could not be started", "command", conversion_command);
                throw (ExceptionConfigurationError) null; // compiler insists
            }

            this.process_stdout = process.getInputStream();
            this.process_stderr = process.getErrorStream();
            this.process_stdin = this.process.getOutputStream();
            this.source_is = source_data.getStream(context);
            this.err = "";

            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "StreamConverter, creating listener thread..."); }

            this.process_listener = new Thread() {
                    public void run () {
                        try {
                            byte [] buf = new byte[1];
                            do {
                                sleep(500);
                                while (process_stdout.available() > 0) {
                                    process_stdout.read(buf, 0, 1);
                                    out.write(buf, 0, 1);
                                    if (cache_out != null) {
                                        cache_out.write(buf, 0, 1);
                                    }
                                }
                                while (process_stderr.available() > 0) {
                                    process_stderr.read(buf, 0 ,1);
                                    if ((notification_level & Notifier.VERBOSE) != 0) {
                                        if (buf[0] == '\n') {
                                            cc.sendTrace(context, Notifier.VERBOSE, "stderr: %(line)", "line", err);
                                            err = "";
                                        } else {
                                            err += (char) buf[0];
                                        }
                                    }
                                }
                                try {
                                    exit_value = process.exitValue();
                                    process_stdout.close();
                                    process_stderr.close();
                                    if (exit_value != 0) {
                                        if ((notification_level & Notifier.PRODUCTION) != 0) { cc.sendError(context, "data conversion failed, data converter exit value: %(exitvalue), command '%(command)'", "exitvalue", t.s(exit_value), "command", convcmd); }
                                    } else {
                                        if ((notification_level & Notifier.VERBOSE) != 0) {
                                            if (err.length() != 0) {
                                                cc.sendTrace(context, Notifier.VERBOSE, "data converter stderr: %(line)", "line", err);
                                            }
                                            cc.sendTrace(context, Notifier.VERBOSE, "data converter exit value: %(exitvalue)", "exitvalue", t.s(exit_value));
                                        }
                                    }
                                    return;
                                } catch (IllegalThreadStateException itse) {
                                    continue;
                                }
                            } while (true);
                        } catch (java.io.IOException ioe) {
                            cc.sendError(context, "data converter listener thread terminated unsuccessfully: %(reason)", "reason", ioe);
                        } catch (java.lang.InterruptedException ie) {
                            cc.sendError(context, "data converter listener thread terminated unsuccessfully: %(reason)", "reason", ie);
                        } finally {
                            try {
                                out.close();
                                if (cache_out != null) {
                                    cache_out.close();
                                    if (exit_value != 0 && cache_file.exists() && ! cache_file.delete()) {
                                        if ((notification_level & Notifier.MONITORING) != 0) { cc.sendSevereWarning(context, "StreamConverter, cache is not working (could not delete cache file after conversion error occured), proceeding, but without caching; this may decrease performance significiantly"); }                    
                                    }
                                }
                            } catch (java.io.IOException ioe) {
                                cc.sendError(context, "data converter listener thread terminated (rather, somehow, not completely) unsuccessfully (during cleanup closing of out file): %(reason)", "reason", ioe);
                            }
                            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "StreamConverter, listener thread terminated."); }
                        }
                    };
                };

            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "StreamConverter, starting listener thread..."); }

            process_listener.start();

            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "StreamConverter, creating supplier thread..."); }

            this.process_supplier = new Thread() {
                    public void run () {
                        long count = 0;
                        try {
                            int c;
                            while ((c = source_is.read()) != -1) {
                                process_stdin.write(c);
                                count++;
                            }
                            process_stdin.close();
                            source_is.close();
                        } catch (java.io.IOException ioe) {
                            cc.sendError(context, "data converter supplier thread terminated unsuccessfully (%(count) characters transmitted): %(reason)", "count", t.o(count), "reason", ioe);
                        }
                        if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "StreamConverter, supplier thread terminated (%(count) characters transmitted).", "count", t.o(count)); }
                    };
                };

            if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "StreamConverter, starting supplier thread..."); }

            this.process_supplier.start();
        }
    }

    public java.io.InputStream getInputStream(CallContext call_context) {
        Context context = Context.create(call_context);
        CustomaryContext cc = CustomaryContext.create(context);

        String cachef = DataPackageInitialiser.getConfiguration(context).get(context, "conversion.CacheFolder", (String) null);
        File cache_file = null;
        if (cachef != null) {
            Locator origin = this.source_data.tryGetOrigin(context);
            if (origin != null) {
                String cachefilename = cachef + "/" + encode(context, origin.getTextLocator(context));
                if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "StreamConverter, checking cache entry '%(entry)'...", "entry", cachefilename); }

                // type
                String tt = encode(context, this.target_type.getName(context));

                // file name
                String dfn = encode(context, this.getDispositionFilename(context));

                // lang
                String isolang = com.sphenon.basics.variatives.classes.StringPoolAccessor.getLanguage(context);
                if (isolang != null) { isolang = encode(context, isolang); }

                // security
                String security_identifier = getAuthority(context).getUnambiguousSecurityIdentifier(context);
                long lmosd = getAuthority(context).getLastModificationOfUserPermissions(context);

                // aspects
                AspectsContext ac = AspectsContext.get((Context) context);
                String aspects = ac.getAspect(context).getName(context);
                if (aspects != null) { aspects = encode(context, aspects); }

                cache_file = new File(cachefilename + "/" + tt + "-" + dfn + (isolang == null ? "" : ("-HL-" + isolang)) + (security_identifier == null ? "" : ("-SI-" + security_identifier)) + (aspects == null ? "" : ("-AS-" + aspects)));

                if (cache_file.exists() && cache_file.lastModified() > this.source_data.getLastUpdate(context).getTime() && lmosd != -1 && cache_file.lastModified() > lmosd) {
                    if ((notification_level & Notifier.SELF_DIAGNOSTICS) != 0) { cc.sendTrace(context, Notifier.SELF_DIAGNOSTICS, "StreamConverter, cache entry is valid, using it..."); }
                    try {
                        return new FileInputStream(cache_file);
                    } catch (java.io.FileNotFoundException fnfe) {
                        cc.throwImpossibleState(context, fnfe, "'exists' method reports 'true' for '%(filename)', but opening for read fails with 'file not found'", "filename", cache_file.getName());
                        throw (ExceptionImpossibleState) null; // compiler insists
                    }
                }
            }
        }                

        return new StreamConverter(context, cache_file);
    }

    public java.io.InputStream getStream(CallContext context) {
        return this.getInputStream(context);
    }

    public java.io.OutputStream getOutputStream(CallContext context) {
        CustomaryContext.create((Context)context).throwLimitation(context, "Data_MediaObject_ConversionAdapter_SystemProcess is not writable");
        throw (ExceptionLimitation) null; // compilernsists
    }

    final static String[] hex =
    {
        "00", "01", "02", "03", "04", "05", "06", "07",
        "08", "09", "0A", "0B", "0C", "0D", "0E", "0F",
        "10", "11", "12", "13", "14", "15", "16", "17",
        "18", "19", "1A", "1B", "1C", "1D", "1E", "1F",
        "20", "21", "22", "23", "24", "25", "26", "27",
        "28", "29", "2A", "2B", "2C", "2D", "2E", "2F",
        "30", "31", "32", "33", "34", "35", "36", "37",
        "38", "39", "3A", "3B", "3C", "3D", "3E", "3F",
        "40", "41", "42", "43", "44", "45", "46", "47",
        "48", "49", "4A", "4B", "4C", "4D", "4E", "4F",
        "50", "51", "52", "53", "54", "55", "56", "57",
        "58", "59", "5A", "5B", "5C", "5D", "5E", "5F",
        "60", "61", "62", "63", "64", "65", "66", "67",
        "68", "69", "6A", "6B", "6C", "6D", "6E", "6F",
        "70", "71", "72", "73", "74", "75", "76", "77",
        "78", "79", "7A", "7B", "7C", "7D", "7E", "7F",
        "80", "81", "82", "83", "84", "85", "86", "87",
        "88", "89", "8A", "8B", "8C", "8D", "8E", "8F",
        "90", "91", "92", "93", "94", "95", "96", "97",
        "98", "99", "9A", "9B", "9C", "9D", "9E", "9F",
        "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7",
        "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF",
        "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7",
        "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF",
        "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7",
        "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF",
        "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7",
        "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF",
        "E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7",
        "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF",
        "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7",
        "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF"
    };

    static public String encode(CallContext call_context, String string) {
        byte[] bytes;
        try {
            bytes = string.getBytes("UTF-8");
        } catch (java.io.UnsupportedEncodingException uee) {
            Context context = Context.create(call_context);
            CustomaryContext cc = CustomaryContext.create(context);
            cc.throwLimitation(context, "System (VM) does not support UTF-8 encoding");
            throw (ExceptionLimitation) null;
        }
        StringBuffer new_string = new StringBuffer();
        int b;
        
        boolean was_slash = false;
        for (int i=0; i < bytes.length; i++) {
            b = bytes[i];
            if (b < 0) { b += 256; }

            if (    (b >= 'A' && b <= 'Z')
                 || (b >= 'a' && b <= 'z')
                 || (b >= '0' && b <= '9')
                 || (b == '/')
               )
            {
                if (was_slash && b == '/') {
                    new_string.append('_');
                }
                new_string.append((char)b);
            } else if (b == '\\') {
                new_string.append("__/");
            } else {
                new_string.append("_"+hex[b]);
            }
            was_slash = (b == '/');
        }
        return new_string.toString();
    }  

    public Locator tryGetOrigin(CallContext context) {
        return source_data.tryGetOrigin(context);
    }
}

