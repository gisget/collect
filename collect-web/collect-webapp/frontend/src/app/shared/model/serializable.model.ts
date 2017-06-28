export class Serializable {
    fillFromJSON(jsonObj) {
        for (var propName in jsonObj) {
            this[propName] = jsonObj[propName]
        }
    }
}