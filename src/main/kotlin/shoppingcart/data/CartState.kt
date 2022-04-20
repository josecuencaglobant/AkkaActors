package shoppingcart.data

import java.lang.StringBuilder

class CartState(private val products: ArrayList<ProductOperation> = ArrayList()) {

    fun addOperation(product: Product, operation: String): CartState{
        products.add( ProductOperation(product,operation) )
        return CartState(products)
    }

    override fun toString(): String {
        var msg = StringBuilder()
        for(data in products){
            var operation = data.operation
            var product = data.product
            msg.append("Operation: $operation. Product: ${product.name} ($${product.price})")
            msg.append("\n")
        }
        return msg.toString()
    }

    companion object{
        val ADD_PRODUCT = "Add Product"
        val DELETE_PRODUCT = "Delete Product"
    }

}

data class ProductOperation(var product: Product,var operation:String)