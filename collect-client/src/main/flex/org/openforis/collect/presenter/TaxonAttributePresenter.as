package org.openforis.collect.presenter {
	import flash.display.DisplayObject;
	import flash.events.FocusEvent;
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	
	import mx.collections.IList;
	import mx.controls.TextInput;
	import mx.core.FlexGlobals;
	import mx.events.FlexMouseEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.AsyncResponder;
	import mx.rpc.IResponder;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.utils.StringUtil;
	
	import org.openforis.collect.client.ClientFactory;
	import org.openforis.collect.client.SpeciesClient;
	import org.openforis.collect.event.InputFieldEvent;
	import org.openforis.collect.event.TaxonInputFieldEvent;
	import org.openforis.collect.i18n.Message;
	import org.openforis.collect.manager.TaxonSearchParameters;
	import org.openforis.collect.metamodel.proxy.TaxonAttributeDefinitionProxy;
	import org.openforis.collect.model.proxy.FieldProxy;
	import org.openforis.collect.model.proxy.NodeUpdateRequestSetProxy;
	import org.openforis.collect.ui.component.input.InputField;
	import org.openforis.collect.ui.component.input.TaxonAttributeRenderer;
	import org.openforis.collect.ui.component.input.TaxonAutoCompletePopUp;
	import org.openforis.collect.util.CollectionUtil;
	import org.openforis.collect.util.PopUpUtil;
	import org.openforis.collect.util.StringUtil;
	import org.openforis.collect.util.UIUtil;
	
	/**
	 * 
	 * @author S. Ricci
	 * */
	public class TaxonAttributePresenter extends CompositeAttributePresenter {
		
		private static const MAX_RESULTS:int = 400;
		private static const MIN_CHARS_TO_START_AUTOCOMPLETE:int = 2;
		private static const SEARCH_BY_FAMILY_SCIENTIFIC_NAME:String = "byFamilyScientificName";
		private static const SEARCH_BY_CODE:String = "byCode";
		private static const SEARCH_BY_SCIENTIFIC_NAME:String = "byScientificName";
		private static const SEARCH_BY_VERNACULAR_NAME:String = "byVernacularName";
		
		private static const UNKNOWN_ITEM:Object = {code: "UNK", scientificName: Message.get("edit.taxon.unknown")};
		private static const UNLISTED_ITEM:Object = {code: "UNL", scientificName: Message.get("edit.taxon.unlisted")};
		
		protected static var autoCompletePopUp:TaxonAutoCompletePopUp;
		protected static var autoCompletePopUpOpened:Boolean = false;
		protected static var autoCompleteDataLoading:Boolean = false;
		protected static var autoCompleteLastSearchType:String;
		protected static var autoCompleteLastInputField:InputField;
		protected static var autoCompleteLastResult:IList;
		
		private var _lastSelectedTaxon:Object;
		private var _lastSearchType:String;
		
		public function TaxonAttributePresenter(view:TaxonAttributeRenderer) {
			super(view);
		}
		
		override protected function initEventListeners():void {
			super.initEventListeners();
			
			//family scientific name text input
			view.familyScientificNameTextInput.applyChangesOnFocusOut = false;
			view.familyScientificNameTextInput.addEventListener(InputFieldEvent.CHANGING, inputFieldChangingHandler);
			view.familyScientificNameTextInput.addEventListener(FocusEvent.FOCUS_OUT, inputFieldFocusOutHandler);
			view.familyScientificNameTextInput.textInput.addEventListener(KeyboardEvent.KEY_DOWN, inputFieldKeyDownHandler, false, 100);
			//id text input
			view.codeTextInput.applyChangesOnFocusOut = false;
			view.codeTextInput.addEventListener(InputFieldEvent.CHANGING, inputFieldChangingHandler);
			view.codeTextInput.addEventListener(FocusEvent.FOCUS_OUT, inputFieldFocusOutHandler);
			view.codeTextInput.textInput.addEventListener(KeyboardEvent.KEY_DOWN, inputFieldKeyDownHandler, false, 100);
			//scientific name text input
			view.scientificNameTextInput.applyChangesOnFocusOut = false;
			view.scientificNameTextInput.addEventListener(InputFieldEvent.CHANGING, inputFieldChangingHandler);
			view.scientificNameTextInput.addEventListener(FocusEvent.FOCUS_OUT, inputFieldFocusOutHandler);
			view.scientificNameTextInput.textInput.addEventListener(KeyboardEvent.KEY_DOWN, inputFieldKeyDownHandler, false, 100);
			//vernacular name text input
			view.vernacularNameTextInput.applyChangesOnFocusOut = false;
			view.vernacularNameTextInput.addEventListener(InputFieldEvent.CHANGING, inputFieldChangingHandler);
			view.vernacularNameTextInput.addEventListener(FocusEvent.FOCUS_OUT, inputFieldFocusOutHandler);
			view.vernacularNameTextInput.textInput.addEventListener(KeyboardEvent.KEY_DOWN, inputFieldKeyDownHandler, false, 100);
			//language code text input
			//language variety text input
			view.languageVarietyTextInput.addEventListener(InputFieldEvent.CHANGING, inputFieldChangingHandler);
		}
		
		protected function inputFieldFocusOutHandler(event:FocusEvent):void {
			var inputField:InputField = event.target.document;
			if ( inputField != null && inputField.changed && ! autoCompletePopUpOpened && ! UIUtil.isFocussed(autoCompletePopUp) ) {
				inputField.presenter.updateValue();
				var defn:TaxonAttributeDefinitionProxy = TaxonAttributeDefinitionProxy(view.attributeDefinition);
				
				if ( defn.codeVisible && inputField != view.codeTextInput && ! inputField.isEmpty() && 
						(view.codeTextInput.isEmpty() || FieldProxy.isShortCutForReasonBlank(view.codeTextInput.text)) ) {
					view.codeTextInput.text = UNLISTED_ITEM.code;
					view.codeTextInput.presenter.updateValue();
				}
			}
		}
		
		private function get view():TaxonAttributeRenderer {
			return TaxonAttributeRenderer(_view);
		}
		
		protected function inputFieldChangingHandler(event:InputFieldEvent):void {
			var inputField:InputField = event.target as InputField;
			if(inputField == null) {
				return; //error
			}
			var text:String = mx.utils.StringUtil.trim(inputField.text);
			if(text.length < MIN_CHARS_TO_START_AUTOCOMPLETE) {
				return;
			}
			switch(inputField) {
				case view.familyScientificNameTextInput:
					_lastSearchType = SEARCH_BY_FAMILY_SCIENTIFIC_NAME;
					break;
				case view.codeTextInput:
					_lastSearchType = SEARCH_BY_CODE;
					break;
				case view.scientificNameTextInput:
					_lastSearchType = SEARCH_BY_SCIENTIFIC_NAME;
					break;
				case view.vernacularNameTextInput:
					_lastSearchType = SEARCH_BY_VERNACULAR_NAME;
					break;
				default:
					_lastSearchType = null;
			}
			if(_lastSearchType != null) {
				var defn:TaxonAttributeDefinitionProxy = TaxonAttributeDefinitionProxy(view.attributeDefinition)
				var alignField:InputField = 
					defn.showFamily ? view.familyScientificNameTextInput:	
					defn.codeVisible ? view.codeTextInput:
					defn.scientificNameVisible ? view.scientificNameTextInput:
					defn.vernacularNameVisible ? view.vernacularNameTextInput:
					null;
				showAutoCompletePopUp(_lastSearchType, inputField, alignField);
			}
		}
		
		protected function inputFieldKeyDownHandler(event:KeyboardEvent):void {
			switch ( event.keyCode ) {
			case Keyboard.DOWN:
				if ( autoCompletePopUpOpened ) {
					preventDefaultHandlerAndPropagation(event);

					autoCompletePopUp.dataGrid.setFocus();
					if ( CollectionUtil.isNotEmpty(autoCompleteLastResult) ) {
						autoCompletePopUp.dataGrid.selectedIndex = 0;
					}
				}
				break;
			case Keyboard.ESCAPE:
				closeAutoCompletePopUp();
				break;
			case Keyboard.TAB:
				if ( autoCompleteDataLoading ) {
					preventDefaultHandlerAndPropagation(event);
				} else if ( autoCompletePopUpOpened ) {
					preventDefaultHandlerAndPropagation(event);
					var matchingResult:Object = getMatchingResult();
					if ( matchingResult == null ) {
						//no matching result, cancel field editing
						cancelAutoComplete();
					} else {
						//select matching result, if any
						performSelectTaxon(matchingResult);
					}
					closeAutoCompletePopUp();
				}
				break;
			}
		}
		
		protected static function getMatchingResult():Object {
			var searchText:String = autoCompleteLastInputField.text;
			for each (var item:Object in autoCompleteLastResult) {
				var compareToValue:String;
				switch ( autoCompleteLastSearchType ) {
					case SEARCH_BY_CODE:
						compareToValue = item.code;
						break;
					case SEARCH_BY_SCIENTIFIC_NAME:
						compareToValue = item.scientificName;
						break;
					case SEARCH_BY_VERNACULAR_NAME:
						compareToValue = item.vernacularName;
						break;
				}
				if ( compareToValue != null && searchText.toUpperCase() == compareToValue.toUpperCase() ) {
					return item;
				}
			}
			return null;
		}
		
		protected static function showAutoCompletePopUp(searchType:String, inputField:InputField, alignField:DisplayObject):void {
			if(autoCompletePopUp == null) {
				autoCompletePopUp = new TaxonAutoCompletePopUp();
				autoCompletePopUp.addEventListener(KeyboardEvent.KEY_DOWN, autoCompleteKeyDownHandler);
				autoCompletePopUp.addEventListener(FlexMouseEvent.MOUSE_DOWN_OUTSIDE, autoCompleteMouseDownOutsideHandler);
				autoCompletePopUp.addEventListener(TaxonInputFieldEvent.TAXON_SELECT, taxonSelectHandler);
			}
			autoCompletePopUp.taxonAttributeDefinition = TaxonAttributeDefinitionProxy(inputField.attributeDefinition);
			
			if(! autoCompletePopUpOpened) {
				PopUpManager.addPopUp(autoCompletePopUp, FlexGlobals.topLevelApplication as DisplayObject, false);
				
				PopUpUtil.alignToField(autoCompletePopUp, alignField, 
					PopUpUtil.POSITION_BELOW, 
					PopUpUtil.VERTICAL_ALIGN_BOTTOM, 
					PopUpUtil.HORIZONTAL_ALIGN_LEFT,
					false
				);
				
				autoCompletePopUpOpened = true;
			}
			autoCompleteLastInputField = inputField;
			autoCompleteLastSearchType = searchType;
			loadAutoCompleteData();
		}
		
		protected static function loadAutoCompleteData():void {
			autoCompleteDataLoading = true;
			autoCompletePopUp.dataGrid.dataProvider = null;
			var client:SpeciesClient = ClientFactory.speciesClient;
			var searchText:String = autoCompleteLastInputField.text;
			var attrDef:TaxonAttributeDefinitionProxy = TaxonAttributeDefinitionProxy(autoCompleteLastInputField.attributeDefinition); 
			var taxonomy:String = attrDef.taxonomy;
			var token:Object = {searchText: searchText, searchType: autoCompleteLastSearchType};
			var responder:IResponder = new AsyncResponder(autoCompleteSearchResultHandler, searchFaultHandler, token);
			var parameters:TaxonSearchParameters = new TaxonSearchParameters();
			parameters.includeUniqueVernacularName = attrDef.includeUniqueVernacularName;
			parameters.includeAncestorTaxons = attrDef.showFamily;

			switch ( autoCompleteLastSearchType ) {
				case SEARCH_BY_FAMILY_SCIENTIFIC_NAME:
					client.findByFamilyScientificName(responder, taxonomy, searchText, MAX_RESULTS, parameters);
					break;
				case SEARCH_BY_CODE:
					client.findByCode(responder, taxonomy, searchText, MAX_RESULTS, parameters);
					break;
				case SEARCH_BY_SCIENTIFIC_NAME:
					client.findByScientificName(responder, taxonomy, searchText, MAX_RESULTS, parameters);
					break;
				case SEARCH_BY_VERNACULAR_NAME:
					var nodeId:int = autoCompleteLastInputField.attribute.id;
					client.findByVernacularName(responder, taxonomy, nodeId, searchText, MAX_RESULTS, parameters);
					break;
				default:
			}
		}
		
		protected static function autoCompleteKeyDownHandler(event:KeyboardEvent):void {
			var keyCode:uint = event.keyCode;
			switch(keyCode) {
				case Keyboard.ENTER:
					var taxon:Object = autoCompletePopUp.dataGrid.selectedItem;
					var renderer:TaxonAttributeRenderer = autoCompleteLastInputField.parentDocument as TaxonAttributeRenderer;
					var presenter:TaxonAttributePresenter = renderer.presenter as TaxonAttributePresenter;
					presenter.performSelectTaxon(taxon);
					closeAutoCompletePopUp();
					break;
				case Keyboard.ESCAPE:
					cancelAutoComplete();
					break;
			}
		}
		
		protected static function autoCompleteMouseDownOutsideHandler(event:FlexMouseEvent):void {
			cancelAutoComplete();
		}
		
		protected static function closeAutoCompletePopUp():void {
			if ( autoCompletePopUpOpened ) {
				PopUpManager.removePopUp(autoCompletePopUp);
				autoCompletePopUpOpened = false;
				var textInput:TextInput = autoCompleteLastInputField.textInput as TextInput;
				textInput.setFocus();
			}
		}
		
		protected static function cancelAutoComplete():void {
			if ( autoCompleteLastInputField.changed ) {
				autoCompleteLastInputField.presenter.undoLastChange();
			}
			closeAutoCompletePopUp();
		}
		
		protected static function taxonSelectHandler(event:TaxonInputFieldEvent = null):void {
			var taxon:Object = event.taxon;
			if(taxon != null) {
				var renderer:TaxonAttributeRenderer = autoCompleteLastInputField.parentDocument as TaxonAttributeRenderer;
				var presenter:TaxonAttributePresenter = renderer.presenter as TaxonAttributePresenter;
				presenter.performSelectTaxon(taxon);
			}
			closeAutoCompletePopUp();
		}
		
		public function performSelectTaxon(taxonOccurrence:Object):void {
			_lastSelectedTaxon = taxonOccurrence;
			var defn:TaxonAttributeDefinitionProxy = TaxonAttributeDefinitionProxy(view.attributeDefinition);
			
			if ( defn.showFamily ) {
				//update family scientific name
				view.familyScientificNameTextInput.text = taxonOccurrence.familyScientificName;
			}
			//always update code
			view.codeTextInput.text = taxonOccurrence.code;
			
			//when searching by code or unlisted/unknown item has been selected, replace fields text
			if ( _lastSearchType == SEARCH_BY_CODE || 
					( taxonOccurrence != UNKNOWN_ITEM && taxonOccurrence != UNLISTED_ITEM ) ) {
				if ( defn.scientificNameVisible ) {
					//update scientific name
					view.scientificNameTextInput.text = taxonOccurrence.scientificName;
				}
				if ( defn.vernacularNameVisible ) {
					//update vernacular name
					view.vernacularNameTextInput.text = taxonOccurrence.vernacularName;
				}
				if ( defn.languageCodeVisible ) {
					//update language code
					view.languageAutocomplete.text = taxonOccurrence.language;
				}
				if ( defn.languageVarietyVisible ) {
					//update language variety
					view.languageVarietyTextInput.text = taxonOccurrence.languageVariety;
				}
			}
			//prepare request set
			var reqSet:NodeUpdateRequestSetProxy = new NodeUpdateRequestSetProxy();
			
			reqSet.addRequest(view.familyScientificNameTextInput.presenter.createValueUpdateRequest());
			reqSet.addRequest(view.codeTextInput.presenter.createValueUpdateRequest());
			reqSet.addRequest(view.scientificNameTextInput.presenter.createValueUpdateRequest());
			reqSet.addRequest(view.vernacularNameTextInput.presenter.createValueUpdateRequest());
			reqSet.addRequest(view.languageAutocomplete.presenter.createValueUpdateRequest());
			reqSet.addRequest(view.languageVarietyTextInput.presenter.createValueUpdateRequest());
			
			ClientFactory.dataClient.updateActiveRecord(reqSet, null, faultHandler);
		}
		
		protected static function autoCompleteSearchResultHandler(event:ResultEvent, token:Object):void {
			var data:IList = event.result as IList;
			if ( CollectionUtil.isEmpty(data) ) {
				var searchType:String = token.searchType;
				var searchText:String = token.searchText;
				if ( (searchType == SEARCH_BY_SCIENTIFIC_NAME || searchType == SEARCH_BY_VERNACULAR_NAME) &&
					org.openforis.collect.util.StringUtil.startsWith(UNKNOWN_ITEM.scientificName, searchText, true) ||
					searchType == SEARCH_BY_CODE && org.openforis.collect.util.StringUtil.startsWith(UNKNOWN_ITEM.code, searchText, true)
					) {
					data.addItem(UNKNOWN_ITEM);
				}
			}
			data.addItem(UNLISTED_ITEM);
			
			autoCompletePopUp.dataGrid.dataProvider = data;
			autoCompleteLastResult = data;
			autoCompleteDataLoading = false;
		}
		
		protected static function searchFaultHandler(event:FaultEvent, token:Object = null):void {
			autoCompleteDataLoading = false;
			faultHandler(event, token);
		}
	}
}
