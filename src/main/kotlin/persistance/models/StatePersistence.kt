package persistance.models

import kotlin.math.min

class StatePersistence() {

    var items: ArrayList<String> = arrayListOf()

    private constructor(items: ArrayList<String>):this(){
        this.items = items
    }

    fun addItem(data: String):StatePersistence{
        var newItems = ArrayList<String>(items)
        newItems.add(data)
        // Keep 5
        var latest = ArrayList<String>( newItems.subList(0, min(5,newItems.size)) )
        return StatePersistence(latest)
    }

}