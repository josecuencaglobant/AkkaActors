package msg

import msg.instructions.DataInstruction

class WithDrawData(id: String, value: Double): DataInstruction() {

    var value = value
    init {
        this.usrId = id
        this.operationType = WITHDRAW_DATA
    }

}