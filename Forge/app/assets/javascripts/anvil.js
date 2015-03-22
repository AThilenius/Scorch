var ready = function() {

    // Drop down Controls
    function selectTab(osName) {
        $('#Windows').hide();
        $('#OSX').hide();
        $('#Other').hide();
        $('#' + osName).fadeIn('slow');
        $('#osDropdown #ddText').text(osName + " ");
        $('#osTypeText').text(osName);
    };

    $("#windowsSelect").click(function () {
        selectTab('Windows')
    });

    $("#osxSelect").click(function () {
        selectTab('OSX');
    });

    $("#otherSelect").click(function () {
        selectTab('Other');
        $('#osTypeText').text("...");
    });

    if (navigator.appVersion.indexOf("Win") != -1) {
        selectTab('Windows');
    } else if (navigator.appVersion.indexOf("Mac") != -1) {
        selectTab('OSX');
    } else {
        selectTab('Other');
    }

    $("#run_button").click(function () {
        var jsonData = {
            auth_token: "bbd3ec28-27a5-4080-b8c3-61803e714817",
            language: "python",
            code: editor.getValue()
        };
        var url = "http://localhost:6010/compile";
        console.log("Submitting code: " + jsonData);

        $.ajax({
            type: "POST",
            url: url,
            data: jsonData,
            dataType:"json",
            success: function (data) {
                console.log("Got compiler output: " + data);
                if (data.strerr != null && data.strerr != "") {
                    terminal.setValue(data.strerr);
                } else {
                    terminal.setValue(data.stdout);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log("Request Failed! :(");
            }
        });
    });

};
$(document).ready(ready)
$(window).bind('page:change', ready)