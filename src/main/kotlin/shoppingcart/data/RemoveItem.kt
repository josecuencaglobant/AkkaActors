package shoppingcart.data

import shoppingcart.model.ShoppingCartCommand

data class RemoveItem(var productId: String, var count: Int): ShoppingCartCommand