import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class PersistentActor extends AbstractBehavior<MyPersistentBehavior.State> {

    public static Behavior<MyPersistentBehavior.State> behavior(){
        return Behaviors.setup(
          c -> new PersistentActor(c)
        );
    }

    @Override
    public Receive<MyPersistentBehavior.State> createReceive() {
        return newReceiveBuilder()
                .onMessage(MyPersistentBehavior.State.class,this::printMessage)
                .build();
    }

    public PersistentActor(ActorContext<MyPersistentBehavior.State> context){
        super(context);
    }

    private Behavior<MyPersistentBehavior.State> printMessage(MyPersistentBehavior.State msg){
        System.out.println("Ingresé un dato:" + msg.items.size());
        return this;
    }

}
