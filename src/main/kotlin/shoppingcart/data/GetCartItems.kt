package shoppingcart.data

import akka.actor.typed.ActorRef
import shoppingcart.model.ShoppingCartCommand

data class GetCartItems(var replyTo: ActorRef<CartItems>): ShoppingCartCommand