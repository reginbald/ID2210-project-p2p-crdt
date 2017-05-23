package se.kth.app.sim.logoot;

import org.junit.Assert;
import org.junit.Test;
import se.kth.app.sim.ScenarioSetup;
import se.kth.app.sim.SimulationResultMap;
import se.kth.app.sim.SimulationResultSingleton;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.run.LauncherComp;

/**
 * Created by reginbald on 21/05/2017.
 */
public class LogootTest {

    private final SimulationResultMap res = SimulationResultSingleton.getInstance();

    @Test
    public void Insert() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed());
        SimulationScenario simpleBootScenario = LogootTestScenarioGen.insertBoot();
        simpleBootScenario.simulate(LauncherComp.class);

        int node1_patch = res.get("1patch", Integer.class);
        String node1_doc = res.getString("1doc");
        String node2_doc = res.getString("2doc");
        String node3_doc = res.getString("2doc");
        String node4_doc = res.getString("3doc");
        String node5_doc = res.getString("4doc");



        // Document at all nodes should be the same after insert
        Assert.assertEquals("Document at all nodes should be the same after insert", node1_doc, node2_doc);
        Assert.assertEquals("Document at all nodes should be the same after insert", node1_doc, node3_doc);
        Assert.assertEquals("Document at all nodes should be the same after insert", node1_doc, node4_doc);
        Assert.assertEquals("Document at all nodes should be the same after insert", node1_doc, node5_doc);
    }
}

