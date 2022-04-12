package msg.instructions

open class DataInstruction{

    lateinit var usrId: String
    lateinit var operationType: String

    companion object{
        const val CHECK_BALANCE = "Check Balance"
        const val ADD_VALUE = "Add Value"
        const val WITHDRAW_DATA = "Withdraw data"
    }

}