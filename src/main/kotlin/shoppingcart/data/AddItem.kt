package shoppingcart.data

import shoppingcart.model.ShoppingCartCommand

data class AddItem(var productId: String, var count: Int):ShoppingCartCommand