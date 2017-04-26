package se.kth.system

import se.kth.app.sim.ScenarioSetup.scenarioSeed
import se.sics.ktoolbox.util.identifiable._
import se.sics.ktoolbox.util.identifiable.overlay.OverlayId
import se.sics.ktoolbox.util.identifiable.overlay.OverlayIdFactory
import se.sics.ktoolbox.util.identifiable.overlay.OverlayRegistry

/**
  * Created by reginbald on 26/04/2017.
  */
object SystemSetup {

  def setup(): OverlayId = {
    BasicIdentifiers.registerDefaults2(scenarioSeed)
    OverlayRegistry.initiate(new OverlayId.BasicTypeFactory(0.toByte), new OverlayId.BasicTypeComparator)
    val croupierOwnerId: Byte = 1
    OverlayRegistry.registerPrefix(OverlayId.BasicTypes.CROUPIER.name, croupierOwnerId)

    val croupierBaseIdFactory: IdentifierFactory[_ <: Identifier] = IdentifierRegistry.lookup(BasicIdentifiers.Values.OVERLAY.toString)
    val croupierIdFactory: OverlayIdFactory = new OverlayIdFactory(croupierBaseIdFactory, OverlayId.BasicTypes.CROUPIER, croupierOwnerId)
    croupierIdFactory.id(new BasicBuilders.StringBuilder("0"))
  }
}