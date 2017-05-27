package se.kth.app.sim.ping;

import org.junit.Assert;
import org.junit.Test;
import se.kth.app.sim.ScenarioSetup;
import se.kth.app.sim.SimulationResultMap;
import se.kth.app.sim.SimulationResultSingleton;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.run.LauncherComp;

public class PingTest {

    private final SimulationResultMap res = SimulationResultSingleton.getInstance();

    @Test
    public void AllCorrectEventuallyAllDeliver() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed());
        SimulationScenario simpleBootScenario = PingTestScenarioGen.simpleBoot();
        simpleBootScenario.simulate(LauncherComp.class);

        int node1_sent = res.get("1sent", Integer.class);
        int node1_ping = res.get("1ping", Integer.class);
        int node1_pong = res.get("1pong", Integer.class);

        int node2_sent = res.get("2sent", Integer.class);
        int node2_ping = res.get("2ping", Integer.class);
        int node2_pong = res.get("2pong", Integer.class);

        int node3_sent = res.get("3sent", Integer.class);
        int node3_ping = res.get("3ping", Integer.class);
        int node3_pong = res.get("3pong", Integer.class);

        int node4_sent = res.get("4sent", Integer.class);
        int node4_ping = res.get("4ping", Integer.class);
        int node4_pong = res.get("4pong", Integer.class);

        int node5_sent = res.get("5sent", Integer.class);
        int node5_ping = res.get("5ping", Integer.class);
        int node5_pong = res.get("5pong", Integer.class);

        // All should send 5 pings
        Assert.assertEquals("Node 1 should send 5 pings", 5, node1_sent);
        Assert.assertEquals("Node 2 should send 5 pings", 5, node2_sent);
        Assert.assertEquals("Node 3 should send 5 pings", 5, node3_sent);
        Assert.assertEquals("Node 4 should send 5 pings", 5, node4_sent);
        Assert.assertEquals("Node 5 should send 5 pings", 5, node5_sent);

        // All should receive 4 pings
        Assert.assertEquals("Node 1 should get 5*5 pings",5*5, node1_ping);
        Assert.assertEquals("Node 2 should get 5*5 pings",5*5, node2_ping);
        Assert.assertEquals("Node 3 should get 5*5 pings",5*5, node3_ping);
        Assert.assertEquals("Node 4 should get 5*5 pings",5*5, node4_ping);
        Assert.assertEquals("Node 5 should get 5*5 pings",5*5, node5_ping);

        // All should receive 4 pongs
        Assert.assertEquals("Node 1 should get 5*5 pongs", 5*5, node1_pong);
        Assert.assertEquals("Node 2 should get 5*5 pongs", 5*5, node2_pong);
        Assert.assertEquals("Node 3 should get 5*5 pongs", 5*5, node3_pong);
        Assert.assertEquals("Node 4 should get 5*5 pongs", 5*5, node4_pong);
        Assert.assertEquals("Node 5 should get 5*5 pongs", 5*5, node5_pong);
    }

    @Test
    public void CorrectNodeSendAllCorrectNodesDeliver() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed());
        SimulationScenario simpleBootScenario = PingTestScenarioGen.killOne();
        simpleBootScenario.simulate(LauncherComp.class);

        int node1_sent = res.get("1sent", Integer.class);
        int node1_ping = res.get("1ping", Integer.class);
        int node1_pong = res.get("1pong", Integer.class);

        int node2_sent = res.get("2sent", Integer.class);
        int node2_ping = res.get("2ping", Integer.class);
        int node2_pong = res.get("2pong", Integer.class);

        int node3_sent = res.get("3sent", Integer.class);
        int node3_ping = res.get("3ping", Integer.class);
        int node3_pong = res.get("3pong", Integer.class);

        int node4_sent = res.get("4sent", Integer.class);
        int node4_ping = res.get("4ping", Integer.class);
        int node4_pong = res.get("4pong", Integer.class);

        int node5_sent = res.get("5sent", Integer.class);
        int node5_ping = res.get("5ping", Integer.class);
        int node5_pong = res.get("5pong", Integer.class);

        // All should send 5 pings
        //Assert.assertEquals("Node 1 should send 5 pings", 5, node1_sent); faulty
        Assert.assertEquals("Node 2 should send 5 pings", 5, node2_sent);
        Assert.assertEquals("Node 3 should send 5 pings", 5, node3_sent);
        Assert.assertEquals("Node 4 should send 5 pings", 5, node4_sent);
        Assert.assertEquals("Node 5 should send 5 pings", 5, node5_sent);

        // All should receive 4 pings
        //Assert.assertEquals("Node 1 should get 4*5 pings",4*5, node1_ping); faulty
        Assert.assertTrue("Node 2 should deliver all pings sent from all correct nodes", node3_sent + node4_sent + node5_sent <= node2_ping - node2_sent && node2_ping - node2_sent <= node1_sent + node3_sent + node4_sent + node5_sent);
        Assert.assertTrue("Node 3 should deliver all pings sent from all correct nodes", node2_sent + node4_sent + node5_sent <= node3_ping - node3_sent && node3_ping - node3_sent <= node1_sent + node2_sent + node4_sent + node5_sent);
        Assert.assertTrue("Node 4 should deliver all pings sent from all correct nodes", node2_sent + node3_sent + node5_sent <= node4_ping - node4_sent && node4_ping - node4_sent <= node1_sent + node3_sent + node2_sent + node5_sent);
        Assert.assertTrue("Node 5 should deliver all pings sent from all correct nodes", node2_sent + node3_sent + node4_sent <= node5_ping - node5_sent && node5_ping - node5_sent <= node1_sent + node3_sent + node4_sent + node2_sent);


        // All should receive 4 pongs
        //Assert.assertEquals("Node 1 should get 4*5 pongs", 4*5, node1_pong); faulty
        Assert.assertTrue("Node 2 should deliver all pongs sent from all correct nodes", node3_sent + node4_sent + node5_sent <= node2_pong - node2_sent && node2_pong - node2_sent <= node1_sent + node3_sent + node4_sent + node5_sent);
        Assert.assertTrue("Node 3 should deliver all pongs sent from all correct nodes", node2_sent + node4_sent + node5_sent <= node3_pong - node3_sent && node3_pong - node3_sent <= node1_sent + node2_sent + node4_sent + node5_sent);
        Assert.assertTrue("Node 4 should deliver all pongs sent from all correct nodes", node2_sent + node3_sent + node5_sent <= node4_pong - node4_sent && node4_pong - node4_sent <= node1_sent + node3_sent + node2_sent + node5_sent);
        Assert.assertTrue("Node 5 should deliver all pongs sent from all correct nodes", node2_sent + node3_sent + node4_sent <= node5_pong - node5_sent && node5_pong - node5_sent <= node1_sent + node3_sent + node4_sent + node2_sent);
    }

    @Test
    public void CorrectNodeDeliverAllCorrectNodesDeliver() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed());
        SimulationScenario simpleBootScenario = PingTestScenarioGen.killOne();
        simpleBootScenario.simulate(LauncherComp.class);

        int node2_ping = res.get("2ping", Integer.class);
        int node3_ping = res.get("3ping", Integer.class);
        int node4_ping = res.get("4ping", Integer.class);
        int node5_ping = res.get("5ping", Integer.class);

        // Verify that all correct nodes deliver the same amount
        Assert.assertTrue("Node 2 and 3 should deliver the same amount of pings", node2_ping == node3_ping);
        Assert.assertTrue("Node 2 and 4 should deliver the same amount of pings", node2_ping == node4_ping);
        Assert.assertTrue("Node 2 and 5 should deliver the same amount of pings", node2_ping == node5_ping);
    }
}
