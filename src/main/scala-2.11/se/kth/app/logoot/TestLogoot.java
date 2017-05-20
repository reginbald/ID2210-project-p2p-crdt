package se.kth.app.logoot;

import org.junit.Test;
import scala.collection.Seq;
import scala.collection.mutable.ArraySeq;
import scala.collection.mutable.ListBuffer;
import scala.collection.mutable.Map;
import se.kth.app.sim.ScenarioSetup;
import se.sics.kompics.Kompics;
import se.sics.kompics.network.Address;
import se.sics.kompics.sl.Init;
import se.sics.ktoolbox.util.network.KAddress;


public class TestLogoot {
    @Test
    public void CreateCorrectLineId() {

        Logoot l = new Logoot(null);

        KAddress node1 = ScenarioSetup.getNodeAdr("127.0.0.1", 1);
        KAddress node3 = ScenarioSetup.getNodeAdr("127.0.0.1", 3);
        KAddress node4 = ScenarioSetup.getNodeAdr("127.0.0.1", 4);
        KAddress node5 = ScenarioSetup.getNodeAdr("127.0.0.1", 5);
        KAddress node9 = ScenarioSetup.getNodeAdr("127.0.0.1", 9);

        ListBuffer<Position> pPos = new ListBuffer<>();
        pPos.$plus$eq$colon(new Position(59, node9, 5));
        pPos.$plus$eq$colon(new Position(2, node4, 7));

        ListBuffer<Position> qPos = new ListBuffer<>();
        qPos.$plus$eq$colon(new Position(3, node3, 9));
        qPos.$plus$eq$colon(new Position(20, node3, 6));
        qPos.$plus$eq$colon(new Position(10, node5, 3));

        LineId p = new LineId(pPos);
        LineId q = new LineId(qPos);

        ListBuffer<LineId> out = l.generateLineId(p, q,10, 10, node1);

        System.out.print(out.toString());
    }
}
