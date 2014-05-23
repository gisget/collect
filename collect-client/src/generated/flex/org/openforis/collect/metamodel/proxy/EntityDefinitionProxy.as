/**
 * Generated by Gas3 v2.2.0 (Granite Data Services).
 *
 * NOTE: this file is only generated if it does not exist. You may safely put
 * your custom code here.
 */

package org.openforis.collect.metamodel.proxy {
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.openforis.collect.model.Queue;
	import org.openforis.collect.util.ArrayUtil;
	import org.openforis.collect.util.UIUtil;

    [Bindable]
    [RemoteClass(alias="org.openforis.collect.metamodel.proxy.EntityDefinitionProxy")]
    public class EntityDefinitionProxy extends EntityDefinitionProxyBase {
		
		public function getChildDefinition(name:String):NodeDefinitionProxy {
			for each(var nodeDef:NodeDefinitionProxy in childDefinitions) {
				if(nodeDef.name == name){
					return nodeDef;
				}
			}
			return null;
		}
		
		public function getChildDefinitionByTabName(name:String):NodeDefinitionProxy {
			for each(var nodeDef:NodeDefinitionProxy in childDefinitions) {
				if(nodeDef.uiTabName == name){
					return nodeDef;
				}
			}
			return null;
		}
		
		public function getDefinitionsInVersion(version:ModelVersionProxy):IList {
			var result:IList = new ArrayCollection();
			for each (var defn:NodeDefinitionProxy in childDefinitions) {
				if(version == null || version.isApplicable(defn)){
					result.addItem(defn);
				}
			}
			return result;
		}
		
		/**
		 * Returns all the key attribute definitions of this entity
		 * (it looks for key attribute defitions even in nested single entities)
		 */
		public function get keyAttributeDefinitions():IList {
			var result:ArrayCollection = new ArrayCollection();
			var queue:Queue = new Queue();
			queue.pushAll(childDefinitions.toArray());
			while ( ! queue.isEmpty() ) {
				var nodeDef:NodeDefinitionProxy = queue.pop();
				if ( nodeDef is AttributeDefinitionProxy && AttributeDefinitionProxy(nodeDef).key ) {
					result.addItem(nodeDef);
				} else if ( nodeDef is EntityDefinitionProxy && ! (nodeDef.multiple) ) {
					var singleEntity:EntityDefinitionProxy = EntityDefinitionProxy(nodeDef);
					queue.pushAll(singleEntity.childDefinitions.toArray());
				}
			}
			return result;
		}
		
		public function isRoot():Boolean {
			return this == rootEntity;
		}
		
		public function hasMultipleEntitiesDescendants():Boolean {
			var stack:Array = new Array();
			ArrayUtil.addAll(stack, childDefinitions.toArray());
			while ( stack.length > 0 ) {
				var nodeDef:NodeDefinitionProxy = stack.pop();
				if ( nodeDef is EntityDefinitionProxy ) {
					if ( nodeDef.multiple ) {
						return true;
					} else {
						ArrayUtil.addAll(stack, EntityDefinitionProxy(nodeDef).childDefinitions.toArray());
					}
				}
			}
			return false;
		}

		public function hasTableLayout():Boolean {
			return layout == UIUtil.LAYOUT_TABLE;
		}

	}
	
}
