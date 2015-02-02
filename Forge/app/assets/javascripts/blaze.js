window.client = new Faye.Client('/faye');

jQuery(function() {
    return client.subscribe('/chat', function(payload) {
        if (payload.message) {
            return $('#postsBody').prepend(payload.message);
        }
    });
});

jQuery(function() {
    $("#postButton").click(function () {
        publisher = client.publish('/chat', {
            message: $("#textInput").val()
        });
    });
});