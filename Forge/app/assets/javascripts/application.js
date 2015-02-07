//= require jquery
//= require jquery_ujs
//= require turbolinks

//= require twitter/bootstrap
//= require faye
//= require Chart
//= require_tree .

// Google Analytics
(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
    m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
})(window,document,'script','//www.google-analytics.com/analytics.js','ga');

ga('create', 'UA-59144431-1', 'auto');
ga('send', 'pageview');

function getBaseURL () {
    return location.protocol + "//" + location.hostname + (location.port && ":" + location.port) + "/";
}

function getFullAddress (href) {
    return getBaseURL() + href;
}

var scrapeAsyncHref = function() {
    // Scan all elements for 'async-href' and add an onClick function
    $('[data-async-href]').each(function () {
        var element = this;
        var $this = $(this);
        var url = getFullAddress($this.data('async-href'));
        var contentAreaName = $this.data('content-area') == null ? 'async-content-area' : $this.data('content-area');
        var contentArea = $(document.getElementById(contentAreaName));

        element.onclick = function() {
            element.disabled = true;

            $.ajax({
                url: url,
                dataType: 'html',
                type: 'get',
                success: function (html) {
                    contentArea.fadeOut("fast", function () {
                        contentArea.html(html);
                        contentArea.fadeIn("fast");
                        scrapeAsyncHref();
                        scrapeAsyncUrl();
                    });

                    element.disabled = false;
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    element.innerHTML = "Request Failed! :(";
                }
            });
        };
    });
};

var scrapeAsyncUrl = function() {
    $('[data-async-url]').each(function () {
        var $this = $(this),
            url = $this.data('async-url');

        // Relative path?
        if (url.indexOf("http") <= -1) {
            url = getFullAddress(url);
        }

        $.ajax({
            url: url,
            dataType: 'html',
            type: 'get',
            success: function (html) {
                $this.fadeOut("fast", function () {
                    $this.html(html);
                    $this.fadeIn("fast");

                    // Finally scrap async-hrefs
                    scrapeAsyncHref();
                    scrapeAsyncUrl();
                });
            }
        });

        // Remove the attribute to prevent re-loading
        this.removeAttribute("data-async-url");
    });
}

var ready = (function() {
    scrapeAsyncHref();
    scrapeAsyncUrl();
});
$(document).ready(ready)
$(window).bind('page:change', ready)