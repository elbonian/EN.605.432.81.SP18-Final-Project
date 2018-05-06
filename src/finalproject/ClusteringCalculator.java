/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.util.HashMap;
import java.util.Iterator;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

/**
 *
 * Class to calculate clustering coefficient of a graph and a specific vertex.
 *
 * @author Marcel Llopis <marcelr@jpl.nasa.gov>
 */
public class ClusteringCalculator {

    public static double calculateAverageClusteringCoefficient(TinkerGraph graph) {

        GraphTraversalSource g = graph.traversal();
        GraphTraversal<Vertex, Vertex> t = g.V().unfold();
        double averageCC = 0.0;
        int vertexCount = 1;

        //Iteration over all vertices
        while (t.hasNext()) {

            Vertex v = t.next();
            double d = calculateVertecClusteringCoefficient(v);
            //Average clustering coefficient
            averageCC = (averageCC * (vertexCount - 1) + d) / (double) vertexCount;
            vertexCount++;

        }

        return averageCC;

    }

    /**
     * This is to calculate the clustering coefficient of one vertex.
     */
    public static double calculateVertecClusteringCoefficient(Vertex v) {

        Iterator<Vertex> neighbors = v.vertices(Direction.BOTH);
        HashMap<String, String> neighborMap = new HashMap<String, String>();

        //Create a map so we know who is a neighbor of this vertex
        while (neighbors.hasNext()) {

            Vertex neighbor = neighbors.next();
            if (!neighbor.toString().contains(v.toString())) {
                neighborMap.put(neighbor.toString(), "isNeighbor");
            }

        }

        int linkCount = 0;
        neighbors = v.vertices(Direction.BOTH);
        while (neighbors.hasNext()) {

            Vertex neighbor = neighbors.next();
            Iterator<Vertex> neighborsOfNeighbor = neighbor.vertices(Direction.BOTH);

            while (neighborsOfNeighbor.hasNext()) {

                Vertex neighborOfNeighbor = neighborsOfNeighbor.next();
                String value = neighborMap.get(neighborOfNeighbor.toString());
                if (value != null) {
                    linkCount++;
                }

            }

        }

        if (neighborMap.size() > 1) {
            return ((double) linkCount) / (neighborMap.size() * (neighborMap.size() - 1));
        } else {
            return 0;
        }

    }

}
