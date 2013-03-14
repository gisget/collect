/**
 * Generated by Gas3 v2.3.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR.
 */

package org.openforis.collect.manager.referenceDataImport {

    import org.granite.util.Enum;

    [Bindable]
    [RemoteClass(alias="org.openforis.collect.manager.referenceDataImport.ParsingError$ErrorType")]
    public class ParsingError$ErrorType extends Enum {

        public static const UNEXPECTED_COLUMNS:ParsingError$ErrorType = new ParsingError$ErrorType("UNEXPECTED_COLUMNS", _);
        public static const WRONG_COLUMN_NAME:ParsingError$ErrorType = new ParsingError$ErrorType("WRONG_COLUMN_NAME", _);
        public static const EMPTY:ParsingError$ErrorType = new ParsingError$ErrorType("EMPTY", _);
        public static const INVALID_VALUE:ParsingError$ErrorType = new ParsingError$ErrorType("INVALID_VALUE", _);
        public static const DUPLICATE_VALUE:ParsingError$ErrorType = new ParsingError$ErrorType("DUPLICATE_VALUE", _);
        public static const UNEXPECTED_SYNONYM:ParsingError$ErrorType = new ParsingError$ErrorType("UNEXPECTED_SYNONYM", _);
        public static const IOERROR:ParsingError$ErrorType = new ParsingError$ErrorType("IOERROR", _);

        function ParsingError$ErrorType(value:String = null, restrictor:* = null) {
            super((value || UNEXPECTED_COLUMNS.name), restrictor);
        }

        protected override function getConstants():Array {
            return constants;
        }

        public static function get constants():Array {
            return [UNEXPECTED_COLUMNS, WRONG_COLUMN_NAME, EMPTY, INVALID_VALUE, DUPLICATE_VALUE, UNEXPECTED_SYNONYM, IOERROR];
        }

        public static function valueOf(name:String):ParsingError$ErrorType {
            return ParsingError$ErrorType(UNEXPECTED_COLUMNS.constantOf(name));
        }
    }
}