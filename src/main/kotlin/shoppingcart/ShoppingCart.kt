package shoppingcart

import akka.actor.typed.Behavior
import akka.persistence.testkit.query.javadsl.PersistenceTestKitReadJournal
import akka.persistence.typed.ReplicaId
import akka.persistence.typed.ReplicationId
import akka.persistence.typed.crdt.Counter
import akka.persistence.typed.javadsl.*
import akka.remote.artery.aeron.TaskRunner
import shoppingcart.data.AddItem
import shoppingcart.data.ItemUpdated
import shoppingcart.data.RemoveItem
import shoppingcart.model.ShoppingCartCommand
import shoppingcart.model.ShoppingCartEvent
import shoppingcart.model.ShoppingCartState

class ShoppingCart: ReplicatedEventSourcedBehavior<ShoppingCartCommand,ShoppingCartEvent,ShoppingCartState> {

    private constructor(replicationContext: ReplicationContext):super(replicationContext)


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

    override fun emptyState(): ShoppingCartState {
        return ShoppingCartState()
    }

    override fun commandHandler(): CommandHandler<ShoppingCartCommand, ShoppingCartEvent, ShoppingCartState> {
        TODO("Not yet implemented")
    }

    override fun eventHandler(): EventHandler<ShoppingCartState, ShoppingCartEvent> {
        TODO("Not yet implemented")
    }

    private fun onAddItem(state: ShoppingCartState, command: AddItem): Effect<ShoppingCartEvent, ShoppingCartState>{
        return Effect()
            .persist(ItemUpdated(command.productId,Counter.Updated(command.count)))
    }

    private fun onRemoveItem(state: ShoppingCartState,command: RemoveItem):Effect<ShoppingCartEvent,ShoppingCartState>{
        return Effect()
            .persist(ItemUpdated(command.productId,Counter.Updated(-command.count)))
    }

}