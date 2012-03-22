package org.openforis.collect.presenter {
	import flash.events.Event;
	import flash.events.FocusEvent;
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	
	import mx.binding.utils.ChangeWatcher;
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.rpc.events.ResultEvent;
	
	import org.openforis.collect.client.ClientFactory;
	import org.openforis.collect.client.DataClient;
	import org.openforis.collect.client.UpdateRequestToken;
	import org.openforis.collect.event.ApplicationEvent;
	import org.openforis.collect.event.InputFieldEvent;
	import org.openforis.collect.event.NodeEvent;
	import org.openforis.collect.metamodel.proxy.AttributeDefinitionProxy;
	import org.openforis.collect.model.FieldSymbol;
	import org.openforis.collect.model.proxy.AttributeProxy;
	import org.openforis.collect.model.proxy.EntityProxy;
	import org.openforis.collect.model.proxy.FieldProxy;
	import org.openforis.collect.model.proxy.NodeProxy;
	import org.openforis.collect.remoting.service.UpdateRequest;
	import org.openforis.collect.remoting.service.UpdateRequestOperation;
	import org.openforis.collect.remoting.service.UpdateRequestOperation$Method;
	import org.openforis.collect.remoting.service.UpdateResponse;
	import org.openforis.collect.ui.component.input.InputField;
	import org.openforis.collect.ui.component.input.InputFieldContextMenu;
	import org.openforis.collect.util.ArrayUtil;
	import org.openforis.collect.util.StringUtil;
	import org.openforis.collect.util.UIUtil;
	
	/**
	 * 
	 * @author M. Togna
	 * @author S. Ricci
	 * */
	public class InputFieldPresenter extends AbstractPresenter {
		
		public static const SHORTCUT_BLANK_ON_FORM:String = "*";
		public static const SHORTCUT_DASH_ON_FORM:String = "-";
		public static const SHORTCUT_ILLEGIBLE:String = "?";
		
		public static const REASON_BLANK_SHORTCUTS:Array = [SHORTCUT_BLANK_ON_FORM, SHORTCUT_DASH_ON_FORM, SHORTCUT_ILLEGIBLE];
		public static const REASON_BLANK_SYMBOLS:Array = [FieldSymbol.BLANK_ON_FORM, FieldSymbol.DASH_ON_FORM, FieldSymbol.ILLEGIBLE];
		
		private var _view:InputField;
		private var _changed:Boolean = false;
		private var _contextMenu:InputFieldContextMenu;
		
		private static var _dataClient:DataClient;
		
		/*
			Static initialization
		*/
		{
			_dataClient = ClientFactory.dataClient;
			eventDispatcher.addEventListener(NodeEvent.UPDATE_SYMBOL, updateSymbolHandler);
			eventDispatcher.addEventListener(NodeEvent.UPDATE_REMARKS, updateRemarksHandler);
			eventDispatcher.addEventListener(NodeEvent.DELETE_NODE, deleteNodeHandler);
			eventDispatcher.addEventListener(NodeEvent.CONFIRM_ERROR, confirmErrorHandler);
			eventDispatcher.addEventListener(NodeEvent.APPROVE_MISSING, approveMissingHandler);
		}
		
		public function InputFieldPresenter(inputField:InputField) {
			_view = inputField;
			_contextMenu = new InputFieldContextMenu(_view);
			super();
			//updateView();
		}
		
		override internal function initEventListeners():void {
			super.initEventListeners();
			
			eventDispatcher.addEventListener(ApplicationEvent.UPDATE_RESPONSE_RECEIVED, updateResponseReceivedHandler);
			
			if(_view.textInput != null) {
				_view.textInput.addEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);
				_view.textInput.addEventListener(Event.CHANGE, changeHandler);
				_view.textInput.addEventListener(FocusEvent.FOCUS_OUT, focusOutHandler);
				_view.textInput.addEventListener(FocusEvent.FOCUS_IN, focusInHandler);
			}
			
			ChangeWatcher.watch(_view, "attribute", attributeChangeHandler);
		}
		
		protected static function approveMissingHandler(event:NodeEvent): void {
			var updRequest:UpdateRequest = new UpdateRequest();
			var node:NodeProxy = event.nodeProxy;
			if(node == null) {
				var nodes:IList = event.nodes;
				if(nodes != null && nodes.length == 1) {
					//approve only first node, if it's a missing value it should be an empty node
					node = nodes.getItemAt(0) as NodeProxy;
				}
			}
			var updFields:ArrayCollection = prepareApproveMissingRequests(updRequest, node, event.fieldIdx);
			var token:UpdateRequestToken = new UpdateRequestToken(UpdateRequestToken.APPROVE_MISSING);
			token.updatedFields = updFields;
			token.symbol = event.symbol;
			
			_dataClient.updateActiveRecord(updRequest, token, null, faultHandler);
		}
		
		protected static function updateRemarksHandler(event:NodeEvent): void {
			var updRequest:UpdateRequest = new UpdateRequest();
			var operation:UpdateRequestOperation;
			var field:FieldProxy;
			var fieldIdx:int;
			var updFields:IList = new ArrayCollection();
			if(event.nodeProxy != null) {
				var attribute:AttributeProxy = AttributeProxy(event.nodeProxy);
				if(event.fieldIdx >= 0) {
					field = prepareUpdateRemarksRequest(updRequest, attribute, event.remarks, event.fieldIdx);
					updFields.addItem(field);
				} else {
					for (fieldIdx = 0; fieldIdx < attribute.fields.length; fieldIdx++) {
						field = prepareUpdateRemarksRequest(updRequest, attribute, event.remarks, fieldIdx);
						updFields.addItem(field);
					}
				}
			} else {
				//considering nodes as attributes
				var attributes:IList = event.nodes;
				for each (attribute in attributes) {
					for (fieldIdx = 0; fieldIdx < attribute.fields.length; fieldIdx++) {
						field = prepareUpdateRemarksRequest(updRequest, attribute, event.remarks, fieldIdx);
						updFields.addItem(field);
					}
				}
			}
			var token:UpdateRequestToken = new UpdateRequestToken(UpdateRequestToken.UPDATE_REMARKS);
			token.remarks = event.remarks;
			token.updatedFields = updFields;
			_dataClient.updateActiveRecord(updRequest, token, null, faultHandler);
		}
		
		protected static function prepareUpdateRemarksRequest(req:UpdateRequest, node:NodeProxy, remarks:String, fieldIdx:Number = NaN):FieldProxy {
			var operation:UpdateRequestOperation = new UpdateRequestOperation();
			operation.method = UpdateRequestOperation$Method.UPDATE_REMARKS;
			operation.remarks = remarks;
			operation.nodeId = node.id;
			operation.parentEntityId = node.parentId;
			operation.fieldIndex = fieldIdx;
			req.addOperation(operation);
			var updatedField:FieldProxy = AttributeProxy(node).fields[fieldIdx];
			return updatedField;
		}
		
		protected static function updateSymbolHandler(event:NodeEvent): void {
			var updRequest:UpdateRequest = new UpdateRequest();
			
			var updFields:ArrayCollection = prepareUpdateSymbolRequests(updRequest, event.nodeProxy, event.symbol, event.fieldIdx);
			var token:UpdateRequestToken = new UpdateRequestToken(UpdateRequestToken.UPDATE_SYMBOL);
			token.updatedFields = updFields;
			token.symbol = event.symbol;
			
			_dataClient.updateActiveRecord(updRequest, token, null, faultHandler);
		}
		
		protected static function prepareUpdateSymbolRequests(updateRequest:UpdateRequest, nodeProxy:NodeProxy, symbol:FieldSymbol, fieldIdx:Number):ArrayCollection {
			var updFields:ArrayCollection = new ArrayCollection();
			
			if( nodeProxy is EntityProxy ){
				var entity:EntityProxy = nodeProxy as EntityProxy;
				var children:IList = entity.getChildren();
				for each (var child:NodeProxy in children) {
					updFields.addAll( prepareUpdateSymbolRequests(updateRequest, child, symbol, fieldIdx) );
				}
			} else {
				var attr:AttributeProxy = AttributeProxy(nodeProxy);
				var operation:UpdateRequestOperation;
				var field:FieldProxy;
				if(isNaN(fieldIdx) || fieldIdx < 0){
					for(var index:int = 0; index < attr.fields.length; index ++) {
						field = attr.fields[index];
						if(field.value == null && field.symbol == null) {
							operation = new UpdateRequestOperation();
							operation.method = UpdateRequestOperation$Method.UPDATE;
							operation.parentEntityId = nodeProxy.parentId;
							operation.nodeName = nodeProxy.name;
							operation.nodeId = nodeProxy.id;
							operation.fieldIndex = index;
							operation.remarks = field.remarks;
							operation.value = field.value != null ? field.value.toString(): null;
							operation.symbol = symbol;
							
							updateRequest.addOperation(operation);
							updFields.addItem(field);
						}
					}
				} else {
					field = attr.fields[fieldIdx];
					if(field.value == null && field.symbol == null) {
						operation = new UpdateRequestOperation();
						operation.method = UpdateRequestOperation$Method.UPDATE;
						operation.parentEntityId = nodeProxy.parentId;
						operation.nodeName = nodeProxy.name;
						operation.nodeId = nodeProxy.id;
						operation.fieldIndex = fieldIdx;
						operation.remarks = field.remarks;
						operation.value = field.value != null ? field.value.toString(): null;
						operation.symbol = symbol;
						
						updateRequest.addOperation(operation);
						updFields.addItem(field);
					}
				}
			}
			return updFields;
		}

		protected static function prepareApproveMissingRequests(updateRequest:UpdateRequest, nodeProxy:NodeProxy, fieldIdx:Number):ArrayCollection {
			var updFields:ArrayCollection = new ArrayCollection();
			
			if( nodeProxy is EntityProxy ){
				var entity:EntityProxy = nodeProxy as EntityProxy;
				var children:IList = entity.getChildren();
				for each (var child:NodeProxy in children) {
					updFields.addAll( prepareApproveMissingRequests(updateRequest, child, fieldIdx) );
				}
			} else {
				var attr:AttributeProxy = AttributeProxy(nodeProxy);
				var operation:UpdateRequestOperation;
				var field:FieldProxy;
				if(isNaN(fieldIdx) || fieldIdx < 0){
					for(var index:int = 0; index < attr.fields.length; index ++) {
						field = attr.fields[index];
						if(field.value == null && field.symbol == null) {
							operation = new UpdateRequestOperation();
							operation.method = UpdateRequestOperation$Method.APPROVE_MISSING;
							operation.parentEntityId = nodeProxy.parentId;
							operation.nodeName = nodeProxy.name;
							operation.nodeId = nodeProxy.id;
							operation.fieldIndex = index;
							updateRequest.addOperation(operation);
							updFields.addItem(field);
						}
					}
				} else {
					field = attr.fields[fieldIdx];
					if(field.value == null && field.symbol == null) {
						operation = new UpdateRequestOperation();
						operation.method = UpdateRequestOperation$Method.APPROVE_MISSING;
						operation.parentEntityId = nodeProxy.parentId;
						operation.nodeName = nodeProxy.name;
						operation.nodeId = nodeProxy.id;
						operation.fieldIndex = fieldIdx;
						updateRequest.addOperation(operation);
						updFields.addItem(field);
					}
				}
			}
			return updFields;
		}
		
		protected static function confirmErrorHandler(event:NodeEvent): void {
			var updRequestOp:UpdateRequestOperation = new UpdateRequestOperation();
			updRequestOp.method = UpdateRequestOperation$Method.CONFIRM_ERROR;
			updRequestOp.nodeId = event.nodeProxy.id;
			updRequestOp.parentEntityId = event.nodeProxy.parentId;
			
			var updRequest:UpdateRequest = new UpdateRequest(updRequestOp);
			var token:UpdateRequestToken = new UpdateRequestToken(UpdateRequestToken.CONFIRM_ERROR);
			_dataClient.updateActiveRecord(updRequest, token, null, faultHandler);
		}
		
		protected static function deleteNodeHandler(event:NodeEvent):void {
			var node:NodeProxy = event.nodeProxy;
			var updRequestOp:UpdateRequestOperation = new UpdateRequestOperation();
			updRequestOp.method = UpdateRequestOperation$Method.DELETE;
			updRequestOp.parentEntityId = node.parentId;
			updRequestOp.nodeId = node.id;
			
			var updRequest:UpdateRequest = new UpdateRequest(updRequestOp);
			var token:UpdateRequestToken = new UpdateRequestToken(UpdateRequestToken.CONFIRM_ERROR);
			_dataClient.updateActiveRecord(updRequest, token, null, faultHandler);
		}
		
		protected function updateResponseReceivedHandler(event:ApplicationEvent):void {
			if(_view.attribute != null) {
				var responses:IList = IList(event.result);
				for each (var response:UpdateResponse in responses) {
					if(response.nodeId == _view.attribute.id) {
						updateView();
						return;
					}
				}
			}
		}
		
		protected function attributeChangeHandler(event:Event):void {
			changed = false;
			_view.visited = false;
			_view.updating = false;
			updateView();
		}
		
		protected function changeHandler(event:Event):void {
			//TODO if autocomplete enabled show autocomplete popup...
			changed = true;
			var inputFieldEvent:InputFieldEvent = new InputFieldEvent(InputFieldEvent.CHANGING);
			_view.dispatchEvent(inputFieldEvent);
		}
		
		protected function focusInHandler(event:FocusEvent):void {
			UIUtil.ensureElementIsVisible(event.target);
		}
		
		protected function focusOutHandler(event:FocusEvent):void {
			if(_view.applyChangesOnFocusOut && changed) {
				updateValue();
			}
			_view.visited = true;
			var inputFieldEvent:InputFieldEvent = new InputFieldEvent(InputFieldEvent.VISITED);
			inputFieldEvent.inputField = _view;
			eventDispatcher.dispatchEvent(inputFieldEvent);
		}
		
		protected function keyDownHandler(event:KeyboardEvent):void {
			var keyCode:uint = event.keyCode;
			switch(keyCode) {
				case Keyboard.ESCAPE:
					undoLastChange();
					break;
			}
		}
		
		public function undoLastChange():void {
			changed = false;
			updateView();
		}
		
		public function updateValue():void {
			var o:UpdateRequestOperation = createUpdateValueOperation();
			var value:String = null;
			var text:String = textToRequestValue();
			var symbol:FieldSymbol = null;
			if(isShortCutForReasonBlank(text)) {
				symbol = parseShortCutForReasonBlank(text);
			} else {
				value = text;
			}
			var token:UpdateRequestToken = new UpdateRequestToken(UpdateRequestToken.UPDATE_VALUE);
			token.updatedFields = new ArrayCollection([getField()]);
			token.symbol = symbol;
			sendUpdateRequest(o, token);
		}
		
		public function createUpdateValueOperation():UpdateRequestOperation {
			var symbol:FieldSymbol = null;
			var value:String = null;
			var text:String = textToRequestValue();
			if(isShortCutForReasonBlank(text)) {
				symbol = parseShortCutForReasonBlank(text);
			} else {
				value = text;
			}
			var remarks:String = getRemarks(); //preserve old remarks
			var o:UpdateRequestOperation = getUpdateFieldOperation(value, symbol, remarks);
			return o;
		}
		/*
		public function applySymbol(symbol:FieldSymbol):void {
			var o:UpdateRequestOperation = getApplySymbolOperation(symbol);
			var token:UpdateRequestToken = new UpdateRequestToken(UpdateRequestToken.TYPE_UPDATE_SYMBOL);
			token.updatedFields = new ArrayCollection([getField()]);
			token.symbol = symbol;
			sendUpdateRequest(o, token);
		}
		
		public function applyRemarks(remarks:String):void {
			var o:UpdateRequestOperation = getApplyRemarksOperation(remarks);
			var token:UpdateRequestToken = new UpdateRequestToken(UpdateRequestToken.TYPE_UPDATE_REMARKS);
			token.updatedFields = new ArrayCollection([getField()]);
			token.remarks = remarks;
			sendUpdateRequest(o, token);
		}

		public function getApplySymbolOperation(symbol:FieldSymbol):UpdateRequestOperation {
			var value:String = null;
			if(! isReasonBlankSymbol(symbol)) {
				value = textToRequestValue(); //preserve old value
			}
			var remarks:String = getRemarks(); //preserve old remarks
			var o:UpdateRequestOperation = getUpdateFieldOperation(value, symbol, remarks);
			return o;
		}
		
		public function getApplyRemarksOperation(remarks:String):UpdateRequestOperation {
			var value:String = null;
			var symbol:FieldSymbol = getSymbol(); //preserve old symbol
			if(! isReasonBlankSymbol(symbol)) {
				value = textToRequestValue(); //preserve old value
			}
			var o:UpdateRequestOperation = getUpdateFieldOperation(value, symbol, remarks);
			return o;
		}
		*/
		protected function getUpdateFieldOperation(value:String, symbol:FieldSymbol = null, remarks:String = null):UpdateRequestOperation {
			var nodeId:Number = _view.attribute != null ? _view.attribute.id: NaN;
			var o:UpdateRequestOperation = getUpdateRequestOperation(UpdateRequestOperation$Method.UPDATE, nodeId, value, symbol, remarks);
			return o;
		}
		
		protected function sendUpdateRequest(o:UpdateRequestOperation, token:UpdateRequestToken):void {
			var req:UpdateRequest = new UpdateRequest(o);
			dataClient.updateActiveRecord(req, token, updateResultHandler, faultHandler);
			_view.updating = true;
		}
		
		protected function getUpdateRequestOperation(method:UpdateRequestOperation$Method, nodeId:Number, 
								 value:String = null, symbol:FieldSymbol = null, remarks:String = null):UpdateRequestOperation {
			var o:UpdateRequestOperation = new UpdateRequestOperation();
			var def:AttributeDefinitionProxy = _view.attributeDefinition;
			o.method = method;
			o.parentEntityId = _view.parentEntity.id;
			o.nodeName = def.name;
			o.nodeId = nodeId;
			o.fieldIndex = _view.fieldIndex;
			o.value = value;
			o.symbol = symbol;
			o.remarks = remarks;
			return o;
		}
		
		protected function updateResultHandler(event:ResultEvent, token:UpdateRequestToken):void {
			changed = false;
			_view.updating = false;
			//_view.currentState = InputField.STATE_SAVE_COMPLETE;
		}
		
		protected function getTextFromValue():String {
			var attribute:AttributeProxy = _view.attribute;
			if(attribute != null) {
				var field:FieldProxy = _view.attribute.getField(_view.fieldIndex);
				if(field.symbol != null) {
					var shortKey:String = getShortCutForReasonBlank(field.symbol);
					if(shortKey != null) {
						return shortKey;
					}
				}
				var value:Object = field.value;
				if(value != null && StringUtil.isNotBlank(value.toString())) {
					return value.toString();
				}
			}
			return "";
		}

		protected function textToRequestValue():String {
			var result:String = null;
			var text:String = _view.text;
			if(StringUtil.isNotBlank(text)) {
				result = StringUtil.trim(text);
			}
			return result;
		}
		
		protected function updateView():void {
			//update view according to attribute (generic text value)
			var hasRemarks:Boolean = false;
			if(_view.attributeDefinition != null) {
				var text:String = getTextFromValue();
				_view.text = text;
				hasRemarks = StringUtil.isNotBlank(getRemarks());
				_contextMenu.updateContextMenuItems();
			}
			_view.hasRemarks = hasRemarks;
		}
		
		protected function getField():FieldProxy {
			if(_view.attribute != null) {
				var fieldIndex:int = 0;
				if(_view.fieldIndex >= 0) {
					fieldIndex = _view.fieldIndex;
				}
				return _view.attribute.getField(fieldIndex);
			}
			return null;
		}
		
		protected function getRemarks():String {
			var f:FieldProxy = getField();
			if(f != null) {
				return f.remarks;
			} 
			return null;
		}
		
		protected function getSymbol():FieldSymbol {
			var f:FieldProxy = getField();
			if(f != null) {
				return f.symbol;
			} 
			return null;
		}
		
		public static function getShortCutForReasonBlank(symbol:FieldSymbol):String {
			switch(symbol) {
				case FieldSymbol.BLANK_ON_FORM:
					return SHORTCUT_BLANK_ON_FORM;
				case FieldSymbol.DASH_ON_FORM:
					return SHORTCUT_DASH_ON_FORM;
				case FieldSymbol.ILLEGIBLE:
					return SHORTCUT_ILLEGIBLE;
				default:
					return null;
			}
		}
		
		public static function parseShortCutForReasonBlank(text:String):FieldSymbol {
			switch(text) {
				case SHORTCUT_BLANK_ON_FORM:
					return FieldSymbol.BLANK_ON_FORM;
				case SHORTCUT_DASH_ON_FORM:
					return FieldSymbol.DASH_ON_FORM;
				case SHORTCUT_ILLEGIBLE:
					return FieldSymbol.ILLEGIBLE;
				default:
					return null;
			}
		}
		
		public static function isShortCutForReasonBlank(text:String):Boolean {
			return ArrayUtil.isIn(REASON_BLANK_SHORTCUTS, text);
		}
		
		public static function isReasonBlankSymbol(symbol:FieldSymbol):Boolean {
			return ArrayUtil.isIn(REASON_BLANK_SYMBOLS, symbol);
		}
		
		protected function get dataClient():DataClient {
			return _dataClient;
		}

		[Bindable]
		protected function get changed():Boolean {
			return _changed;
		}
		
		protected function set changed(value:Boolean):void {
			_changed = value;
		}
		
	}
}
