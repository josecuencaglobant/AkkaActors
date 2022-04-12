package actors

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import db.FakeDB
import msg.CheckDataInstruction
import msg.CheckDataResult
import msg.instructions.DataInstruction

class VerificationActor: AbstractBehavior<DataInstruction> {

    constructor(context: ActorContext<DataInstruction>): super(context)

    override fun createReceive(): Receive<DataInstruction> {
        return newReceiveBuilder()
            .onMessage(CheckDataInstruction::class.java,this::checkData)
            .build()

    }

    private fun checkData(msg: CheckDataInstruction): Behavior<DataInstruction> {
        msg.senderActor.tell(CheckDataResult( FakeDB.getData( msg.usrId ) ))
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