/**
 * Generated by Gas3 v2.2.0 (Granite Data Services).
 *
 * NOTE: this file is only generated if it does not exist. You may safely put
 * your custom code here.
 */

package org.openforis.collect.model.proxy {
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.collections.ListCollectionView;
	import mx.collections.Sort;
	
	import org.granite.collections.IMap;
	import org.openforis.collect.metamodel.proxy.AttributeDefinitionProxy;
	import org.openforis.collect.metamodel.proxy.CodeAttributeDefinitionProxy;
	import org.openforis.collect.metamodel.proxy.CodeListItemProxy;
	import org.openforis.collect.metamodel.proxy.EntityDefinitionProxy;
	import org.openforis.collect.metamodel.proxy.ModelVersionProxy;
	import org.openforis.collect.metamodel.proxy.NodeDefinitionProxy;
	import org.openforis.collect.metamodel.proxy.NumberAttributeDefinitionProxy;
	import org.openforis.collect.metamodel.proxy.TaxonAttributeDefinitionProxy;
	import org.openforis.collect.util.ArrayUtil;
	import org.openforis.collect.util.CollectionUtil;
	import org.openforis.collect.util.ObjectUtil;
	import org.openforis.collect.util.StringUtil;
	import org.openforis.collect.util.UIUtil;
	import org.openforis.idm.metamodel.validation.ValidationResultFlag;

	/**
	 * @author S. Ricci
	 */
    [Bindable]
    [RemoteClass(alias="org.openforis.collect.model.proxy.EntityProxy")]
    public class EntityProxy extends EntityProxyBase {
		
		private static const _SORT:Sort = new Sort();
		
		private static const KEY_LABEL_SEPARATOR:String = "-";
		private static const FULL_KEY_LABEL_SEPARATOR:String = " ";
		
		private var _keyText:String;
		private var _fullKeyText:String;
		private var _compactKeyText:String;
		private var _enumeratedEntitiesCodeWidths:Array;
		
		override public function init():void {
			super.init();
			updateEnumeratedCodeWidths();
			updateKeyText();
		}
		
		protected function updateEnumeratedCodeWidths():void {
			_enumeratedEntitiesCodeWidths = new Array();
			var entities:IList = getChildEntities();
			for each (var e:EntityProxy in entities) {
				if(e.enumerated) {
					var name:String = e.name;
					var maxWidth:Number = _enumeratedEntitiesCodeWidths[name];
					var keyAttribute:CodeAttributeProxy = e.getKeyAttribute();
					if(keyAttribute != null && keyAttribute.codeListItem != null) {
						var def:CodeAttributeDefinitionProxy = CodeAttributeDefinitionProxy(keyAttribute.definition);
						var item:CodeListItemProxy = keyAttribute.codeListItem;
						var label:String = (def.showCode ? (item.code + " - "): "") + item.getLabelText();
						var width:Number = UIUtil.measureFixedCodeWidth(label) + 10;
						if(item.qualifiable) {
							width += 104; //space for qualifier text input
						}
						if(!isNaN(maxWidth)) {
							maxWidth = Math.max(maxWidth, width);
						} else {
							maxWidth = width;
						}
					}
					_enumeratedEntitiesCodeWidths[name] = maxWidth;
				}
			}
		}
		
		protected function getKeyAttribute():CodeAttributeProxy {
			var children:IList = getChildren();
			for each (var child:NodeProxy in children) {
				if(child is CodeAttributeProxy) {
					var codeAttribute:CodeAttributeProxy = CodeAttributeProxy(child);
					if(codeAttribute.enumerator) {
						return codeAttribute;
					}
				}
			}
			return null;
		}
		
		/**
		 * Traverse each child and pass it to the argument function
		 * */
		public function traverse(funct:Function):void {
			var stack:Array = new Array();
			var children:IList = getChildren();
			ArrayUtil.addAll(stack, children.toArray());
			while ( stack.length > 0 ) {
				var node:NodeProxy = NodeProxy(stack.pop());
				funct(node);
				if ( node is EntityProxy ) {
					children = EntityProxy(node).getChildren();
					ArrayUtil.addAll(stack, children.toArray());
				}
			}
		}
		
		override protected function setParentReferencesOnChildren():void {
			var children:IList = getChildren();
			for each (var child:NodeProxy in children) {
				child.parent = this;
			}
		}
		
		public function getSingleAttribute(def:NodeDefinitionProxy):AttributeProxy {
			var attributes:IList = getChildren(def);
			if(attributes != null) {
				if(attributes.length == 1) {
					var attribute:AttributeProxy = attributes.getItemAt(0) as AttributeProxy;
					return attribute;
				} else if (attributes.length > 1) {
					var message:String = "Single attribute expected but multiple values found: " + def.name;
					throw new Error(message);
				}
			}
			return null;
		}
		
		public function getDescendantSingleAttribute(defnId:int):AttributeProxy {
			var leafAttrs:IList = getLeafAttributes();
			for each (var attr:AttributeProxy in leafAttrs) {
				if ( attr.definition.id == defnId ) {
					return attr;
				}
			}
			return null;
		}
		
		public function getChildren(childDefinition:NodeDefinitionProxy = null):IList {
			var result:ArrayCollection;
			if(childDefinition != null) {
				result = childrenByDefinitionId.get(childDefinition.id);
				if (result == null) {
					result = new ArrayCollection();
				}
			} else {
				result = new ArrayCollection();
				var childDefns:IList = EntityDefinitionProxy(definition).childDefinitions;
				for each (var childDefn:NodeDefinitionProxy in childDefns) {
					var childrenPart:IList = childrenByDefinitionId.get(childDefn.id);
					if ( CollectionUtil.isNotEmpty(childrenPart) ) {
						result.addAll(childrenPart);
					}
				}
			}
			return result;
		}
		
		public function getLeafAttributes():IList {
			var result:ArrayCollection = new ArrayCollection();
			var stack:Array = new Array();
			var children:IList = getChildren();
			ArrayUtil.addAll(stack, children.toArray());
			while ( stack.length > 0 ) {
				var node:NodeProxy = NodeProxy(stack.pop());
				if ( node is EntityProxy ) {
					children = EntityProxy(node).getChildren();
					ArrayUtil.addAll(stack, children.toArray());
				} else {
					result.addItemAt(node, 0);
				}
			}
			return result;
		}
		
		public function getLeafFields():IList {
			var result:IList = new ArrayCollection();
			var leafAttributes:IList = getLeafAttributes();
			for each (var a:AttributeProxy in leafAttributes) {
				var fields:ListCollectionView = a.fields;
				CollectionUtil.addAll(result, fields);
			}
			return result;
		}
		
		public static function sortEntitiesByKey(entities:IList):IList {
			var result:ArrayCollection = null;
			if ( entities != null ) {
				result = new ArrayCollection(entities.toArray());
				var sort:Sort = new Sort();
				sort.compareFunction = entitiesKeyCompareFunction;
				result.sort = sort;
				result.refresh();
				//to prevent issue due to use of a custom sort function in data providers...
				result = new ArrayCollection(result.toArray());
			}
			return result;
		}
		
		protected static function entitiesKeyCompareFunction(entity1:EntityProxy, entity2:EntityProxy, fields:Array = null):int {
			if ( entity1 == null && entity2 == null ) {
				return 0;
			} else if ( entity1 == null ) {
				return -1;
			} else if ( entity2 == null ) {
				return 1;
			} else if ( entity1 == entity2 ) {
				return 0;
			} else {
				var keyValues1:Array = entity1.getKeyValues();
				if ( keyValues1.length > 0 ) {
					var keyValues2:Array = entity2.getKeyValues();
					for (var i:int = 0; i < keyValues1.length; i++) {
						var keyValue1:Object = keyValues1[i];
						var keyValue2:Object = keyValues2[i];
						var compareResult:int = compareKeyValues(keyValue1, keyValue2, fields);
						if ( compareResult != 0 ) {
							return compareResult;
						}
					}
				} else {
					return _SORT.compareFunction.call(null, entity1.index, entity2.index, fields);
				}
			}
			return 0;
		}
	
		protected static function compareKeyValues(keyValue1:Object, keyValue2:Object, fields:Array = null):int {
			if ( ObjectUtil.isNumber(keyValue1) && ObjectUtil.isNumber(keyValue2) ) {
				keyValue1 = ObjectUtil.toNumber(keyValue1);
				keyValue2 = ObjectUtil.toNumber(keyValue2);
			}
			var partialCompareResult:int = _SORT.compareFunction.call(null, keyValue1, keyValue2, fields);
			return partialCompareResult;
		}
		
		public function getChild(childDef:NodeDefinitionProxy, index:int):NodeProxy {
			var children:IList = getChildren(childDef);
			if(children != null && children.length > index) {
				return children.getItemAt(index) as NodeProxy;
			} else {
				return null;
			}
		}
		
		public function getChildEntities():IList {
			var entities:IList = new ArrayCollection();
			var values:IList = childrenByDefinitionId.values;
			for each (var childList:IList in values) {
				for each (var child:NodeProxy in childList) {
					if(child is EntityProxy) {
						entities.addItem(child);
					}
				}
			}
			return entities;
		}
		
		public function addChild(child:NodeProxy):void {
			var childDef:NodeDefinitionProxy = child.definition
			var siblings:IList = getChildren(childDef);
			if(siblings == null) {
				siblings = new ArrayCollection();
				childrenByDefinitionId.put(childDef.id, siblings);
			}
			child.parent = this;
			child.init();
			siblings.addItem(child);
			showErrorsOnChild(childDef);
			
			if ( child is EntityProxy ) {
				if (CollectionUtil.isEmpty(EntityDefinitionProxy(childDef).keyAttributeDefinitions)) {
					for each (var sibling:EntityProxy in siblings) {
						sibling.updateKeyText();
					}
				}
			}
		}
		
		public function removeChild(node:NodeProxy):void {
			var childDef:NodeDefinitionProxy = node.definition;
			var children:IList = getChildren(childDef);
			var index:int = children.getItemIndex(node);
			if(index >= 0) {
				children.removeItemAt(index);
			}
			showErrorsOnChild(childDef);
			
			if ( childDef is EntityDefinitionProxy && CollectionUtil.isEmpty(EntityDefinitionProxy(childDef).keyAttributeDefinitions)) {
				for each (var sibling:EntityProxy in children) {
					sibling.updateKeyText();
				}
			}
		}
		
		public function replaceChild(oldNode:NodeProxy, newNode:NodeProxy):void {
			var childDef:NodeDefinitionProxy = oldNode.definition;
			var children:IList = getChildren(childDef);
			var index:int = children.getItemIndex(oldNode);
			children.setItemAt(newNode, index);
		}
		
		public function moveChild(node:NodeProxy, index:int):void {
			var childDef:NodeDefinitionProxy = node.definition;
			var children:IList = getChildren(childDef);
			CollectionUtil.moveItem(children, node, index);
			for each (var child:NodeProxy in children){
				child.updateIndex();
			}
		}
		
		public function updateKeyText():void {
			var keyDefs:IList = EntityDefinitionProxy(definition).keyAttributeDefinitions;
			if(keyDefs.length > 0) {
				var shortKeyParts:Array = new Array();
				var fullKeyParts:Array = new Array();
				for each (var def:AttributeDefinitionProxy in keyDefs) {
					var keyAttr:AttributeProxy = getSingleAttribute(def);
					if(keyAttr != null) {
						var keyValue:Object = getKeyValue(keyAttr);
						if(keyValue != null && StringUtil.isNotBlank(keyValue.toString())) {
							shortKeyParts.push(keyValue.toString());
							var label:String = def.getInstanceOrHeadingLabelText();
							var fullKeyPart:String = label + " " + keyValue;
							fullKeyParts.push(fullKeyPart);
						}
					}
				}
				keyText = StringUtil.concat(KEY_LABEL_SEPARATOR, shortKeyParts);
				fullKeyText = StringUtil.concat(FULL_KEY_LABEL_SEPARATOR, fullKeyParts);
			} else if(parent != null) {
				var siblings:IList = getSiblings();
				var itemIndex:int = siblings.getItemIndex(this);
				keyText = String(itemIndex + 1);
				fullKeyText = keyText;
			}
		}
		
		protected function getKeyValues():Array {
			var result:Array = new Array();
			var keyDefs:IList = EntityDefinitionProxy(definition).keyAttributeDefinitions;
			if(keyDefs.length > 0) {
				for each (var def:AttributeDefinitionProxy in keyDefs) {
					var keyAttr:AttributeProxy = getSingleAttribute(def);
					var keyValue:Object = getKeyValue(keyAttr);
					result.push(keyValue);
				}
			}
			return result;
		}
		
		private function getKeyValue(attribute:AttributeProxy):Object {
			var attributeDefn:AttributeDefinitionProxy = AttributeDefinitionProxy(attribute.definition);
			if(attributeDefn.editable && attribute.userSpecified && attribute.empty) {
				return null;
			}
			if (attributeDefn is NumberAttributeDefinitionProxy) {
				var numberDefn:NumberAttributeDefinitionProxy = NumberAttributeDefinitionProxy(attributeDefn);
				var f:FieldProxy = attribute.getField(0);
				var value:Object = f.value;
				if ( numberDefn.integer ) {
					return int(value);
				} else {
					return Number(value);
				}
			} else if (attributeDefn is TaxonAttributeDefinitionProxy) {
				var visibleFieldIndexes:IList = attributeDefn.visibleFieldIndexes;
				var firstVisibleFieldIdx:int = int(visibleFieldIndexes.getItemAt(0));
				var f:FieldProxy = attribute.getField(firstVisibleFieldIdx);
				return f.value;
			} else {
				var f:FieldProxy = attribute.getField(0);
				return f.value;
			}
		}
		
		public function updateChildrenMinCountValiditation(map:IMap):void {
			updateList(childrenMinCountValidation, map);
		}
		
		public function updateChildrenMaxCountValiditation(map:IMap):void {
			updateList(childrenMaxCountValidation, map);
		}

		public function updateChildrenRelevance(map:IMap):void {
			updateList(childrenRelevance, map);
		}

		public function updateChildrenMinCount(map:IMap):void {
			updateList(childrenMinCount, map);
		}
		
		public function updateChildrenMaxCount(map:IMap):void {
			updateList(childrenMaxCount, map);
		}
		
		public function showErrorsOnNotEmptyDescendants():void {
			this.traverse(function(node:NodeProxy):void {
				if (! node.empty) {
					var parent:EntityProxy = node.parent;
					if (parent != null && node.definition != null) {
						parent.showErrorsOnChild(node.definition);
					}
				}
			});
		}
		
		public function showErrorsOnChildren():void {
			for (var i:int; i < childrenErrorVisible.length; i++) {
				childrenErrorVisible.setItemAt(true, i);
			}
		}
		
		public function showErrorsOnChild(childDef:NodeDefinitionProxy):void {
			var index:int = getChildDefinitionIndex(childDef);
			childrenErrorVisible.setItemAt(true, index);
		}
		
		public function isErrorOnChildVisible(childDef:NodeDefinitionProxy):Boolean {
			var index:int = getChildDefinitionIndex(childDef);
			var result:Boolean = childrenErrorVisible.getItemAt(index);
			return result;
		}
		
		public function isRequired(childDef:NodeDefinitionProxy):Boolean {
			return getMinCount(childDef) > 0;
		}
		
		public function getMinCount(childDef:NodeDefinitionProxy):int {
			var index:int = getChildDefinitionIndex(childDef);
			var count:int = childrenMinCount.getItemAt(index) as int;
			return count;
		}
		
		public function getMaxCount(childDef:NodeDefinitionProxy):int {
			var index:int = getChildDefinitionIndex(childDef);
			var count:int = childrenMaxCount.getItemAt(index) as int;
			return count;
		}
		
		public function getMinCountValidation(childDef:NodeDefinitionProxy):ValidationResultFlag {
			var index:int = getChildDefinitionIndex(childDef);
			return childrenMinCountValidation.getItemAt(index) as ValidationResultFlag;
		}
		
		public function getMaxCountValidation(childDef:NodeDefinitionProxy):ValidationResultFlag {
			var index:int = getChildDefinitionIndex(childDef);
			return childrenMaxCountValidation.getItemAt(index) as ValidationResultFlag;
		}
		
		public function isRelevant(childDef:NodeDefinitionProxy):Boolean {
			var index:int = getChildDefinitionIndex(childDef);
			return childrenRelevance.getItemAt(index) as Boolean;
		}
		
		override public function hasErrors():Boolean {
			var children:IList = getChildren();
			for each(var child:NodeProxy in children){
				if( child.hasErrors() ) {
					return true;
				}
			}
			return false;
		}
		
		public function childContainsErrors(childDef:NodeDefinitionProxy):Boolean {
			var children:IList = getChildren(childDef);
			for each(var child:NodeProxy in children){
				if( child.hasErrors() ) {
					return true;
				}
			}
			return false;
		}
		
		public function hasConfirmedError(childDef:NodeDefinitionProxy):Boolean {
			var children:IList = getChildren(childDef);
			for each(var child:NodeProxy in children){
				if(child is AttributeProxy){
					var attr:AttributeProxy = child as AttributeProxy;
					if( !attr.errorConfirmed ){
						return false;
					}
				} else {
					return false;
				}
			}
			return true;
		}
		
		public function getEnumeratedCodeWidth(entityName:String):Number {
			var result:Number = _enumeratedEntitiesCodeWidths[entityName];
			return result;
		}
		
		public function getCount(childDef:NodeDefinitionProxy):int {
			var children:IList = getChildren(childDef);
			return children.length;
		}
		
		override public function get empty():Boolean {
			var children:IList = getChildren();
			for each (var child:NodeProxy in children ) {
				if ( ! child.empty ) {
					return false;
				}
			}
			return true;
		}
		
		public function hasDescendantWithBlankField():Boolean {
			var children:IList = getChildren();
			var nodes:Array = children.toArray();
			while ( nodes.length > 0 ) {
				var node:NodeProxy = NodeProxy(nodes.pop());
				if ( node is AttributeProxy && AttributeProxy(node).hasBlankField() ) {
					return true;
				} else if (node is EntityProxy) {
					var descendants:IList = EntityProxy(node).getChildren();
					nodes = nodes.concat(descendants.toArray());
				}
			}
			return false;
		}
		
		/**
		 * Returns the first descendant node having the specified node definition.
		 */
		public function getDescendantNodes(nodeDefn:NodeDefinitionProxy):IList {
			var pathDiff:String = nodeDefn.path.substr(this.definition.path.length + 1);
			var currentParentEntities:IList = new ArrayCollection();
			currentParentEntities.addItem(this);
			var currentLevelChildren:IList = new ArrayCollection();
			var parts:Array = pathDiff.split("/");
			for ( var count:int = 0; count < parts.length; count++ ) {
				var part:String = parts[count];
				for each (var currentParentEntity:EntityProxy in currentParentEntities ) {
					var currentParentEntityDefn:EntityDefinitionProxy = EntityDefinitionProxy(currentParentEntity.definition);
					var partNodeDef:NodeDefinitionProxy = currentParentEntityDefn.getChildDefinition(part);
					var currentChildren:IList = currentParentEntity.getChildren(partNodeDef);
					CollectionUtil.addAll(currentLevelChildren, currentChildren);
				}
				if ( currentLevelChildren.length == 0 ) {
					return null; //node not found
				} else if ( count == parts.length - 1 ) {
					return currentLevelChildren;
				} else {
					currentParentEntities = currentLevelChildren;
					currentLevelChildren = new ArrayCollection();
				}
			}
			//node not found
			return null;
		}
		
		public function getDescendantCousins(nodeDefn:NodeDefinitionProxy):IList {
			var result:IList = new ArrayCollection();
			var siblingEntities:IList = getSiblings();
			for each (var entity:EntityProxy in siblingEntities) {
				var cousins:IList = entity.getDescendantNodes(nodeDefn);
				CollectionUtil.addAll(result, cousins);
			}
			return result;
		}
		
		public function isDescendantCousin(node:NodeProxy):Boolean {
			var cousins:IList = this.getDescendantCousins(node.definition);
			return CollectionUtil.contains(cousins, node);
		}
		
		public function isAncestorOf(node:NodeProxy):Boolean {
			var currentParent:EntityProxy = node.parent;
			while ( currentParent != null ) {
				if ( currentParent == this ) {
					return true;
				}
				currentParent = currentParent.parent;
			}
			return false;
		}
		
		protected function updateList(list:IList, newMap:IMap):void {
			if(list != null && newMap != null) {
				var defIds:IList = newMap.keySet;
				for each(var defId:int in defIds) {
					var childDef:NodeDefinitionProxy = EntityDefinitionProxy(definition).getChildDefinitionById(defId);
					//childDef can be null if the node is hidden in the ui or not applicable for the current model version
					if (childDef != null) {
						var index:int = getChildDefinitionIndex(childDef);
						var value:Object = newMap.get(defId);
						if (value != null) {
							list.setItemAt(value, index);
						}
					}
				}
			}
		}
		
		private function getChildDefinitionIndex(childDefn:NodeDefinitionProxy):int {
			var version:ModelVersionProxy = this.record.version;
			var defs:IList = EntityDefinitionProxy(definition).getDefinitionsInVersion(version);
			var index:int = CollectionUtil.getItemIndex(defs, "id", childDefn.id);
			return index;
		}
		
		/*
		* GETTERS AND SETTERS
		*/
		public function get keyText():String {
			return _keyText;
		}
		
		public function set keyText(value:String):void {
			_keyText = value;
		}
		
		public function get fullKeyText():String {
			return _fullKeyText;
		}
		
		public function set fullKeyText(value:String):void {
			_fullKeyText = value;
		}
		
		public function get enumeratedEntitiesCodeWidths():Array {
			return _enumeratedEntitiesCodeWidths;
		}

		public function set enumeratedEntitiesCodeWidths(value:Array):void {
			_enumeratedEntitiesCodeWidths = value;
		}

	}
}