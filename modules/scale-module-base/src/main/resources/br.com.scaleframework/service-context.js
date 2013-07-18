/**
 * 
 */
ServiceContext = function() {

};

ServiceContext.prototype.ajax = function(path, params, method, onSuccess, onError) {

	$.ajax(method, path, params, new function(success, data) {
			if (!!success) {
				if(!!onSuccess) {
					onSuccess(data);
				}
			} else {
				if(!!onError) {
					onError(data);
				}
			}
		}
	});
}

ServiceContext.prototype.get = function(path, params, onSuccess, onError) {

	this.ajax(path, params, "GET", callback);
}

ServiceContext.prototype.post = function(path, params, onSuccess, onError) {

	this.ajax(path, params, "POST", callback);
}
