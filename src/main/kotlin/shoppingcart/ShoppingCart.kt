package shoppingcart

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.javadsl.CommandHandler
import akka.persistence.typed.javadsl.Effect
import akka.persistence.typed.javadsl.EventHandler
import akka.persistence.typed.javadsl.EventSourcedBehavior
import shoppingcart.data.*
import shoppingcart.model.CartCommand
import shoppingcart.model.CartEvent

class ShoppingCart: EventSourcedBehavior<CartCommand,CartEvent,CartState> {

    var subscriber: ActorRef<CartState>

    private constructor(persistenceId: PersistenceId,subscriber: ActorRef<CartState>): super(persistenceId){
        this.subscriber = subscriber
    }

    companion object{
        fun create(persistenceId: PersistenceId,subscriber: ActorRef<CartState>):Behavior<CartCommand>{
            return ShoppingCart(persistenceId,subscriber)
        }
    }

    override fun emptyState(): CartState {
        return CartState()
    }

    override fun commandHandler(): CommandHandler<CartCommand, CartEvent, CartState> {
        return newCommandHandlerBuilder()
            .forAnyState()
            .onCommand(AddProduct::class.java,this::onAdd)
            .onCommand(DeleteProduct::class.java,this::onDeleted)
            .build()
    }

    private fun onAdd(command: AddProduct):Effect<CartEvent,CartState>{
        return Effect()
            .persist(ProductAdded(command.product))
            //.thenRun { state: CartState -> subscriber.tell(state) }
    }

    private fun onDeleted(command: DeleteProduct): Effect<CartEvent,CartState>{
        return Effect()
            .persist(ProductDeleted(command.product))
            //.thenRun { state: CartState -> subscriber.tell(state) }
    }

    override fun eventHandler(): EventHandler<CartState, CartEvent> {
        return newEventHandlerBuilder()
            .forAnyState()
            .onEvent(ProductAdded::class.java)
            {state,event -> tellSubscriber(state.addOperation(event.product,event.operation))}
            .onEvent(ProductDeleted::class.java)
            {state,event -> tellSubscriber(state.addOperation(event.product,event.operation))}
            .build()
    }

    fun tellSubscriber(cartState: CartState): CartState{
        subscriber.tell(cartState)
        return cartState
    }

}