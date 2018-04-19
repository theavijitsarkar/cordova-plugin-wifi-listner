var exec = require('cordova/exec');

exports.startTracker = function(arg0,arg1,arg2, success, error) {
    exec(success, error, "wifilistner", "startTracker", [arg0,arg1,arg2]);
};

exports.stopTracker = function(arg0, success, error) {
    exec(success, error, "wifilistner", "stopTracker", [arg0]);
};