package db

class FakeDB {

    companion object{
        private val db = HashMap<String, Double>()
        private const val NOT_FOUND_VALUE = Double.NaN
        private const val NOT_FOUND_USER = "User does not exist"
        private const val ADD_VALUE_OK = "Value Added"
        private const val NOT_ENOUGH_VALUE = "Insufficient Data"
        private const val WITHDRAW_OK = "Value Withdraw"
        const val USER1 = "user1"
        const val USER2 = "user2"

        fun init(){
            db[USER1] = 0.0
            db[USER2] = 0.0
        }

        fun getData(id: String): String{
            return "$id : ${db.getOrDefault(id, NOT_FOUND_VALUE)}"
        }

        fun addData(id: String, value: Double): String{
            return if(!db.containsKey(id)){
                NOT_FOUND_USER
            }else{
                db[id] = db[id]!!.plus(value)
                return ADD_VALUE_OK
            }
        }

        fun withDrawData(id: String, value: Double): String{
            return if(!db.containsKey(id)){
                NOT_FOUND_USER
            }else{
                if( value > db[id]!!){
                    NOT_ENOUGH_VALUE
                }else{
                    db[id] = db[id]!!.minus(value)
                    WITHDRAW_OK
                }
            }
        }

    }

}