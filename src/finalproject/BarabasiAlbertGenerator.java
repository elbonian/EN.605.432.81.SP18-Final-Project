/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import finalproject.utils.Misc;
import static finalproject.utils.Misc.getVertexDegree;
import java.util.Random;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

/**
 *
 * @author Marcel Llopis <marcelr@jpl.nasa.gov>
 */
public class BarabasiAlbertGenerator {

    int numVertices = 0;
    int numEdges = 0;
    TinkerGraph graph = null;

    public BarabasiAlbertGenerator(int nodes) {

        numVertices = nodes;

    }

    /**
     *
     * Method that generates a scale-free graph based on the number of nodes
     * passed to the constructor. If the boolean passed is true, we remove
     * unconnected nodes after the algorithm is done.
     *
     * @param noZeroDegreeNodes
     * @return
     */
    public TinkerGraph generateGraph(boolean noZeroDegreeNodes) {

        // We only return one graph per class instantiation.
        if (graph != null) {
            return graph;
        }

        graph = TinkerGraph.open();

        for (int currVertices = 1; currVertices < numVertices; currVertices++) {

            Vertex newVertex = graph.addVertex();

            GraphTraversalSource g = graph.traversal();
            GraphTraversal<Vertex, Vertex> t = g.V().unfold();
            int numEdgesSnapshot = numEdges;

            while (t.hasNext()) {

                Vertex v = t.next();

                double attachmentProb = (getVertexDegree(v) + 1) / ((double) (numEdgesSnapshot + currVertices));
                boolean shouldAttach = Misc.binaryDiceWithBias(attachmentProb);

                if (shouldAttach) {

                    v.addEdge("original_edge", newVertex);
                    numEdges++;

                }

            }

        }

        if (noZeroDegreeNodes) {
            removeZeroDegreeNodes(graph);
        }

        return graph;

    }

    private void removeZeroDegreeNodes(TinkerGraph graph) {

        GraphTraversalSource g = graph.traversal();
        GraphTraversal<Vertex, Vertex> t = g.V().unfold();

        while (t.hasNext()) {

            Vertex v = t.next();
            if (Misc.getVertexDegree(v) == 0) {

                v.remove();

            }

        }

    }

}
