package shoppingcart

import akka.actor.typed.Behavior
import akka.persistence.testkit.query.javadsl.PersistenceTestKitReadJournal
import akka.persistence.typed.ReplicaId
import akka.persistence.typed.ReplicationId
import akka.persistence.typed.crdt.Counter
import akka.persistence.typed.javadsl.*
import akka.remote.artery.aeron.TaskRunner
import shoppingcart.data.*
import shoppingcart.model.ShoppingCartCommand
import shoppingcart.model.ShoppingCartEvent
import shoppingcart.model.ShoppingCartState
import java.util.*
import kotlin.collections.HashMap

class ShoppingCart: ReplicatedEventSourcedBehavior<ShoppingCartCommand,ShoppingCartEvent,ShoppingCartState> {

    private constructor(replicationContext: ReplicationContext):super(replicationContext)

/*
    companion object{
        fun create(
            entityId: String, replicaId: ReplicaId, allReplicas: Set<ReplicaId>
        ): Behavior<ShoppingCartCommand>{
            return ReplicatedEventSourcing.commonJournalConfig(
                ReplicationId("blog",entityId,replicaId),
                allReplicas,
                PersistenceTestKitReadJournal.Identifier(),
                ShoppingCart::new
            )
        }

    }


 */
    override fun emptyState(): ShoppingCartState {
        return ShoppingCartState()
    }

    override fun commandHandler(): CommandHandler<ShoppingCartCommand, ShoppingCartEvent, ShoppingCartState> {
        return newCommandHandlerBuilder()
            .forAnyState()
            .onCommand(AddItem::class.java,this::onAddItem)
            .onCommand(RemoveItem::class.java,this::onRemoveItem)
            .onCommand(GetCartItems::class.java,this::onGetCartItems)
            .build()
    }

    override fun eventHandler(): EventHandler<ShoppingCartState, ShoppingCartEvent> {
        return newEventHandlerBuilder()
            .forAnyState()
            .onEvent(ItemUpdated::class.java,this::onItemUpdate)
            .build()
    }

    private fun onAddItem(state: ShoppingCartState, command: AddItem): Effect<ShoppingCartEvent, ShoppingCartState>{
        return Effect()
            .persist(ItemUpdated(command.productId,Counter.Updated(command.count)))
    }

    private fun onRemoveItem(state: ShoppingCartState,command: RemoveItem):Effect<ShoppingCartEvent,ShoppingCartState>{
        return Effect()
            .persist(ItemUpdated(command.productId,Counter.Updated(-command.count)))
    }

    private fun onGetCartItems(state: ShoppingCartState,command: GetCartItems): Effect<ShoppingCartEvent,ShoppingCartState>{
        command.replyTo.tell(CartItems(filterEmptyAndNegative(state.items)))
        return Effect().none()
    }

    private fun filterEmptyAndNegative(cart: Map<String, Int>): Map<String,Int>{
        var result = HashMap<String,Int>()
        for(entry in cart.entries){
            val count = entry.value
            if(count>0){
                result[entry.key] = count
            }
        }
        return Collections.unmodifiableMap(result)
    }

    private fun onItemUpdate(state: ShoppingCartState, event: ItemUpdated): ShoppingCartState{
        var counterForProduct: Counter
        if(state.items.containsKey(event.productId)){
            counterForProduct = (state.items[event.productId]) as Counter
        }else{
            counterForProduct = Counter.empty()
        }
        state.items[event.productId] = counterForProduct.applyOperation(event.updated) as Int
        return state
    }



}