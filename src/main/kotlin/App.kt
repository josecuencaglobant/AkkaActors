import actors.DataManageActor
import actors.VerificationActor
import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.SupervisorStrategy
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.PoolRouter
import akka.actor.typed.javadsl.Routers
import db.FakeDB
import msg.CheckDataInstruction
import msg.instructions.DataInstruction
import javax.xml.crypto.Data

fun main(){
    FakeDB.init()
    println( FakeDB.addData(FakeDB.USER1, 10.0) )
    println( FakeDB.withDrawData( FakeDB.USER1 , 80.0 ) )
    println( FakeDB.getData(FakeDB.USER1) )

    var verificationActor: ActorRef<DataInstruction>  = ActorSystem
        .create(VerificationActor.behavior(),"verification")

    var poolDataManager: PoolRouter<DataInstruction> = Routers.pool(
        4,
        Behaviors.supervise( DataManageActor.behavior(verificationActor) ).onFailure(
            SupervisorStrategy.restart()
        )
    )

    var router: ActorRef<DataInstruction> = ActorSystem.create(
        poolDataManager.withRoundRobinRouting(),"managerRouting")

    router.tell( CheckDataInstruction( FakeDB.USER1 ) )
    router.tell( CheckDataInstruction( FakeDB.USER2 ) )

}