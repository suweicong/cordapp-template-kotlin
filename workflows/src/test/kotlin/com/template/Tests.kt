//package com.example.client
package com.template

import com.template.flows.ExampleFlowWithFixedToken
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

        val issue_amount = 90000L
        val transafer_amount = 1L
        val args_a = "localhost:10006"
        val nodeAddress_a = NetworkHostAndPort.parse(args_a)
        val client_a = CordaRPCClient(nodeAddress_a)
        val proxy_a = client_a.start("user1", "test").proxy
        val a = proxy_a.partiesFromName("PartyA", exactMatch = false).first()

        proxy_a.startFlow(::ExampleFlowWithFixedToken, "MXN", issue_amount, a)


        for (i in 0..100) {
            proxy_a.startFlow(::MoveEvolvableFungibleTokenFlow, a, transafer_amount, a)
            println(i)
            Thread.sleep(30)

        }
    }
}