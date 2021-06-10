package no.nav.syfo.consumer

import kotlinx.coroutines.runBlocking
import no.nav.syfo.auth.StsConsumer
import no.nav.syfo.testEnviornment
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import no.nav.syfo.testutil.mocks.*
import org.amshove.kluent.shouldNotBe
import java.lang.RuntimeException
import kotlin.test.assertFailsWith

const val aktorIdNonReservedUser = aktorId
const val aktorIdReservedUser = aktorId2
const val aktorIdUnsuccessfulCall = aktorId3
const val aktorIdUnknownUser = aktorId4
const val aktorIdInvalid = "${aktorId}-with-invalid-input"

object DkifConsumerSpek : Spek({

    val testEnv = testEnviornment()
    val stsMockServer = StsMockServer(testEnv).mockServer()
    val dkifMockServer = DkifMockServer(testEnv).mockServer()
    val stsConsumer = StsConsumer(testEnv)
    val dkifConsumer = DkifConsumer(testEnv, stsConsumer)

    beforeGroup {
        stsMockServer.start()
        dkifMockServer.start()
    }

    afterGroup {
        stsMockServer.stop(1L, 10L)
        dkifMockServer.stop(1L, 10L)
    }

    describe("DkifConsumerSpek") {
        it("Call DKIF for non-reserved user") {
            val dkifResponse = runBlocking { dkifConsumer.isBrukerReservert(aktorIdNonReservedUser) }
            dkifResponse shouldNotBe null
            dkifResponse!!.kanVarsles shouldEqual true
        }

        it("Call DKIF for reserved user") {
            val dkifResponse = runBlocking { dkifConsumer.isBrukerReservert(aktorIdReservedUser) }
            dkifResponse shouldNotBe null
            dkifResponse!!.kanVarsles shouldEqual false
        }

        it("DKIF consumer should throw RuntimeException when call fails") {
            assertFailsWith(RuntimeException::class) {
                runBlocking { dkifConsumer.isBrukerReservert(aktorIdUnsuccessfulCall) }
            }
        }

        it("DKIF consumer should throw RuntimeException when requesting data for unknown user") {
            assertFailsWith(RuntimeException::class) {
                runBlocking { dkifConsumer.isBrukerReservert(aktorIdUnknownUser) }
            }
        }

        it("DKIF consumer should return null on invalid aktorid") {
            val dkifResponse = runBlocking { dkifConsumer.isBrukerReservert(aktorIdInvalid) }
            dkifResponse shouldEqual null
        }
    }
})
