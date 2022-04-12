package actors

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import db.FakeDB
import msg.AddDataInstruction
import msg.CheckDataInstruction
import msg.MessageResult
import msg.WithDrawData
import msg.instructions.DataInstruction
import javax.xml.crypto.Data

class VerificationActor: AbstractBehavior<DataInstruction> {

    constructor(context: ActorContext<DataInstruction>): super(context)

    override fun createReceive(): Receive<DataInstruction> {
        return newReceiveBuilder()
            .onMessage(CheckDataInstruction::class.java,this::checkData)
            .onMessage(AddDataInstruction::class.java,this::addData)
            .onMessage(WithDrawData::class.java,this::withDrawData)
            .build()

    }

    private fun withDrawData(msg: WithDrawData): Behavior<DataInstruction>{
        msg.senderActor.tell(MessageResult(
            FakeDB.withDrawData(msg.usrId,msg.value)
        ))
        return this
    }

    private fun addData(msg: AddDataInstruction): Behavior<DataInstruction>{
        msg.senderActor.tell( MessageResult(
            FakeDB.addData( msg.usrId,msg.value )
        )   )
        return this
    }

    private fun checkData(msg: CheckDataInstruction): Behavior<DataInstruction> {
        msg.senderActor.tell(MessageResult( FakeDB.getData( msg.usrId ) ))
        return this
    }

    companion object{
        fun behavior(): Behavior<DataInstruction>{
            return Behaviors.setup {
                    c -> VerificationActor(c)
            }
        }
    }

}