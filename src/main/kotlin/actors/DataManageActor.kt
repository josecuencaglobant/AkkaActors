package actors

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import msg.AddDataInstruction
import msg.instructions.DataInstruction
import msg.CheckDataInstruction
import msg.MessageResult
import msg.WithDrawData

class DataManageActor: AbstractBehavior<DataInstruction> {

    private var verificationActor: ActorRef<DataInstruction>

    constructor(context: ActorContext<DataInstruction>, verificationActor: ActorRef<DataInstruction>) : super(context) {
        this.verificationActor = verificationActor
    }

    override fun createReceive(): Receive<DataInstruction> {
        return newReceiveBuilder()
            .onMessage(CheckDataInstruction::class.java,this::checkData)
            .onMessage(AddDataInstruction::class.java,this::addData)
            .onMessage(WithDrawData::class.java,this::withDrawData)
            .onMessage(MessageResult::class.java, this::checkResult)
            .build()
    }

    private fun withDrawData(msg: WithDrawData): Behavior<DataInstruction>{
        msg.senderActor = context.self
        verificationActor.tell(msg)
        return this
    }

    private fun addData(msg: AddDataInstruction): Behavior<DataInstruction>{
        msg.senderActor = context.self
        verificationActor.tell(msg)
        return this
    }

    private fun checkData(msg: CheckDataInstruction): Behavior<DataInstruction> {
        msg.senderActor = context.self
        verificationActor.tell( msg )
        return this
    }

    private fun checkResult(msg: MessageResult): Behavior<DataInstruction>{
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