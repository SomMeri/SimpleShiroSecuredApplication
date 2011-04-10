/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.meri.simpleshirosecuredapplication.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.security.SslSocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

import com.gargoylesoftware.htmlunit.WebClient;

public abstract class AbstractContainerTest {
    protected static PauseableServer server;

    protected static final int port = 9180;
    protected static final int sslPort = 8443;

    protected static final String BASEURI = "http://localhost:" + port + "/";

    protected final WebClient webClient = new WebClient();
    
    private static String[] configurationClasses =
    {
    	"org.mortbay.jetty.webapp.JettyWebXmlConfiguration",
    	"org.mortbay.jetty.webapp.WebInfConfiguration",
    	"org.mortbay.jetty.plus.webapp.EnvConfiguration",
    	"org.mortbay.jetty.plus.webapp.Configuration",
    	"org.mortbay.jetty.webapp.TagLibConfiguration"
    } ; 


    @BeforeClass
    public static void startContainer() throws Exception {
        if (server == null) {
            server = new PauseableServer();
            Connector connector = createHttpConnector();
            SslSocketConnector sslConnector = createSslConnector();
            server.setConnectors(new Connector[]{connector, sslConnector});
            
            WebAppContext context = createWebAppContext();
						server.setHandler(context);
						
            server.start();
            assertTrue(server.isStarted());
        }
    }

		private static WebAppContext createWebAppContext() {
	    WebAppContext context = new WebAppContext("src/main/webapp", "/");
	    context.setConfigurationClasses(configurationClasses);
	    
	    return context;
    }

		private static SslSocketConnector createSslConnector() {
	    SslSocketConnector sslConnector = new SslSocketConnector();
	    sslConnector.setPort(sslPort);
	    sslConnector.setKeyPassword("secret");
	    sslConnector.setKeystore("src/test/resources/keystore");
	    sslConnector.setTrustPassword("secret");
	    sslConnector.setTruststore("src/main/resources/truststore");
	    sslConnector.setPassword("secret");
	    sslConnector.setWantClientAuth(true);
	    //sslConnector.setNeedClientAuth(true);
	    
	    return sslConnector;
    }

		private static Connector createHttpConnector() {
	    Connector connector = new SelectChannelConnector();
	    connector.setPort(port);
	    return connector;
    }

    @Before
    public void beforeTest() {
        webClient.setThrowExceptionOnFailingStatusCode(true);
    }

    public void pauseServer(boolean paused) {
        if (server != null) server.pause(paused);
    }

    public static class PauseableServer extends Server {
        public synchronized void pause(boolean paused) {
            try {
                if (paused) for (Connector connector : getConnectors())
                    connector.stop();
                else for (Connector connector : getConnectors())
                    connector.start();
            } catch (Exception e) {
            }
        }
    }
}
