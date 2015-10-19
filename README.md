About
====
This is a sample liberty app which can be used to generate a plugin configuration. Note that it can only be used from the local system where liberty is running; it will deny all requests that come from remote IPs. Load the app on the liberty server for which you want to generate a plugin configuration.

URLs
----
The app exposes the URL `/GenPlugin` which takes two parameters: `InstallRoot` and `ServerName`. They can be passed in the query string for a GET request, or in the body as form-encoded data in a POST request. `InstallRoot` should be set to the install root of the plugin; `ServerName` should be set to the webserver name in your liberty configuration. The response body will contain the contents of the generated `plugin-cfg.xml`.

Here is an example using curl:

    curl "http://127.0.0.1:9080/plugingen/GenPlugin?InstallRoot=/tmp/fakeinstallroot&ServerName=webserver1" > plugin-cfg.xml

The quotes are important here because without them, the `&` in the query string will get interpreted by the shell.

There's also another URL, `/PluginCfgLocation`, which tells you where the generated configuration is initially saved (`/GenPlugin` saves the config as a file, then reads that file and sends the contents as the response).

Frontend
----
If you'd rather use a web frontend, hit `/` from the same system running the liberty server. It should present you with a form; fill out the fields and hit the "Generate" button. The box below should populate with the generated `plugin-cfg.xml`.
