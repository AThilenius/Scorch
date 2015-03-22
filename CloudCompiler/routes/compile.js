var express = require('express');
var fs = require('fs');
var router = express.Router();

router.post('/', function(req, res) {
    console.log("Handling request: " + JSON.stringify(req.body));
    console.log("Running: " + req.body.code);
    var stdoutStr = "";
    var erroutStr = "";

    // Set the Auth Token
    fs.writeFile("BlazeConfig.json",
        JSON.stringify({ url : "https://scorchforge.com/services/blaze/json", auth_token : req.body.auth_token }), 
        function(err) {
            if(err) {
                return console.log(err);
            }
        }
    );

    var spawn  = require('child_process').spawn,
        python = spawn('python', ['-c', req.body.code]);

    python.stdout.on('data', function (data) {
        console.log('stdout: ' + data);
        stdoutStr = stdoutStr + data;
    });

    python.stderr.on('data', function (data) {
        console.log('stderr: ' + data);
        erroutStr = erroutStr + data;
    });

    python.on('close', function (code) {
        console.log('child process exited with code ' + code);
        res.json( { stdout : stdoutStr, strerr : erroutStr } );
    });
});

module.exports = router;
