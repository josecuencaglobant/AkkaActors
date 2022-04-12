package msg

import akka.actor.typed.ActorRef
import msg.instructions.DataInstruction

class CheckDataInstruction(usrId: String): DataInstruction() {

    init {
        this.usrId = usrId
        this.operationType = CHECK_BALANCE
    }

}