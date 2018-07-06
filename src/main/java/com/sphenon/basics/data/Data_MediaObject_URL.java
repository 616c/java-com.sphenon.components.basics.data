package com.sphenon.basics.data;

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
import com.sphenon.basics.notification.*;
import com.sphenon.basics.metadata.*;
import com.sphenon.basics.metadata.tplinst.*;
import com.sphenon.basics.variatives.*;
import com.sphenon.basics.variatives.tplinst.*;
import com.sphenon.basics.locating.*;
import com.sphenon.basics.locating.factories.*;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Data_MediaObject_URL extends Data_MediaObject_Stream {

    protected String  url_string;

    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }

    static {
        CallContext context = com.sphenon.basics.context.classes.RootContext.getInitialisationContext();
        notification_level = NotificationLocationContext.getLevel(context, "com.sphenon.basics.data.Data_MediaObject_URL");
    }

    protected Data_MediaObject_URL(CallContext context, String url_string, Type type, String filename, String origin_locator, Date last_update) {
        super(context, (InputStream) null, type, filename, origin_locator, last_update);
        this.url_string = url_string;
    }

    static public Data_MediaObject_URL create(CallContext context, String url, Type type, String filename) {
        return new Data_MediaObject_URL(context, url, type, filename, null, null);
    }

    static public Data_MediaObject_URL create(CallContext context, String url, Type type, String filename, String origin_locator) {
        return new Data_MediaObject_URL(context, url, type, filename, origin_locator, null);
    }

    protected InputStream getCurrentStream(CallContext context) {
        initialise(context);

        try {
            URL url = new URL(url_string);
            URLConnection connection = url.openConnection();

            // if (sessioncookie != null && sessioncookie.length() != 0) {
            //     connection.addRequestProperty("Cookie",sessioncookie);
            // }

            // ((HttpURLConnection)connection).setRequestMethod("GET");

            // Map<String,List<String>> headers = connection.getHeaderFields();

            // // schoen waers.... (in java 1.5 gibts nur das interface, impl erst ab 1.6)
            // // CookieHandler.setDefault(new CookieManager());

            // for (String key : headers.keySet()) {
            //     String sep="";
            //     for (String value : headers.get(key)) {
            //         if (key != null) {
            //             if (key.equals("Set-Cookie")) {
            //                 if (sessioncookie == null) {
            //                     sessioncookie = "";
            //                 } else {
            //                     sessioncookie += "; ";
            //                 }
            //                 sessioncookie += value.replaceFirst(";.*$","");
            //                 if (verbose >= 1) {
            //                     System.err.print(sessioncookie+",");
            //                 }
            //             } else if (key.equals("Content-Disposition")) {
            //                 Pattern p = null;
            //                 try {
            //                     p = Pattern.compile(".*filename=\"([^\"]+)\".*");
            //                 } catch (PatternSyntaxException pse) {
            //                     pse.printStackTrace();
            //                     if (throw_exceptions) { throw pse; }
            //                 }
            //                 Matcher matcher = p.matcher(value);
            //                 if (matcher.find()) {
            //                     this.disposition_filename = matcher.group(1);
            //                 }
            //             } else if (key.equals("Content-Type")) {
            //                 this.content_type = value;
            //             }
            //         }
            //     }
            // }

            int status_code = 0;

            if (connection instanceof HttpURLConnection) {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                status_code = httpConnection.getResponseCode();
            }

            if (status_code == 200) {
                return connection.getInputStream();
            }

            CustomaryContext.create((Context)context).throwEnvironmentError(context, "Resource not reachable '%(url)', code '%(code)'", "url", url_string, "code", status_code);
            throw (ExceptionEnvironmentError) null; // compiler insists

        } catch (MalformedURLException mue) {
            CustomaryContext.create((Context)context).throwEnvironmentError(context, mue, "Invalid URL '%(url)'", "url", url_string);
            throw (ExceptionEnvironmentError) null; // compiler insists
        } catch (IOException ioe) {
            CustomaryContext.create((Context)context).throwEnvironmentError(context, ioe, "Cannot read resource '%(url)'", "url", url_string);
            throw (ExceptionEnvironmentError) null; // compiler insists
        }
    }

    // protected String sessioncookie;

    static public String proxy_host;
    static public String proxy_port;
    static public String proxy_user;
    static public String proxy_passwd;

    protected static volatile boolean initialised;

    static public void initialise (CallContext context) {
        if (initialised == false) {
            synchronized (Data_MediaObject_URL.class) {
                if (initialised == false) {
                    initialised = true;

                    if (proxy_host != null  ) { proxy_host    = System.getenv("proxy_host"); }
                    if (proxy_port != null  ) { proxy_port    = System.getenv("proxy_port"); }
                    if (proxy_user != null  ) { proxy_user    = System.getenv("proxy_user"); }
                    if (proxy_passwd != null) { proxy_passwd  = System.getenv("proxy_passwd"); }
                    
                    /* Proxy Magic */
                    if (proxy_host != null)
                    {
                        System.setProperty( "https.proxyHost", proxy_host);
                        System.setProperty( "https.proxyPort", proxy_port); 
                        System.setProperty( "http.proxyHost", proxy_host);
                        System.setProperty( "http.proxyPort", proxy_port); 
                        
                        // The properties to define the proxy user and password dont seem to work
                        // you will need to add the Proxy-Authorization header manually to the request
                        if (proxy_user != null) {
                            Authenticator.setDefault(new Authenticator() {
                                    protected PasswordAuthentication getPasswordAuthentication() {
                                        System.err.println("\nAuth was called\n");
                                        return new PasswordAuthentication(proxy_user,proxy_passwd.toCharArray());
                                    }
                                });
                        }
                    }

                    // System.setProperty("javax.net.ssl.trustStore", "/.../keystore");
                        
                    TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager(){
                            public java.security.cert.X509Certificate[] getAcceptedIssuers(){
                                return null;
                            }
                            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                        }
                    };
                    
                    SSLContext sc;
                    try {
                        sc = SSLContext.getInstance("SSL");
                        sc.init(null, trustAllCerts, new java.security.SecureRandom());
                    } catch (NoSuchAlgorithmException nsae) {
                        CustomaryContext.create((Context)context).throwEnvironmentFailure(context, nsae, "could not initialize ssl context");
                        throw (ExceptionEnvironmentFailure) null; // compiler insists
                    } catch (KeyManagementException kme) {
                        CustomaryContext.create((Context)context).throwEnvironmentFailure(context, kme, "could not initialize ssl context");
                        throw (ExceptionEnvironmentFailure) null; // compiler insists
                    }

                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                    
                    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }
                        });
                }
            }
        }
    }
}
