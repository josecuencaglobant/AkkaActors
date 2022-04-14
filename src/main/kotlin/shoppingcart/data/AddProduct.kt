package shoppingcart.data

import shoppingcart.model.CartCommand

class AddProduct(var product: Product, val operation: String = CartState.ADD_PRODUCT): CartCommand {
}