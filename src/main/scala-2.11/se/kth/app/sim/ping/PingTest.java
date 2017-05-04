package se.kth.app.sim.ping;

import org.junit.Assert;
import org.junit.Test;
import se.kth.app.sim.ScenarioGen;
import se.kth.app.sim.ScenarioSetup;
import se.kth.app.sim.SimulationResultMap;
import se.kth.app.sim.SimulationResultSingleton;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.run.LauncherComp;

/**
 * Created by reginbald on 04/05/2017.
 */
public class PingTest {

    private final SimulationResultMap res = SimulationResultSingleton.getInstance();

    @Test
    public void base() {
        SimulationScenario.setSeed(ScenarioSetup.scenarioSeed());
        SimulationScenario simpleBootScenario = ScenarioGen.simpleBoot();
        simpleBootScenario.simulate(LauncherComp.class);

        //int node1_sent = res.get("/192.168.0.1:45678sent", Integer.class);

        Assert.assertEquals(true, true);
    }
}
