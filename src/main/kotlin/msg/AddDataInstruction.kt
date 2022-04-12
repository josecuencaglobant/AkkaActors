package msg

import msg.instructions.DataInstruction

class AddDataInstruction(id:String, value: Double): DataInstruction() {

    var value = value
    init {
        this.usrId = id
        this.operationType = ADD_VALUE
    }

}