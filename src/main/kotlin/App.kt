import actors.DataManageActor
import actors.VerificationActor
import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.SupervisorStrategy
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.GroupRouter
import akka.actor.typed.javadsl.PoolRouter
import akka.actor.typed.javadsl.Routers
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.receptionist.ServiceKey
import db.FakeDB
import msg.AddDataInstruction
import msg.CheckDataInstruction
import msg.WithDrawData
import msg.instructions.DataInstruction

fun main(){
    FakeDB.init()

    var verificationActorWorker: ActorRef<DataInstruction>  = ActorSystem
        .create( Behaviors.supervise( VerificationActor.behavior() ).onFailure(
            SupervisorStrategy.restart()
        ) ,"verification")
    var serviceKey: ServiceKey<DataInstruction> = ServiceKey.create(
        DataInstruction::class.java,"service-key"
    )
    var groupRouter: GroupRouter<DataInstruction> = Routers.group(serviceKey)
        .withConsistentHashingRouting(3) { msg -> msg.consistentHashKey() }
    var verificationActor: ActorSystem<DataInstruction> = ActorSystem.create(
        groupRouter,"verificationActor"
    )
    verificationActor.receptionist().tell(Receptionist.register(serviceKey,verificationActorWorker))


    var poolDataManager: PoolRouter<DataInstruction> = Routers.pool(
        100,
        Behaviors.supervise( DataManageActor.behavior(verificationActor) ).onFailure(
            SupervisorStrategy.restart()
        )
    )

    var router: ActorRef<DataInstruction> = ActorSystem.create(
        poolDataManager.withRoundRobinRouting(),"managerRouting")

    router.tell( CheckDataInstruction( FakeDB.USER1 ) )
    router.tell( AddDataInstruction( FakeDB.USER1, 10.0 ) )
    router.tell(WithDrawData(FakeDB.USER1,5.0))
    router.tell( CheckDataInstruction( FakeDB.USER1 ) )
    router.tell(WithDrawData(FakeDB.USER1,6.0))

    router.tell( CheckDataInstruction( FakeDB.USER2 ) )
    router.tell( AddDataInstruction( FakeDB.USER2, 10.0 ) )
    router.tell(WithDrawData(FakeDB.USER2,5.0))
    router.tell( CheckDataInstruction( FakeDB.USER2 ) )
    router.tell(WithDrawData(FakeDB.USER2,6.0))

}