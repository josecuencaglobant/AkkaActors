package persistance

import akka.actor.typed.Behavior
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.javadsl.CommandHandler
import akka.persistence.typed.javadsl.EventHandler
import akka.persistence.typed.javadsl.EventSourcedBehavior
import persistance.data.Add
import persistance.data.Added
import persistance.models.CommandPersistence
import persistance.models.EventPersistence
import persistance.models.StatePersistence

class MyPersistenceBehavior: EventSourcedBehavior<CommandPersistence, EventPersistence, StatePersistence> {

    private constructor(persistenceId: PersistenceId): super(persistenceId)

    override fun emptyState(): StatePersistence {
        return StatePersistence()
    }

    override fun commandHandler(): CommandHandler<CommandPersistence, EventPersistence, StatePersistence> {
       return newCommandHandlerBuilder()
           .forAnyState()
           .onCommand(Add::class.java, {command -> Effect().persist(Added(command.data))})
           .build()
    }

    override fun eventHandler(): EventHandler<StatePersistence, EventPersistence> {
        return newEventHandlerBuilder()
            .forAnyState()
            .onEvent(Added::class.java,{state,event -> state.addItem(event.data)})
            .build()
    }

    companion object{
        fun create(persistenceId: PersistenceId): Behavior<CommandPersistence>{
            return MyPersistenceBehavior(persistenceId)
        }
    }

}