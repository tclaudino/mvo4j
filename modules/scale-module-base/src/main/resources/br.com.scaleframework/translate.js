/**
 * 
 */

Translation.prototype.map = new Array();

Translation.prototype.LOAD_MODULES_ERROR = "Erro ao carregar o modulo '{0}', \nMessagem: {1}";

Translation.prototype.add = function(k, v) {
	Translation.map.push(new Map(k, v));
};

Translation.prototype.translate = function(key, defaultValue) {
	for (i = 0; i < Translation.map.length; i++) {
		if (Translation.map[i].key == key) {
			return Translation.map[i].value;
		}
	}
	return !!defaultValue ? defaultValue : "";
};