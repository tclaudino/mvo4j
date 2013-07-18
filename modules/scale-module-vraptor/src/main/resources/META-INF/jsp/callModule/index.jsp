<html>

<head>
<link rel="stylesheet" type="text/css"
	href="extjs/resources/css/ext-all.css">
<script type="text/javascript" src="extjs/ext-all-debug.js"></script>
</head>

<body>

	<h2>Wellcome to Modules Index</h2>

	<script type="text/javascript">
		Ext.create('Ext.tab.Panel', {
			renderTo : Ext.getBody(),
			height : 100,
			width : 200,
			items : [
					{
						// Explicitly define the xtype of this Component configuration.
						// This tells the Container (the tab panel in this case)
						// to instantiate a Ext.panel.Panel when it deems necessary
						xtype : 'panel',
						title : 'Tab One',
						html : 'The first tab',
						listeners : {
							render : function() {
								Ext.MessageBox.alert('Rendered One',
										'Tab One was rendered.');
							}
						}
					},
					{
						// this component configuration does not have an xtype since 'panel' is the default
						// xtype for all Component configurations in a Container
						title : 'Tab Two',
						html : 'The second tab',
						listeners : {
							render : function() {
								Ext.MessageBox.alert('Rendered One',
										'Tab Two was rendered.');
							}
						}
					} ]
		});
	</script>
</body>
</html>
