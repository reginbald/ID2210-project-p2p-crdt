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
        SimulationScenario simpleBootScenario = LogootTestScenarioGen.simpleBoot();
        simpleBootScenario.simulate(LauncherComp.class);

        int node1_patch = res.get("1patch", Integer.class);
        String node1_doc = res.getString("1doc");


        // All should send 5 pings
        Assert.assertEquals("Node 1 should send 5 pings", 5, node1_patch);
    }
}

