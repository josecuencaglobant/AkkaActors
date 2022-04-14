package shoppingcart.data

import shoppingcart.model.CartEvent
import shoppingcart.model.CartSerializable

class ProductDeleted(var product: Product, val operation: String = CartState.DELETE_PRODUCT): CartEvent,CartSerializable {
}