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
 */

(function() {

    var showError = function(error) {
        $('#error-message').text(error);
        $('#error-alert').collapse('show');
    }

    var hideError = function() {
        $('#error-alert').collapse('hide');
    }

    $('#submitBtn').on('click', function() {
        var formData = {
            InstallRoot: $('#pluginPathInput').val(),
            ServerName: $('#webserverNameInput').val()
        };
        var port = parseInt($('#portInput').val());

        if (Number(port) != port || port % 1 != 0 || port < 0 || port > 65535) {
            showError("Port must be a number between 0 and 65535");
        }

        if (formData.InstallRoot == "") {
            showError("The plugin install root must not be blank"); 
            return;
        }

        if (formData.ServerName == "") {
            showError("The webserver name must not be blank"); 
            return;
        }

        $.ajax("http://127.0.0.1:" + port + "/plugingen/GenPlugin", {
                method: 'POST',
                data: formData,
                success: onGenSuccess,
                error: onGenError
            });
    });

    var onGenSuccess = function(data, textStatus, jqXHR) {
        $('#cfg-output').text(data);
        hideError();
    };

    var onGenError = function(jqXHR, textStatus, errorThrown) {
        if (jqXHR.status == 0) {
            showError("We couldn't contact the backend. Ensure the server is on the same system as this browser.");
        } else {
            showError("Error when attempting to generate plugin config: " + errorThrown);
        }
    };
})();
