/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject.utils;

import java.util.Iterator;
import java.util.Random;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

/**
 *
 * @author Marcel Llopis <marcelr@jpl.nasa.gov>
 */
public class Misc {

    // Method to calculate the second smallest value in an array of doubles.
    public static double secondSmallest(double arr[]) {
        double first, second, arr_size = arr.length;

        if (arr_size < 2) {
            return -1.0;
        }

        first = second = Double.MAX_VALUE;
        for (int i = 0; i < arr_size; i++) {
            if (arr[i] < first) {
                second = first;
                first = arr[i];
            } else if (arr[i] < second && arr[i] != first) {
                second = arr[i];
            }
        }
        if (second == Double.MAX_VALUE) {
            return -1.0;
        } else {
            return second;
        }
    }

    public static int getPositionInArray(double value, double[] array) {

        for (int i = 0; i < array.length; i++) {

            if (array[i] == value) {
                return i;
            }

        }

        return -1;

    }

    // Method to assign a partition to a vertex
    public static void assignPartition(TinkerGraph graph, String vertexID, String partitionName) {

        GraphTraversalSource g = graph.traversal();
        GraphTraversal<Vertex, Vertex> t = g.V().unfold();

        while (t.hasNext()) {

            Vertex v = t.next();

            if (v.id().toString().equals(vertexID)) {

                v.property("partition", partitionName);

            }

        }

    }

    public static int getVertexDegree(Vertex v) {

        Iterator<Edge> edges = v.edges(Direction.BOTH);
        int degree = 0;

        while (edges.hasNext()) {

            edges.next();
            degree++;

        }

        return degree;

    }

    public static boolean binaryDiceWithBias(double prob) {

        Random randomNumGenerator = new Random();
        double randomDouble = randomNumGenerator.nextDouble();

        if (randomDouble <= prob) {
            return true;
        } else {
            return false;
        }

    }
    
    public static boolean alreadyExistsEdge(Vertex v1, Vertex v2){
        
        Iterator<Vertex> neighborsOfV1 = v1.vertices(Direction.BOTH);
        
        while(neighborsOfV1.hasNext()){
            
            Vertex neighbor = neighborsOfV1.next();
            
            if(neighbor.equals(v2)){
                
                return true;
                
            }
            
        }
        
        return false;
        
    }

}
