/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.servlet;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class RegisteredEchoServletTestCase {
    @Deployment(testable = false)
    public static WebArchive getTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "servlet-test.war")
                .addClass(EchoServlet.class)
                .setWebXML(new StringAsset(Descriptors.create(WebAppDescriptor.class).metadataComplete(true).version("2.5")
                        .createServlet()
                            .servletName(EchoServlet.class.getSimpleName())
                            .servletClass(EchoServlet.class.getName()).up()
                        .createServletMapping()
                            .servletName(EchoServlet.class.getSimpleName())
                            .urlPattern(EchoServlet.URL_PATTERN).up()
                        .exportAsString()));
    }

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void shouldBeAbleToInvokeServletInDeployedWebApp() throws Exception {
        String requestUrl = deploymentUrl + EchoServlet.URL_PATTERN.substring(1) + "?" + EchoServlet.MESSAGE_PARAM + "=hello";
        String body = StreamReaderUtil.readAllAndClose(new URL(requestUrl).openStream());

        Assert.assertEquals("Verify that the servlet was deployed and returns expected result", "hello", body);
    }

}
