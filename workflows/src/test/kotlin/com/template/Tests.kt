//package com.example.client
package com.template

import com.template.states.ExampleEvolvableTokenType
import com.template.flows.MoveEvolvableFungibleTokenFlow

import net.corda.client.rpc.CordaRPCClient
import net.corda.core.contracts.StateAndRef
import net.corda.core.messaging.startFlow
import net.corda.core.utilities.NetworkHostAndPort
import net.corda.core.utilities.loggerFor
import org.slf4j.Logger


/**
 *  Demonstration of using the CordaRPCClient to connect to a Corda Node and
 *  steam some State data from the node.
 **/

fun main(args: Array<String>) {
    ExampleClientRPC().main(args)
}

class ExampleClientRPC {
    companion object {
        val logger: Logger = loggerFor<ExampleClientRPC>()
        private fun logState(state: StateAndRef<ExampleEvolvableTokenType>) = logger.info("{}", state.state.data)
    }

    fun main(args: Array<String>) {

        val args = "localhost:10006"
        val nodeAddress = NetworkHostAndPort.parse(args)

        val client = CordaRPCClient(nodeAddress)

        val proxy = client.start("user1", "test").proxy

        val a = proxy.partiesFromName("PartyA", exactMatch = false).first()
        val amount=1L
        for (i in 0..100) {
            proxy.startFlow(::MoveEvolvableFungibleTokenFlow, a, amount, a)
            println(i)
            Thread.sleep(100)

        }
    }
}