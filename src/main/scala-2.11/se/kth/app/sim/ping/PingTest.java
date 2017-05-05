package se.kth.app.sim.ping;

import org.junit.Assert;
import org.junit.Test;
import se.kth.app.sim.ScenarioGen;
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
        SimulationScenario simpleBootScenario = ScenarioGen.simpleBoot();
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

        // All should send 1 ping
        Assert.assertEquals("Node 1 should send 1 ping",1, node1_sent);
        Assert.assertEquals("Node 2 should send 1 ping",1, node2_sent);
        Assert.assertEquals("Node 3 should send 1 ping",1, node3_sent);
        Assert.assertEquals("Node 4 should send 1 ping",1, node4_sent);
        Assert.assertEquals("Node 5 should send 1 ping",1, node5_sent);

        // All should receive 4 pings
        Assert.assertEquals("Node 1 should get 4 pings",4, node1_ping);
        Assert.assertEquals("Node 2 should get 4 pings",4, node2_ping);
        Assert.assertEquals("Node 3 should get 4 pings",4, node3_ping);
        Assert.assertEquals("Node 4 should get 4 pings",4, node4_ping);
        Assert.assertEquals("Node 5 should get 4 pings",4, node5_ping);

        // All should receive 4 pongs
        Assert.assertEquals("Node 1 should get 4 pongs",4, node1_pong);
        Assert.assertEquals("Node 2 should get 4 pongs",4, node2_pong);
        Assert.assertEquals("Node 3 should get 4 pongs",4, node3_pong);
        Assert.assertEquals("Node 4 should get 4 pongs",4, node4_pong);
        Assert.assertEquals("Node 5 should get 4 pongs",4, node5_pong);
    }
}
