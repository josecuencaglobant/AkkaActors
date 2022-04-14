import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.persistence.typed.PersistenceId;
import akka.persistence.typed.ReplicaId;


import java.util.*;

public class App {

    //Interactions
    //https://doc.akka.io/docs/akka/current/typed/interaction-patterns.html

    public static void main(String[] args) {
        //shoppingCart();
        eventHandlerRegular();
    }

    private static void eventHandlerRegular(){
        ActorSystem<MyPersistentBehavior.Command> system = ActorSystem.create(
                MyPersistentBehavior.create(new PersistenceId("12345")),"system"
        );
        ActorRef<MyPersistentBehavior.Command> ref = system;
        ref.tell( new MyPersistentBehavior.Add("hola") );
    }

    private static void shoppingCart(){
        Set<ReplicaId> set = Set.of(new ReplicaId("A"), new ReplicaId("B"));

        ActorSystem<ShoppingCart.Command> system = ActorSystem.create(
                ShoppingCart.create("shopping",new ReplicaId("Hola"),set),"system"
        );

        ActorRef<ShoppingCart.Command> actor = system;

        actor.tell(new ShoppingCart.AddItem("123",2));
        actor.tell(new ShoppingCart.RemoveItem("123",7));
    }


}
