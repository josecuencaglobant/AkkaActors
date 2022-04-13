package shoppingcart.data

import akka.persistence.typed.crdt.Counter
import shoppingcart.model.ShoppingCartEvent

data class ItemUpdated(var productId: String, var updated: Counter.Updated): ShoppingCartEvent