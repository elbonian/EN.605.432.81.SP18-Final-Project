/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import finalproject.utils.Misc;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

/**
 *
 * Clustering-enhancing algorithm.
 *
 * @author Marcel Llopis <marcelr@jpl.nasa.gov>
 */
public class ClusteringEnhancer {

    TinkerGraph graph = null;
    HashMap<Vertex, Integer> nodeDegrees = new HashMap<Vertex, Integer>();
    int sumOfDegreesOverOne = 0;

    /**
     * Constructor receives the graph to enhance.
     *
     * @param graph
     */
    public ClusteringEnhancer(TinkerGraph graph) {

        this.graph = graph;

    }

    /**
     * This returns a graph with a run of the clustering-enhancing algorithm in
     * it.
     *
     * @return
     */
    public TinkerGraph enhanceGraphClustering() {

        saveDegrees();

        GraphTraversalSource g = graph.traversal();
        GraphTraversal<Vertex, Vertex> t = g.V().unfold();

        while (t.hasNext()) {

            Vertex v = t.next();

            if (nodeDegrees.get(v) > 1) {

                enhanceNeighborsOf(v);

            }

        }

        return graph;

    }

    /**
     * We need this to keep a snapshot of the degrees as they will be changed as
     * we go.
     */
    private void saveDegrees() {

        GraphTraversalSource g = graph.traversal();
        GraphTraversal<Vertex, Vertex> t = g.V().unfold();

        while (t.hasNext()) {

            Vertex v = t.next();
            int vertexDegree = Misc.getVertexDegree(v);
            nodeDegrees.put(v, vertexDegree);

            if (vertexDegree > 1) {

                sumOfDegreesOverOne = sumOfDegreesOverOne + vertexDegree;

            }

        }

    }

    /**
     * Method that enhances the neighbors of a vertex.
     *
     * @param v
     */
    private void enhanceNeighborsOf(Vertex v) {

        Iterator<Vertex> neighbors = v.vertices(Direction.BOTH);
        LinkedList<Vertex> neighborList = new LinkedList<Vertex>();

        while (neighbors.hasNext()) {

            Vertex neighbor = neighbors.next();
            neighborList.add(neighbor);

        }

        int nodeDegree = nodeDegrees.get(v);
        double p = nodeDegree / ((double) (sumOfDegreesOverOne));

        neighbors = v.vertices(Direction.BOTH);
        while (neighbors.hasNext()) {

            Vertex neighbor = neighbors.next();
            attachToNeighborsBasedOnP(neighbor, neighborList, p);

        }

    }

    /**
     * This does the probabilistic attachment.
     *
     * @param neighbor
     * @param neighborList
     * @param p
     */
    private void attachToNeighborsBasedOnP(Vertex neighbor, List<Vertex> neighborList, double p) {

        Iterator<Vertex> neighbors = neighborList.iterator();

        while (neighbors.hasNext()) {

            Vertex v = neighbors.next();
            boolean shouldAttach = Misc.binaryDiceWithBias(p);

            if (shouldAttach & !Misc.alreadyExistsEdge(v, neighbor)) {
                neighbor.addEdge("enhanced_edge", v, T.id, Long.toHexString(Double.doubleToLongBits(Math.random())));
            }

        }

    }

}
