package no.nav.syfo.service

import no.nav.syfo.ToggleEnv
import no.nav.syfo.consumer.distribuerjournalpost.DistibusjonsType
import no.nav.syfo.kafka.consumers.varselbus.domain.ArbeidstakerHendelse
import no.nav.syfo.kafka.consumers.varselbus.domain.HendelseType.SM_FORHANDSVARSEL_STANS
import no.nav.syfo.kafka.consumers.varselbus.domain.VarselData
import no.nav.syfo.kafka.consumers.varselbus.domain.toVarselData
import no.nav.syfo.metrics.tellAktivitetskravFraIsAktivitetskrav
import org.apache.commons.cli.MissingArgumentException
import org.slf4j.LoggerFactory
import java.io.IOException

class AktivitetskravVarselService(
    val senderFacade: SenderFacade,
    val accessControlService: AccessControlService,
    val isNewSendingEnabled: Boolean
) {
    private val log = LoggerFactory.getLogger(AktivitetskravVarselService::class.qualifiedName)

    fun sendVarselTilArbeidstaker(varselHendelse: ArbeidstakerHendelse) {
        if (!isNewSendingEnabled) {
            tellAktivitetskravFraIsAktivitetskrav()
            if (varselHendelse.type == SM_FORHANDSVARSEL_STANS) {
                val varselData = dataToVarselData(varselHendelse.data)

                requireNotNull(varselData.journalpost?.id)

                sendFysiskBrevTilArbeidstaker(
                    varselData.journalpost!!.uuid,
                    varselHendelse,
                    varselData.journalpost.id!!
                )
            }
        }
    }

    private fun sendFysiskBrevTilArbeidstaker(
        uuid: String,
        arbeidstakerHendelse: ArbeidstakerHendelse,
        journalpostId: String,
    ) {
        try {
            senderFacade.sendBrevTilFysiskPrint(uuid, arbeidstakerHendelse, journalpostId, DistibusjonsType.VIKTIG)
        } catch (e: RuntimeException) {
            log.info("Feil i sending av fysisk brev om dialogmote: ${e.message} for hendelsetype: ${arbeidstakerHendelse.type.name}")
        }
    }

    private fun dataToVarselData(data: Any?): VarselData {
        return data?.let {
            try {
                return data.toVarselData()
            } catch (e: IOException) {
                throw IOException("ArbeidstakerHendelse har feil format")
            }
        } ?: throw MissingArgumentException("EsyfovarselHendelse mangler 'data'-felt")
    }

}