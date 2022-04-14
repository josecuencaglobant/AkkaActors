package shoppingcart.data

import shoppingcart.model.CartCommand
import shoppingcart.model.CartEvent
import shoppingcart.model.CartSerializable

class ProductAdded(var product: Product, val operation: String = CartState.ADD_PRODUCT): CartEvent,CartSerializable {
}