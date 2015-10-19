/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */

package com.ibm.elu;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;

import javax.management.JMX;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.websphere.webcontainer.GeneratePluginConfigMBean;

@WebServlet("/GenPlugin")
public class GenPlugin extends HttpServlet {
	private static final long serialVersionUID = 1L;
    final String mbeanName = "WebSphere:name=com.ibm.ws.jmx.mbeans.generatePluginConfig";
    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
       
    public GenPlugin() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	handleRequest(request, response);
    }

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	handleRequest(request, response);
    }

	protected void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String installRoot = request.getParameter("InstallRoot");
		String serverName = request.getParameter("ServerName");
        ServletOutputStream output = response.getOutputStream();

    	response.addHeader("Access-Control-Allow-Origin", "*");
		
		if (!Util.checkAccess(request)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			output.println("You must hit this app from localhost.");
			output.close();
			return;
		}
		
		if (installRoot == null || installRoot.length() == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			output.println("The InstallRoot paramter is missing.");
			output.close();
			return;
		} else if (serverName == null || serverName.length() == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			output.println("The ServerName paramter is missing.");
			output.close();
			return;
		}

        try {
            ObjectName pluginMBean = new ObjectName(mbeanName);
            if (mbs.isRegistered(pluginMBean)) {
            	GeneratePluginConfigMBean plugin = JMX.newMBeanProxy(mbs, pluginMBean, GeneratePluginConfigMBean.class);
            	plugin.generatePluginConfig(installRoot, serverName);
            }
            
            String pluginCfgPath = System.getProperty("server.output.dir") + "plugin-cfg.xml";
            File pluginCfg = new File(pluginCfgPath);
            if (pluginCfg.exists()) {
            	Files.copy(pluginCfg.toPath(), output);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch(MalformedObjectNameException e) {
        	e.printStackTrace();
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
	}
}
