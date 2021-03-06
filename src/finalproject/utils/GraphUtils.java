package finalproject.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

/**
 * @author C. Savkli, Dec 20, 2017
 * @version 1.0
 */
public class GraphUtils {

    // Open a graph saved in GraphML format
    public static TinkerGraph readGraphML(String file) {
        TinkerGraph graph = TinkerGraph.open();
        try {
            graph.io(IoCore.graphml()).readGraph(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    // Open a graph saved in GraphSON format
    public static TinkerGraph readGraphSON(String file) {
        TinkerGraph graph = TinkerGraph.open();
        try {
            graph.io(IoCore.graphson()).readGraph(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    // Save graph in GraphML format
    public static void saveGraphML(TinkerGraph graph, String file) {

        try {
            OutputStream os = new FileOutputStream(file);
            graph.io(IoCore.graphml()).writer().normalize(true).create().writeGraph(os, graph);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save graph in GraphSON format
    public static void saveGraphSON(TinkerGraph graph, String file) {
        try {
            OutputStream os = new FileOutputStream(file);
            graph.io(IoCore.graphson()).writer().create().writeGraph(os, graph);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Print vertex attributes of a graph
    public static void printAttributes(TinkerGraph graph, List<String> attributes) {
        GraphTraversalSource g = graph.traversal();

        GraphTraversal<Vertex, Vertex> t = g.V().unfold();

        while (t.hasNext()) {
            Vertex v = t.next();
            for (String att : attributes) {
                System.out.print(att + ":\t " + v.value(att) + " ");
            }
            System.out.print("\n");
        }
    }

    // Convert GraphML files generated by version 2 of TinkerPop interface to version 3. 
    public static void convertTP2GraphMLtoTP3(String graphMLTP2, String graphMLTP3) {
        InputStream stylesheet = Thread.currentThread().getContextClassLoader().getResourceAsStream("tp2-to-tp3-graphml.xslt");
        File datafile = new File(graphMLTP2);
        File outfile = new File(graphMLTP3);

        TransformerFactory tFactory = TransformerFactory.newInstance();
        StreamSource stylesource = new StreamSource(stylesheet);
        Transformer transformer;
        try {
            transformer = tFactory.newTransformer(stylesource);

            StreamSource source = new StreamSource(datafile);
            StreamResult result = new StreamResult(new FileWriter(outfile));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
