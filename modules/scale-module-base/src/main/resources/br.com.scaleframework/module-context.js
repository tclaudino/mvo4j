/**
 * 
 */

ModelMap = function() {

	function model(k, v) {
		this.key = k;
		this.value = v;
	}

	this.map = new Array();
};

ModelMap.prototype.add = function(k, v) {
	this.map.push(new model(k, v));
};

ModelMap.prototype.get = function(k, v) {
	for (i = 0; i < this.map.length; i++) {
		if (this.map[i].key == k) {
			return this.map[i].value;
		}
	}
};

ModuleStack.prototype.executions = {
	"AFTER" : 0,
	"BEFORE" : 1
};

ModuleStack = function(_module) {
	this.module = _module;
	this.index = -1;

	this.dependents = new Array();

	var StackModule = function(_module, _execution) {

		this._module = _module;
		this.execution = _execution;
	}

};

ModuleStack.prototype.next = function() {
	if (this.dependents.length == 0
			|| this.index >= (this.dependents.length - 1))
		return undefined;

	return this.dependents[++this.index];
};

ModuleStack.prototype.addAsNext = function(module) {
	this.dependents.push(module);
};

ModuleStack.prototype.addAsBefore = function(module) {
	this.dependents.push(module);
};

Module = function(_id, _name, _version, _path) {
	this.id = _id;
	this.name = _name;
	this.version = _version;
	this.path = _path;

	this.isDependent = false;
	this.dependents = new Array();
	this.attributes = new ModelMap();

	this.stack = new ModuleStack(this);
};

ModuleStack.prototype.addDependent = function(dependent) {
	this.dependents.push(dependent);
};

DepedentModule = function(_id, _name, _version, _dependentId, _jqSelector,
		_path) {
	Module.call(this, _id, _name, _path);
	this.dependentId = _dependentId;
	this.jqSelector = _jqSelector;
	this.isDependent = true;
};

DepedentModule.prototype = new Module();

ModuleContext = function() {

	ServiceContext.call(this);
	this.modules = new Array();
};

ModuleContext.prototype = new ServiceContext();

ModuleContext.prototype.getModule = function(id) {
	for (i = 0; i < this.modules.length; i++) {
		if (this.modules[i].id == id) {
			return this.modules[i];
		}
	}
	return undefined;
};

ModuleContext.prototype.addModule = function(module) {
	this.modules.push(module);
};

ModuleContext.prototype.callDependent = function(path, jqSelector) {
	this.get(path, this.modelMap, new function(data) {
		$(this.rootElement).chilldren(jqSelector).empty().html(data);
	}, new function(data) {
		Alert.error(Translation.translate(Translation.LOAD_MODULES_ERROR, ''));
	});
}