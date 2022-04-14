package shoppingcart.actors

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import shoppingcart.data.CartState

class ManageResultActor: AbstractBehavior<CartState> {

    private constructor(context: ActorContext<CartState>): super(context)

    companion object{
        fun behavior(): Behavior<CartState>{
            return Behaviors.setup {
                ctx -> ManageResultActor(ctx)
            }
        }
    }

    override fun createReceive(): Receive<CartState> {
        return newReceiveBuilder()
            .onMessage(CartState::class.java,this::printMessage)
            .build()
    }

    private fun printMessage(msg: CartState): Behavior<CartState>{
        println(msg)
        return this
    }

}