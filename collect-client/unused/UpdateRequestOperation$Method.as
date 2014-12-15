/**
 * Generated by Gas3 v2.3.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR.
 */

package org.openforis.collect.remoting.service {

    import org.granite.util.Enum;

    [Bindable]
    [RemoteClass(alias="org.openforis.collect.remoting.service.UpdateRequestOperation$Method")]
    public class UpdateRequestOperation$Method extends Enum {

        public static const ADD:UpdateRequestOperation$Method = new UpdateRequestOperation$Method("ADD", _);
        public static const UPDATE:UpdateRequestOperation$Method = new UpdateRequestOperation$Method("UPDATE", _);
        public static const DELETE:UpdateRequestOperation$Method = new UpdateRequestOperation$Method("DELETE", _);
        public static const CONFIRM_ERROR:UpdateRequestOperation$Method = new UpdateRequestOperation$Method("CONFIRM_ERROR", _);
        public static const APPROVE_MISSING:UpdateRequestOperation$Method = new UpdateRequestOperation$Method("APPROVE_MISSING", _);
        public static const UPDATE_REMARKS:UpdateRequestOperation$Method = new UpdateRequestOperation$Method("UPDATE_REMARKS", _);
        public static const APPLY_DEFAULT_VALUE:UpdateRequestOperation$Method = new UpdateRequestOperation$Method("APPLY_DEFAULT_VALUE", _);

        function UpdateRequestOperation$Method(value:String = null, restrictor:* = null) {
            super((value || ADD.name), restrictor);
        }

        protected override function getConstants():Array {
            return constants;
        }

        public static function get constants():Array {
            return [ADD, UPDATE, DELETE, CONFIRM_ERROR, APPROVE_MISSING, UPDATE_REMARKS, APPLY_DEFAULT_VALUE];
        }

        public static function valueOf(name:String):UpdateRequestOperation$Method {
            return UpdateRequestOperation$Method(ADD.constantOf(name));
        }
    }
}