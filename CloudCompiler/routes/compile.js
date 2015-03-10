var express = require('express');
var router = express.Router();

router.post('/', function(req, res) {
    console.log("Running: " + req.body.code);
    var stdoutStr = "";
    var erroutStr = "";

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
