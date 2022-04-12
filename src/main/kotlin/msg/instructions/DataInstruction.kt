package msg.instructions

import akka.actor.typed.ActorRef
import db.FakeDB

open class DataInstruction{

    lateinit var usrId: String
    lateinit var operationType: String
    lateinit var senderActor: ActorRef<DataInstruction>

    fun consistentHashKey(): String{
        if(usrId == FakeDB.USER1) return "1"
        if(usrId == FakeDB.USER2) return "2"
        return "3"
    }

    companion object{
        const val CHECK_BALANCE = "Check Balance"
        const val ADD_VALUE = "Add Value"
        const val WITHDRAW_DATA = "Withdraw data"
    }

}