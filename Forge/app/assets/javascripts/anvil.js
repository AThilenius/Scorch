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


};
$(document).ready(ready)
$(window).bind('page:change', ready)