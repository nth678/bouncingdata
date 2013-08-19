/**
 * editor_plugin_src.js
 *
 * This plugin provides a button named 'codeElement' which users can use to wrap text
 * in a <code>code element</code> or tag.
 */

(function() {
	
	tinymce.create('tinymce.plugins.CodeElementPlugin', {
		init : function(ed, url) {

			// Register commands
			ed.addCommand('mceCodeElement', function() {
				ed.execCommand('mceBeginUndoLevel');
				
				var e = ed.dom.getParent(ed.selection.getNode(), 'CODE');
				if (e == null){
					//add code element
					if ( ed.selection.isCollapsed() ) {
						ed.execCommand('mceInsertContent', false, " <code>code</code>&nbsp;");
					} else {
						ed.execCommand('mceReplaceContent',false,' <code>{$selection}</code>');
					}				
				} else {
					//remove code element
					ed.execCommand('mceRemoveNode', false, e);
					ed.nodeChanged();
				}
				
				ed.execCommand('mceEndUndoLevel');
			});

			// Register buttons
			ed.addButton('codeElement', {
				title : 'Insert/edit code',
				cmd : 'mceCodeElement',
				image : url + '/img/code.gif'
			});
			
			//set button to pressed when cursor at code
			ed.onNodeChange.add(function(ed, cm, n) {
				cm.setActive('codeElement', n.nodeName == 'CODE');
			});
		},

		getInfo : function() {
			return {
				longname : 'Code Element Plugin',
				author : 'Mauro Bieg',
				authorurl : '',
				infourl : '',
				version : tinymce.majorVersion + "." + tinymce.minorVersion
			};
		}
	});

	// Register plugin
	tinymce.PluginManager.add('codeElement', tinymce.plugins.CodeElementPlugin);
})();