var exec = require('cordova/exec');

exports.startTracker = function(arg0,arg1,arg2,arg3, success, error) {
    exec(success, error, "wifilistner", "startTracker", [arg0,arg1,arg2,arg3]);
    //arg0-mac, arg1-title, arg2-on connected, arg3- on disconnect
};

exports.stopTracker = function(arg0, success, error) {
    exec(success, error, "wifilistner", "stopTracker", [arg0]);
};