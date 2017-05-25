package se.kth.app.sim.logoot;

import org.junit.Assert;
import org.junit.Test;
import se.kth.app.sim.ScenarioSetup;
import se.kth.app.sim.SimulationResultMap;
import se.kth.app.sim.SimulationResultSingleton;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.run.LauncherComp;

public class LogootTest {

    private final SimulationResultMap res = SimulationResultSingleton.getInstance();

    @Test
    public void Insert() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed());
        SimulationScenario simpleBootScenario = LogootTestScenarioGen.insertBoot();
        simpleBootScenario.simulate(LauncherComp.class);

        int node1_patch = res.get("1patch", Integer.class);
        int node2_patch = res.get("2patch", Integer.class);
        int node3_patch = res.get("3patch", Integer.class);
        int node4_patch = res.get("4patch", Integer.class);
        int node5_patch = res.get("5patch", Integer.class);
        String node1_doc = res.getString("1doc");
        String node2_doc = res.getString("2doc");
        String node3_doc = res.getString("3doc");
        String node4_doc = res.getString("4doc");
        String node5_doc = res.getString("5doc");


        // All Nodes should send 3 patches
        Assert.assertEquals("Node1 should send 3 patches", node1_patch, 3);
        Assert.assertEquals("Node2 should send 3 patches", node2_patch, 3);
        Assert.assertEquals("Node3 should send 3 patches", node3_patch, 3);
        Assert.assertEquals("Node4 should send 3 patches", node4_patch, 3);
        Assert.assertEquals("Node5 should send 3 patches", node5_patch, 3);

        // Document at all nodes should be the same after insert
        Assert.assertEquals("Document at all nodes should be the same after insert", node1_doc, node2_doc);
        Assert.assertEquals("Document at all nodes should be the same after insert", node1_doc, node3_doc);
        Assert.assertEquals("Document at all nodes should be the same after insert", node1_doc, node4_doc);
        Assert.assertEquals("Document at all nodes should be the same after insert", node1_doc, node5_doc);

        // The content of the document should include all inserts
        Assert.assertEquals("Document should have all insert operations", node1_doc, "-start- mom 3 dad 3 eric 3 mom 3 dad 3 eric 3 mom 3 dad 3 eric 3 mom 3 dad 3 eric 3 mom 3 dad 3 eric 3 mom 2 dad 2 eric 2 mom 2 dad 2 eric 2 mom 2 dad 2 eric 2 mom 2 dad 2 eric 2 mom 2 dad 2 eric 2 mom 1 mom 1 mom 1 mom 1 mom 1 dad 1 eric 1 dad 1 eric 1 dad 1 eric 1 dad 1 eric 1 dad 1 eric 1-end-");
    }

    @Test
    public void Remove() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed());
        SimulationScenario simpleBootScenario = LogootTestScenarioGen.removeBoot();
        simpleBootScenario.simulate(LauncherComp.class);

        int node1_patch = res.get("1patch", Integer.class);
        int node2_patch = res.get("2patch", Integer.class);
        int node3_patch = res.get("3patch", Integer.class);
        String node1_doc = res.getString("1doc");
        String node2_doc = res.getString("2doc");
        String node3_doc = res.getString("3doc");


        // All Nodes should send 3 patches
        Assert.assertEquals("Node1 should send 3 patches", node1_patch, 3);
        Assert.assertEquals("Node2 should send 3 patches", node2_patch, 3);
        Assert.assertEquals("Node3 should send 3 patches", node3_patch, 3);

        // Document at all nodes should be the same after remove
        Assert.assertEquals("Document at all nodes should be the same after remove", node1_doc, node2_doc);
        Assert.assertEquals("Document at all nodes should be the same after insert", node1_doc, node3_doc);

        Assert.assertEquals("Document should remove first mom insert", node1_doc, "-start- mom 3 dad 3 eric 3 mom 3 dad 3 eric 3 mom 3 dad 3 eric 3 dad 1 eric 1 dad 1 eric 1 dad 1 eric 1-end-");
    }

    @Test
    public void Undo() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed());
        SimulationScenario simpleBootScenario = LogootTestScenarioGen.undoBoot();
        simpleBootScenario.simulate(LauncherComp.class);

        int node1_patch = res.get("1patch", Integer.class);
        String node1_doc = res.getString("1doc");
        String node2_doc = res.getString("2doc");
        String node3_doc = res.getString("3doc");


    }

    @Test
    public void Redo() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed());
        SimulationScenario simpleBootScenario = LogootTestScenarioGen.redoBoot();
        simpleBootScenario.simulate(LauncherComp.class);

        int node1_patch = res.get("1patch", Integer.class);
        String node1_doc = res.getString("1doc");
        String node2_doc = res.getString("2doc");
        String node3_doc = res.getString("3doc");

        // Document at all nodes should be the same after remove
        Assert.assertEquals("Document at all nodes should be the same after remove", node1_doc, node2_doc);
        Assert.assertEquals("Document at all nodes should be the same after insert", node1_doc, node3_doc);
    }
}

