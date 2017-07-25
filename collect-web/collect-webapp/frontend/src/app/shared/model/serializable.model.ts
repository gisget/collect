export class Serializable {
    fillFromJSON(jsonObj) {
        for (var propName in jsonObj) {
            this[propName] = jsonObj[propName]
        }
    }
    
    public static createArrayFromJSON(jsonArr, itemClassName: any): Array<any> {
        let result: Array<any> = [];
        for (var i = 0; i < jsonArr.length; i++) {
            var itemJsonObj = jsonArr[i];
            var item: any = new itemClassName();
            item.fillFromJSON(itemJsonObj);
            result.push(item);
        }
        return result;
    }
}