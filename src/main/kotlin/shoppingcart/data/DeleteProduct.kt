package shoppingcart.data

import shoppingcart.model.CartCommand

class DeleteProduct(var product: Product, val operation: String = CartState.DELETE_PRODUCT): CartCommand {
}