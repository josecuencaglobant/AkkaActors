package actors

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import msg.instructions.DataInstruction
import msg.CheckDataInstruction
import msg.CheckDataResult

class DataManageActor: AbstractBehavior<DataInstruction> {

    private var verificationActor: ActorRef<DataInstruction>

    constructor(context: ActorContext<DataInstruction>, verificationActor: ActorRef<DataInstruction>) : super(context) {
        this.verificationActor = verificationActor
    }

    override fun createReceive(): Receive<DataInstruction> {
        return newReceiveBuilder()
            .onMessage(CheckDataInstruction::class.java,this::checkData)
            .onMessage(CheckDataResult::class.java, this::checkResult)
            .build()
    }

    private fun checkData(msg: CheckDataInstruction): Behavior<DataInstruction> {
        msg.senderActor = context.self
        verificationActor.tell( msg )
        return this
    }

    private fun checkResult(msg: CheckDataResult): Behavior<DataInstruction>{
        println(msg.message)
        return this
    }

    companion object{
        fun behavior(verification: ActorRef<DataInstruction>): Behavior<DataInstruction>{
            return Behaviors.setup {
                c -> DataManageActor(c,verification)
            }
        }
    }

}