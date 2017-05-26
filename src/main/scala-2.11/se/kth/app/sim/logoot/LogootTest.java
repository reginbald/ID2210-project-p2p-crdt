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
        Assert.assertEquals("Node1 should send 3 patches", 3, node1_patch);
        Assert.assertEquals("Node2 should send 3 patches", 3, node2_patch);
        Assert.assertEquals("Node3 should send 3 patches", 3, node3_patch);
        Assert.assertEquals("Node4 should send 3 patches", 3, node4_patch);
        Assert.assertEquals("Node5 should send 3 patches", 3, node5_patch);

        // Document at all nodes should be the same after insert
        Assert.assertEquals("Document at node 1 should be the same as at node 2", node1_doc, node2_doc);
        Assert.assertEquals("Document at node 1 should be the same as at node 3", node1_doc, node3_doc);
        Assert.assertEquals("Document at node 1 should be the same as at node 4", node1_doc, node4_doc);
        Assert.assertEquals("Document at node 1 should be the same as at node 5", node1_doc, node5_doc);

        // The content of the document should include all inserts
        Assert.assertEquals("Document at node 1 should contain 5x mom 1", 5, ((node1_doc.length() - node1_doc.replace("mom 1", "").length()) / ("mom 1".length())));
        Assert.assertEquals("Document at node 1 should contain 5x mom 2", 5, (node2_doc.length() - node2_doc.replace("mom 2", "").length()) /("mom 2".length()));
        Assert.assertEquals("Document at node 1 should contain 5x mom 3", 5, (node3_doc.length() - node3_doc.replace("mom 3", "").length()) /("mom 3".length()));

        Assert.assertEquals("Document at node 1 should contain 5x dad 1", 5, ((node1_doc.length() - node1_doc.replace("dad 1", "").length()) / ("dad 1".length())));
        Assert.assertEquals("Document at node 1 should contain 5x dad 2", 5, (node2_doc.length() - node2_doc.replace("dad 2", "").length()) /("dad 2".length()));
        Assert.assertEquals("Document at node 1 should contain 5x dad 3", 5, (node3_doc.length() - node3_doc.replace("dad 3", "").length()) /("dad 3".length()));

        Assert.assertEquals("Document at node 1 should contain 5x eric 1", 5, ((node1_doc.length() - node1_doc.replace("eric 1", "").length()) / ("eric 1".length())));
        Assert.assertEquals("Document at node 1 should contain 5x eric 2", 5, (node2_doc.length() - node2_doc.replace("eric 2", "").length()) /("eric 2".length()));
        Assert.assertEquals("Document at node 1 should contain 5x eric 3", 5, (node3_doc.length() - node3_doc.replace("eric 3", "").length()) /("eric 3".length()));
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
        Assert.assertEquals("Node1 should send 3 patches", 3, node1_patch);
        Assert.assertEquals("Node2 should send 3 patches", 3, node2_patch);
        Assert.assertEquals("Node3 should send 3 patches", 3, node3_patch);

        // Document at all nodes should be the same after remove
        Assert.assertEquals("Document at node 1 should be the same as at node 2", node1_doc, node2_doc);
        Assert.assertEquals("Document at node 1 should be the same as at node 3", node1_doc, node3_doc);

        // Verify that remove was executed
        Assert.assertEquals("Document at node 1 should remove 3x mom 1", 0, ((node1_doc.length() - node1_doc.replace("mom 1", "").length()) / ("mom 1".length())));
        Assert.assertEquals("Document at node 2 should remove 3x mom 1", 0, (node2_doc.length() - node2_doc.replace("mom 1", "").length()) /("mom 1".length()));
        Assert.assertEquals("Document at node 3 should remove 3x mom 1", 0, (node3_doc.length() - node3_doc.replace("mom 1", "").length()) /("mom 1".length()));
    }

    @Test
    public void Undo() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed());
        SimulationScenario simpleBootScenario = LogootTestScenarioGen.undoBoot();
        simpleBootScenario.simulate(LauncherComp.class);

        int node1_patch = res.get("1patch", Integer.class);
        int node2_patch = res.get("2patch", Integer.class);
        int node3_patch = res.get("3patch", Integer.class);
        String node1_doc = res.getString("1doc");
        String node2_doc = res.getString("2doc");
        String node3_doc = res.getString("3doc");

        // All Nodes should send 2 patches and 1 undo
        Assert.assertEquals("Node1 should send 3 patches", 3, node1_patch);
        Assert.assertEquals("Node2 should send 3 patches", 3, node2_patch);
        Assert.assertEquals("Node3 should send 3 patches", 3, node3_patch);

        // Document at all nodes should be the same after undo
        Assert.assertEquals("Document at node 1 should be the same as at node 2", node1_doc, node2_doc);
        Assert.assertEquals("Document at node 1 should be the same as at node 3", node1_doc, node3_doc);

        // Verify that remove was reverted
        Assert.assertEquals("Document at node 1 should contain 3x mom 1", 3, ((node1_doc.length() - node1_doc.replace("mom 1", "").length()) / ("mom 1".length())));
        Assert.assertEquals("Document at node 2 should contain 3x mom 1", 3, (node2_doc.length() - node2_doc.replace("mom 1", "").length()) /("mom 1".length()));
        Assert.assertEquals("Document at node 3 should contain 3x mom 1", 3, (node3_doc.length() - node3_doc.replace("mom 1", "").length()) /("mom 1".length()));
    }

    @Test
    public void Redo() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed());
        SimulationScenario simpleBootScenario = LogootTestScenarioGen.redoBoot();
        simpleBootScenario.simulate(LauncherComp.class);

        int node1_patch = res.get("1patch", Integer.class);
        int node2_patch = res.get("2patch", Integer.class);
        int node3_patch = res.get("3patch", Integer.class);
        String node1_doc = res.getString("1doc");
        String node2_doc = res.getString("2doc");
        String node3_doc = res.getString("3doc");

        // All Nodes should send 2 patches, 1 undo and 1 redo
        Assert.assertEquals("Node1 should send 4 patches", 4, node1_patch);
        Assert.assertEquals("Node2 should send 4 patches", 4, node2_patch);
        Assert.assertEquals("Node3 should send 4 patches", 4, node3_patch);

        //Document at all nodes should be the same after redo
        Assert.assertEquals("Document at node 1 should be the same as at node 2", node1_doc, node2_doc);
        Assert.assertEquals("Document at node 1 should be the same as at node 3", node1_doc, node3_doc);

        // Verify that remove was re-executed
        Assert.assertEquals("Document at node 1 should remove 3x mom 1", 0, ((node1_doc.length() - node1_doc.replace("mom 1", "").length()) / ("mom 1".length())));
        Assert.assertEquals("Document at node 2 should remove 3x mom 1", 0, (node2_doc.length() - node2_doc.replace("mom 1", "").length()) /("mom 1".length()));
        Assert.assertEquals("Document at node 3 should remove 3x mom 1", 0, (node3_doc.length() - node3_doc.replace("mom 1", "").length()) /("mom 1".length()));
    }

    @Test
    public void DoubleUndoOneRedo() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed());
        SimulationScenario simpleBootScenario = LogootTestScenarioGen.doubleUndoOneRedoBoot();
        simpleBootScenario.simulate(LauncherComp.class);

        int node1_patch = res.get("1patch", Integer.class);
        int node2_patch = res.get("2patch", Integer.class);
        int node3_patch = res.get("3patch", Integer.class);
        String node1_doc = res.getString("1doc");
        String node2_doc = res.getString("2doc");
        String node3_doc = res.getString("3doc");

        // All Nodes should send 2 patches, 2 undo and 1 redo
        Assert.assertEquals("Node1 should send 5 patches", 5, node1_patch);
        Assert.assertEquals("Node2 should send 5 patches", 5, node2_patch);
        Assert.assertEquals("Node3 should send 5 patches", 5, node3_patch);

        //Document at all nodes should be the same after redo
        Assert.assertEquals("Document at node 1 should be the same as at node 2", node1_doc, node2_doc);
        Assert.assertEquals("Document at node 1 should be the same as at node 3", node1_doc, node3_doc);

        // Verify that remove was reverted even if there is an undo
        Assert.assertEquals("Document at node 1 should contain 3x mom 1", 3, ((node1_doc.length() - node1_doc.replace("mom 1", "").length()) / ("mom 1".length())));
        Assert.assertEquals("Document at node 2 should contain 3x mom 1", 3, (node2_doc.length() - node2_doc.replace("mom 1", "").length()) /("mom 1".length()));
        Assert.assertEquals("Document at node 3 should contain 3x mom 1", 3, (node3_doc.length() - node3_doc.replace("mom 1", "").length()) /("mom 1".length()));
    }
}

