//= require jquery
//= require jquery_ujs
//= require turbolinks

//= require twitter/bootstrap
//= require Chart
//= require_tree .

// Google Analytics
(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
    m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
})(window,document,'script','//www.google-analytics.com/analytics.js','ga');

ga('create', 'UA-59144431-1', 'auto');
ga('send', 'pageview');


// Helper Functions
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

        // Ensure SSL on Prod
        if (location.hostname == "scorchforge.com" && url.indexOf("https") <= -1) {
            url = url.replace("http", "https")
        }

        console.log("Getting [scrapeAsyncHref]: " + url);

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
            url = $this.data('async-url'),
            pollInterval = $this.data('poll-interval');

        // Relative path?
        if (url.indexOf("http") <= -1) {
            url = getFullAddress(url);
        }

        // Ensure SSL on Prod
        if (location.hostname == "scorchforge.com" && url.indexOf("https") <= -1) {
            url = url.replace("http", "https")
        }

        console.log("Getting [scrapeAsyncUrl]: " + url);

        var pollRegisterFunction = function(isPoll) {
            $.ajax({
                url: url,
                dataType: 'html',
                type: 'get',
                success: function (html) {

                    if (isPoll) {
                        var innerHtmlText = $this.innerHTML;
                        var htmlText = $this.html();
                        if (htmlText != html) {
                            $this.html(html);
                        }
                    } else {
                        $this.fadeOut("fast", function () {
                            $this.html(html);
                            $this.fadeIn("fast");

                            // Finally scrap async-hrefs
                            scrapeAsyncHref();
                            scrapeAsyncUrl();
                        });
                    }

                    if (pollInterval != null) {
                        setTimeout(function() { pollRegisterFunction(true) }, pollInterval);
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    if (pollInterval != null) {
                        setTimeout(function() { pollRegisterFunction(true) }, pollInterval * 5);
                    }
                }
            });
        };

        pollRegisterFunction(false);

        // Remove the attribute to prevent re-loading
        this.removeAttribute("data-async-url");
    });
};

var ready = (function() {
    scrapeAsyncHref();
    scrapeAsyncUrl();
});
$(document).ready(ready)
$(window).bind('page:change', ready)