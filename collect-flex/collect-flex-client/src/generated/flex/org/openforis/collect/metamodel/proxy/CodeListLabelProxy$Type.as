/**
 * Generated by Gas3 v2.2.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR.
 */

package org.openforis.collect.metamodel.proxy {

    import org.granite.util.Enum;

    [Bindable]
    [RemoteClass(alias="org.openforis.collect.metamodel.proxy.CodeListLabelProxy$Type")]
    public class CodeListLabelProxy$Type extends Enum {

        public static const ITEM:CodeListLabelProxy$Type = new CodeListLabelProxy$Type("ITEM", _);
        public static const LIST:CodeListLabelProxy$Type = new CodeListLabelProxy$Type("LIST", _);

        function CodeListLabelProxy$Type(value:String = null, restrictor:* = null) {
            super((value || ITEM.name), restrictor);
        }

        override protected function getConstants():Array {
            return constants;
        }

        public static function get constants():Array {
            return [ITEM, LIST];
        }

        public static function valueOf(name:String):CodeListLabelProxy$Type {
            return CodeListLabelProxy$Type(ITEM.constantOf(name));
        }
    }
}