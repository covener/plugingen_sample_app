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

package java.net.wasdev;

import javax.servlet.http.HttpServletRequest;

public class Util {
    public static boolean checkAccess(HttpServletRequest request) {
		String remote_ip = request.getHeader("X-Forwarded-For");
		if (remote_ip == null) {
			remote_ip = request.getRemoteAddr();
		}

		if (!remote_ip.equals("127.0.0.1") && !remote_ip.equals("localhost")) {
			return false;
		}
		return true;
    }
    
    public static String getPluginLocation() {
        return System.getProperty("server.output.dir") + "plugin-cfg.xml";
    }
}
