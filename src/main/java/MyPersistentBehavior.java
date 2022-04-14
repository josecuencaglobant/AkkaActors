import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.persistence.typed.PersistenceId;
import akka.persistence.typed.javadsl.CommandHandler;
import akka.persistence.typed.javadsl.Effect;
import akka.persistence.typed.javadsl.EventHandler;
import akka.persistence.typed.javadsl.EventSourcedBehavior;

import java.util.ArrayList;
import java.util.List;

public class MyPersistentBehavior extends EventSourcedBehavior<
        MyPersistentBehavior.Command, MyPersistentBehavior.Event, MyPersistentBehavior.State> {

interface Command {}

interface Event {}

    private ActorRef<State> subscriber;

    public static Behavior<Command> create(PersistenceId persistenceId, ActorRef<State> actorRef) {
        return new MyPersistentBehavior(persistenceId,actorRef);
    }

    private MyPersistentBehavior(PersistenceId persistenceId,ActorRef<State> actorRef) {
        super(persistenceId);
        this.subscriber = actorRef;
    }

    @Override
    public State emptyState() {
        return new State();
    }

    @Override
    public CommandHandler<Command, Event, State> commandHandler() {
        return newCommandHandlerBuilder()
                .forAnyState()
                .onCommand(Add.class, this::onAdd)
                .onCommand(Clear.class, this::onClear)
                .build();
    }

    private Effect<Event, State> onAdd(Add command) {
        return Effect()
                .persist(new Added(command.data))
                .thenRun(newState -> subscriber.tell(newState));
    }

    private Effect<Event, State> onClear(Clear command) {
        return Effect()
                .persist(Cleared.INSTANCE)
                .thenRun(newState -> subscriber.tell(newState))
                .thenStop();
    }

    @Override
    public EventHandler<State, Event> eventHandler() {
        return newEventHandlerBuilder()
                .forAnyState()
                .onEvent(Added.class, (state, event) -> state.addItem(event.data))
                .onEvent(Cleared.class, () -> new State())
                .build();
    }

    public static class Add implements Command {
        public final String data;

        public Add(String data) {
            this.data = data;
        }
    }

    public enum Clear implements Command {
        INSTANCE
    }

    public static class Added implements Event, MySerializable {
        public final String data;

        public Added(String data) {
            this.data = data;
        }
    }

    public enum Cleared implements Event {
        INSTANCE
    }

    public static class State {
        public final List<String> items;

        private State(List<String> items) {
            this.items = items;
        }

        public State() {
            this.items = new ArrayList<>();
        }

        public State addItem(String data) {
            List<String> newItems = new ArrayList<>(items);
            newItems.add(0, data);
            // keep 5 items
            List<String> latest = newItems.subList(0, Math.min(5, newItems.size()));
            return new State(latest);
        }
    }


}
