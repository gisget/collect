package org.openforis.collect.ui
{
	import flash.display.DisplayObjectContainer;
	import flash.display.InteractiveObject;
	import flash.events.ContextMenuEvent;
	import flash.ui.ContextMenu;
	import flash.ui.ContextMenuItem;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.controls.Alert;
	import mx.messaging.management.Attribute;
	import mx.rpc.AsyncResponder;
	import mx.rpc.events.ResultEvent;
	
	import org.openforis.collect.Application;
	import org.openforis.collect.client.ClientFactory;
	import org.openforis.collect.event.ApplicationEvent;
	import org.openforis.collect.event.EventDispatcherFactory;
	import org.openforis.collect.i18n.Message;
	import org.openforis.collect.metamodel.proxy.AttributeDefinitionProxy;
	import org.openforis.collect.metamodel.proxy.CodeAttributeDefinitionProxy;
	import org.openforis.collect.metamodel.proxy.EntityDefinitionProxy;
	import org.openforis.collect.model.FieldSymbol;
	import org.openforis.collect.model.proxy.AttributeProxy;
	import org.openforis.collect.model.proxy.EntityProxy;
	import org.openforis.collect.model.proxy.FieldProxy;
	import org.openforis.collect.model.proxy.NodeProxy;
	import org.openforis.collect.model.proxy.NodeStateProxy;
	import org.openforis.collect.model.proxy.RecordProxy$Step;
	import org.openforis.collect.presenter.RemarksPopUpPresenter;
	import org.openforis.collect.remoting.service.UpdateRequest;
	import org.openforis.collect.remoting.service.UpdateRequest$Method;
	import org.openforis.collect.ui.component.detail.CollectFormItem;
	import org.openforis.collect.ui.component.detail.EntityDataGroupItemRenderer;
	import org.openforis.collect.ui.component.input.InputField;
	import org.openforis.collect.ui.component.input.RemarksPopUp;
	import org.openforis.collect.util.AlertUtil;

	public class ContextMenuBuilder {
		
		private static const BLANK_ON_FORM_MENU_ITEM:ContextMenuItem = new ContextMenuItem(Message.get("edit.contextMenu.blankOnForm"));
		
		private static const DASH_ON_FORM_MENU_ITEM:ContextMenuItem = new ContextMenuItem(Message.get("edit.contextMenu.dashOnForm"));
		
		private static const ILLEGIBLE_MENU_ITEM:ContextMenuItem = new ContextMenuItem(Message.get("edit.contextMenu.illegible"));
		
		private static const EDIT_REMARKS_MENU_ITEM:ContextMenuItem = new ContextMenuItem(Message.get("edit.contextMenu.editRemarks"), true);
		
		private static const REPLACE_BLANKS_WITH_DASH_MENU_ITEM:ContextMenuItem = new ContextMenuItem(Message.get("edit.contextMenu.replaceBlanksWithDash"), true);
		
		private static const REPLACE_BLANKS_WITH_STAR_MENU_ITEM:ContextMenuItem = new ContextMenuItem(Message.get("edit.contextMenu.replaceBlanksWithStar"));
		
		private static const DELETE_ATTRIBUTE_MENU_ITEM:ContextMenuItem = new ContextMenuItem(Message.get("edit.contextMenu.deleteAttribute"), true);
		
		private static const DELETE_ENTITY_MENU_ITEM:ContextMenuItem = new ContextMenuItem(Message.get("edit.contextMenu.deleteEntity"), true);
		
		private static const APPROVE_ERROR_MENU_ITEM:ContextMenuItem = new ContextMenuItem(Message.get("edit.contextMenu.approveError"), true);
		
		private static const APPROVE_MISSING_VALUE_MENU_ITEM:ContextMenuItem = new ContextMenuItem(Message.get("edit.contextMenu.approveMissingValue"), true);
		
		private static const APPROVE_MISSING_VALUES_IN_ROW_MENU_ITEM:ContextMenuItem = new ContextMenuItem(Message.get("edit.contextMenu.approveMissingValuesInRow"), true);
		
		private static const all:Array = [
			BLANK_ON_FORM_MENU_ITEM, 
			DASH_ON_FORM_MENU_ITEM, 
			ILLEGIBLE_MENU_ITEM, 
			EDIT_REMARKS_MENU_ITEM, 
			REPLACE_BLANKS_WITH_DASH_MENU_ITEM,
			REPLACE_BLANKS_WITH_STAR_MENU_ITEM, 
			DELETE_ATTRIBUTE_MENU_ITEM, 
			DELETE_ENTITY_MENU_ITEM, 
			APPROVE_ERROR_MENU_ITEM, 
			APPROVE_MISSING_VALUE_MENU_ITEM
		];
		
		private static var remarksPopUpPresenter:RemarksPopUpPresenter;
		
		private static var currentInputField:InputField;

		{
			initStatics();
		}

		private static function initStatics():void {
			//init context menu items' event listener
			var item:ContextMenuItem;
			for each (item in all)  {
				item.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, menuItemSelectHandler);
			}
			//init remarks popup presenter
			remarksPopUpPresenter = new RemarksPopUpPresenter();
		}
		
		public static function buildContextMenu(inputField:InputField):ContextMenu {
			var step:RecordProxy$Step = Application.activeRecord.step;
			var cm:ContextMenu = new ContextMenu();
			var items:Array = new Array();
			
			addValueItems(items, step, inputField);
			
			addRowItems(items, step, inputField);
			
			addApproveValueItems(items, step, inputField);
			
			cm.customItems = items;
			cm.hideBuiltInItems();
			return cm;
		}
		
		private static function addValueItems(currentItems:Array, step:RecordProxy$Step, inputField:InputField):void {
			if(inputField.isEmpty()) {
				currentItems.push(
					BLANK_ON_FORM_MENU_ITEM,
					DASH_ON_FORM_MENU_ITEM,
					ILLEGIBLE_MENU_ITEM
				);
			}
			currentItems.push(EDIT_REMARKS_MENU_ITEM);
		}
		
		private static function addRowItems(currentItems:Array, step:RecordProxy$Step, inputField:InputField):void {
			var def:AttributeDefinitionProxy = inputField.attributeDefinition;
			if(def != null && inputField.isInDataGroup) {
				if(def.multiple && ! (def is CodeAttributeDefinitionProxy)) {
					currentItems.push(DELETE_ATTRIBUTE_MENU_ITEM);
				}
				var entityDef:EntityDefinitionProxy = def.parent;
				if(entityDef != null && entityDef.multiple) {
					currentItems.push(
						REPLACE_BLANKS_WITH_DASH_MENU_ITEM, 
						REPLACE_BLANKS_WITH_STAR_MENU_ITEM
					);
					if( !entityDef.enumerated) {
						currentItems.push(DELETE_ENTITY_MENU_ITEM);
					}
				}
			}			
		}
		
		private static function addApproveValueItems(currentItems:Array, step:RecordProxy$Step, inputField:InputField):void {
			var attribute:AttributeProxy = inputField.attribute;
			if(attribute != null) {
				var state:NodeStateProxy = attribute.state;
				if(step == RecordProxy$Step.ENTRY && state != null && state.hasErrors()) {
					currentItems.push(APPROVE_ERROR_MENU_ITEM);
				}
			}
		}
		
		public static function menuItemSelectHandler(event:ContextMenuEvent):void {
			var owner:InteractiveObject = event.contextMenuOwner;
			if(owner is InputField) {
				var field:InputField = InputField(owner);
				var parentEntity:EntityProxy = field.parentEntity;
				currentInputField = field;
				switch(event.target) {
					case BLANK_ON_FORM_MENU_ITEM:
						field.applyChanges(FieldSymbol.BLANK_ON_FORM);
						break;
					case DASH_ON_FORM_MENU_ITEM:
						field.applyChanges(FieldSymbol.DASH_ON_FORM);
						break;
					case ILLEGIBLE_MENU_ITEM:
						field.applyChanges(FieldSymbol.ILLEGIBLE);
						break;
					case EDIT_REMARKS_MENU_ITEM:
						remarksPopUpPresenter.openPopUp(field, true);
						break;
					case REPLACE_BLANKS_WITH_DASH_MENU_ITEM:
						setReasonBlankInChildren(parentEntity, FieldSymbol.DASH_ON_FORM);
						break;
					case REPLACE_BLANKS_WITH_STAR_MENU_ITEM:
						setReasonBlankInChildren(parentEntity, FieldSymbol.BLANK_ON_FORM);
						break;
					case DELETE_ATTRIBUTE_MENU_ITEM:
						AlertUtil.showConfirm("global.confirmDelete", [field.attributeDefinition.getLabelText()], "global.confirmAlertTitle", performDeleteAttribute);
						break;
					case DELETE_ENTITY_MENU_ITEM:
						AlertUtil.showConfirm("edit.confirmDeleteEntity", null, "global.confirmAlertTitle", performDeleteEntity);
						break;
					case APPROVE_ERROR_MENU_ITEM:
						field.applyChanges(FieldSymbol.CONFIRMED);
						break
				}
			}
		}
		
		protected static function performDeleteAttribute():void {
			var def:AttributeDefinitionProxy = currentInputField.attributeDefinition;
			var req:UpdateRequest = new UpdateRequest();
			req.parentEntityId = currentInputField.parentEntity.id;
			req.nodeName = def.name;
			if(currentInputField.attribute != null) {
				req.nodeId = currentInputField.attribute.id;
			}
			req.method = UpdateRequest$Method.DELETE;
			
			var responder:AsyncResponder = new AsyncResponder(updateFieldResultHandler, null);
			ClientFactory.dataClient.updateActiveRecord(responder, req);
		}
		
		protected static function performDeleteEntity():void {
			var entity:EntityProxy = currentInputField.parentEntity;
			var req:UpdateRequest = new UpdateRequest();
			req.nodeName = entity.name;
			req.nodeId = entity.id;
			req.parentEntityId = entity.parentId;
			req.method = UpdateRequest$Method.DELETE;
			
			var responder:AsyncResponder = new AsyncResponder(updateFieldResultHandler, null);
			ClientFactory.dataClient.updateActiveRecord(responder, req);
		}
		
		public static function setReasonBlankInChildren(entity:EntityProxy, symbol:FieldSymbol):void {
			var req:UpdateRequest = new UpdateRequest();
			req.parentEntityId = entity.parentId;
			req.nodeName = entity.name;
			req.symbol = symbol;
			req.nodeId = entity.id;
			req.method = UpdateRequest$Method.UPDATE_SYMBOL;
			var responder:AsyncResponder = new AsyncResponder(updateFieldResultHandler, null);
			ClientFactory.dataClient.updateActiveRecord(responder, req);
		}
		
		protected static function updateFieldResultHandler(event:ResultEvent, token:Object = null):void {
			var result:IList = event.result as IList;
			Application.activeRecord.update(result);
			var appEvt:ApplicationEvent = new ApplicationEvent(ApplicationEvent.UPDATE_RESPONSE_RECEIVED);
			appEvt.result = result;
			EventDispatcherFactory.getEventDispatcher().dispatchEvent(appEvt);
		}
		

	}
}