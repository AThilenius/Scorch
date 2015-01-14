$(document).ready(function() {
    $("#downloadButton").click(function() {
        $("#downloadButton").prop("disabled", true);
        window.location = "/downloads/flame";
    });
});