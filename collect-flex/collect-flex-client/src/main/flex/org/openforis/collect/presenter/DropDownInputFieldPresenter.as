package org.openforis.collect.presenter {
	import flash.events.Event;
	import flash.events.FocusEvent;
	import flash.events.KeyboardEvent;
	
	import mx.binding.utils.ChangeWatcher;
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.events.PropertyChangeEvent;
	
	import org.openforis.collect.Application;
	import org.openforis.collect.event.UIEvent;
	import org.openforis.collect.i18n.Message;
	import org.openforis.collect.model.CollectRecord$Step;
	import org.openforis.collect.model.FieldSymbol;
	import org.openforis.collect.model.proxy.AttributeProxy;
	import org.openforis.collect.model.proxy.FieldProxy;
	import org.openforis.collect.ui.component.input.DropDownInputField;
	import org.openforis.collect.util.CollectionUtil;
	import org.openforis.collect.util.ObjectUtil;
	
	/**
	 * 
	 * @author S. Ricci
	 * */
	public class DropDownInputFieldPresenter extends InputFieldPresenter {
		
		public static const EMPTY_ITEM:Object = {label: Message.get('global.dropDownEmpty'), separator: true};
		public static const BLANK_ON_FORM_ITEM:Object = {label: Message.get('edit.dropDownList.blankOnForm'), shortCut: "*", separator: true};
		public static const DASH_ON_FORM_ITEM:Object = {label: Message.get('edit.dropDownList.dashOnForm'), shortCut: "-"};
		public static const ILLEGIBLE_ITEM:Object = {label: Message.get('edit.dropDownList.illegible'), shortCut: "?"};

		private var _view:DropDownInputField;
		
		public function DropDownInputFieldPresenter(inputField:DropDownInputField) {
			_view = inputField;
			
			super(inputField);
			
			initInternalDataProvider();
		}
		
		override internal function initEventListeners():void {
			super.initEventListeners();
			
			_view.dropDownList.addEventListener(FocusEvent.FOCUS_IN, focusInHandler);
			_view.dropDownList.addEventListener(FocusEvent.FOCUS_OUT, focusOutHandler);
			_view.dropDownList.addEventListener(Event.CHANGE, changeHandler);
			_view.dropDownList.addEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);
			eventDispatcher.addEventListener(UIEvent.ACTIVE_RECORD_CHANGED, activeRecordChangeHandler);
			
			ChangeWatcher.watch(_view, "dataProvider", dataProviderChangeHandler);
		}
		
		override protected function changeHandler(event:Event):void {
			changed = true;
			updateValue();
		}
		
		protected function activeRecordChangeHandler(event:Event):void {
			initInternalDataProvider();
		}
		
		protected function dataProviderChangeHandler(event:PropertyChangeEvent):void {
			initInternalDataProvider();
		}
		
		protected function initInternalDataProvider():void {
			var temp:ArrayCollection = new ArrayCollection();
			if(_view.dataProvider != null) {
				temp.addAll(_view.dataProvider);
			}
			if(Application.activeRecord != null && Application.activeRecord.step == CollectRecord$Step.ENTRY) {
				temp.addItem(BLANK_ON_FORM_ITEM);
				temp.addItem(DASH_ON_FORM_ITEM);
				temp.addItem(ILLEGIBLE_ITEM);
			} else {
				temp.addItem(EMPTY_ITEM);
			}
			_view.internalDataProvider = temp;
		}
		
		override protected function textToRequestValue():String {
			var result:String = null;
			var selectedItem:* = _view.dropDownList.selectedItem;
			if(selectedItem != null) {
				switch(selectedItem) {
					case BLANK_ON_FORM_ITEM:
					case DASH_ON_FORM_ITEM:
					case ILLEGIBLE_ITEM:
						result = selectedItem.shortCut;
						break;
					default:
						var value:* = ObjectUtil.getValue(selectedItem, _view.dataField);
						if(value != null) {
							result = String(value);
						}
				}
			}
			return result;
		}
		
		override protected function updateView():void {
			var item:Object = null;
			var attribute:AttributeProxy = _view.attribute;
			if(attribute != null) {
				var field:FieldProxy = attribute.getField(_view.fieldIndex);
				var value:Object = field.value;
				if(field.symbol != null && isReasonBlankSymbol(field.symbol)) {
					switch(field.symbol) {
						case FieldSymbol.BLANK_ON_FORM:
							item = BLANK_ON_FORM_ITEM;
							break;
						case FieldSymbol.DASH_ON_FORM:
							item = DASH_ON_FORM_ITEM;
							break;1
						case FieldSymbol.ILLEGIBLE:
							item = ILLEGIBLE_ITEM;
							break;
					}
				} else if(value != null) {
					item = getItem(value);
				}
			}
			if(item == null && Application.activeRecord != null && Application.activeRecord.step != CollectRecord$Step.ENTRY) {
				if(_view.parentEntity != null && _view.parentEntity.isErrorOnChildVisible(_view.attributeDefinition.name)) {
					item = EMPTY_ITEM;
				}
			}
			_view.dropDownList.selectedItem = item;
		}
		
		override protected function keyDownHandler(event:KeyboardEvent):void {
			var item:Object = null;
			var char:String = String.fromCharCode(event.charCode);
			switch(char) {
				case SHORTCUT_BLANK_ON_FORM:
					item = BLANK_ON_FORM_ITEM;
					break;
				case SHORTCUT_DASH_ON_FORM:
					item = DASH_ON_FORM_ITEM;
					break;
				case SHORTCUT_ILLEGIBLE:
					item = ILLEGIBLE_ITEM;
					break;
			}
			if(item != null) {
				_view.dropDownList.selectedItem = item;
				updateValue();
			}
		}

		protected function getItem(value:*):Object {
			var dataProvider:IList = _view.dropDownList.dataProvider;
			var dataField:String = _view.dataField;
			var item:Object = CollectionUtil.getItem(dataProvider, dataField, value);
			return item;
		}
	}
}
