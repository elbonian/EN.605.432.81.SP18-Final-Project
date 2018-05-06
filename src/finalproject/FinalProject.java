/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import finalproject.utils.GraphUtils;
import java.io.File;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

/**
 *
 * @author marcelr
 */
public class FinalProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println("Calculating scale-free graph...");
        BarabasiAlbertGenerator bag = new BarabasiAlbertGenerator(1000);
        TinkerGraph graph = bag.generateGraph(true);
        GraphUtils.saveGraphML(graph, "graphs" + File.separator + "scalefree.graphml");
        double cc = ClusteringCalculator.calculateAverageClusteringCoefficient(graph);
        System.out.println("\tCC: " + cc);

        for (int i = 1; i <= 10; i++) {

            System.out.println("Run "+ i +" of the clustering enhancer...");
            ClusteringEnhancer ce = new ClusteringEnhancer(graph);
            graph = ce.enhanceGraphClustering();
            cc = ClusteringCalculator.calculateAverageClusteringCoefficient(graph);
            System.out.println("\tCC: " + cc);
            GraphUtils.saveGraphML(graph, "graphs" + File.separator + "enhanced" + i + ".graphml");

        }

    }

}
