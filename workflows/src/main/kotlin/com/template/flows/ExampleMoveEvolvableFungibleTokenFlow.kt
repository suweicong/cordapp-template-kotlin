package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType
import com.r3.corda.lib.tokens.contracts.types.TokenType
import com.r3.corda.lib.tokens.contracts.utilities.heldBy
import com.r3.corda.lib.tokens.contracts.utilities.issuedBy
import com.r3.corda.lib.tokens.contracts.utilities.of
import com.r3.corda.lib.tokens.money.FiatCurrency
import com.r3.corda.lib.tokens.workflows.flows.rpc.CreateEvolvableTokens
import com.r3.corda.lib.tokens.workflows.flows.rpc.IssueTokens
import com.r3.corda.lib.tokens.workflows.flows.rpc.MoveFungibleTokens
import com.r3.corda.lib.tokens.workflows.types.PartyAndAmount
import com.template.states.ExampleEvolvableTokenType
import net.corda.core.contracts.Amount
import net.corda.core.contracts.TransactionState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.FlowException
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.StartableByRPC
import net.corda.core.identity.Party
import net.corda.core.node.services.queryBy
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.ProgressTracker
import java.util.*

@StartableByRPC
class MoveEvolvableFungibleTokenFlow(
        val holder: Party,
        val quantity: Long,
        val auditor : Party) : FlowLogic<SignedTransaction>() {

    override val progressTracker = ProgressTracker()



    @Suspendable
    @Throws(FlowException::class)
    override fun call(): SignedTransaction {
        val tokenMXN : TokenType = FiatCurrency.getInstance("MXN");
        val amount = Amount(quantity,tokenMXN);
        val partyAndAmount = PartyAndAmount(holder,amount)
        if(holder.equals(auditor)) {
            return subFlow(MoveFungibleTokens(partyAndAmount = partyAndAmount))
        } else {
            return subFlow(MoveFungibleTokens(partyAndAmount = partyAndAmount, observers = listOf(auditor)))
        }
    }
}
