package org.neo4j.graphalgo.api;

import org.neo4j.graphalgo.core.utils.Pools;
import org.neo4j.graphdb.Direction;
import org.neo4j.logging.Log;
import org.neo4j.logging.NullLog;

import java.util.concurrent.ExecutorService;

/**
 * DTO to ease the use of the GraphFactory-CTor. Should contain
 * setup options for loading the graph from neo4j.
 *
 * @author mknblch
 */
public class GraphSetup {

    // start label type. null means any label.
    public final String startLabel;
    // end label type (not yet implemented).
    public final String endLabel;
    // relationtype name. null means any relation.
    public final String relationshipType;
    // load incoming relationships.
    public final boolean loadIncoming;
    // load outgoing relationships.
    public final boolean loadOutgoing;
    // property of relationship weights. null means NO property (the default value will be used instead).
    public final String relationWeightPropertyName;
    // default property is used for weighted relationships if property is not set.
    public final double relationDefaultWeight;
    // property of node weights. null means NO property (the default value will be used instead).
    public final String nodeWeightPropertyName;
    // default property is used for weighted nodes if property is not set.
    public final double nodeDefaultWeight;
    // additional node property. null means NO property (the default value will be used instead).
    public final String nodePropertyName;
    // default property is used for node properties if property is not set.
    public final double nodeDefaultPropertyValue;

    public final Log log;

    // the executor service for parallel execution. null means single threaded evaluation.
    public final ExecutorService executor;
    // concurrency level
    public final int concurrency;
    /**
     * batchSize for parallel compuation
     */
    public final int batchSize;
    // TODO
    public final boolean accumulateWeights;

    /**
     * main ctor
     * @param startLabel the start label. null means any label.
     * @param endLabel not implemented yet
     * @param relationshipType the relation type identifier. null for any relationship
     * @param relationWeightPropertyName property name which holds the weights / costs of a relation.
*                                   null means the default value is used for each weight.
     * @param relationDefaultWeight the default relationship weight if property is not given.
     * @param nodeWeightPropertyName property name which holds the weights / costs of a node.
*                               null means the default value is used for each weight.
     * @param nodeDefaultWeight the default node weight if property is not given.
     * @param nodePropertyName property name which holds additional values of a node.
*                         null means the default value is used for each value.
     * @param nodeDefaultPropertyValue the default node value if property is not given.
     * @param executor the executor. null means single threaded evaluation
     * @param batchSize batch size for parallel loading
     * @param accumulateWeights true if relationship-weights should be summed within the loader
     */
    public GraphSetup(
            String startLabel,
            String endLabel,
            String relationshipType,
            Direction direction,
            String relationWeightPropertyName,
            double relationDefaultWeight,
            String nodeWeightPropertyName,
            double nodeDefaultWeight,
            String nodePropertyName,
            double nodeDefaultPropertyValue,
            ExecutorService executor,
            int concurrency,
            int batchSize,
            boolean accumulateWeights,
            Log log) {

        this.startLabel = startLabel;
        this.endLabel = endLabel;
        this.relationshipType = relationshipType;
        this.loadIncoming = direction == Direction.INCOMING || direction == Direction.BOTH;
        this.loadOutgoing = direction == Direction.OUTGOING || direction == Direction.BOTH;
        this.relationWeightPropertyName = relationWeightPropertyName;
        this.relationDefaultWeight = relationDefaultWeight;
        this.nodeWeightPropertyName = nodeWeightPropertyName;
        this.nodeDefaultWeight = nodeDefaultWeight;
        this.nodePropertyName = nodePropertyName;
        this.nodeDefaultPropertyValue = nodeDefaultPropertyValue;
        this.executor = executor;
        this.concurrency = concurrency;
        this.batchSize = batchSize;
        this.accumulateWeights = accumulateWeights;
        this.log = log;
    }

    /**
     * Setup Graph to load any label, any relationship, no property in single threaded mode
     */
    public GraphSetup() {
        this.startLabel = null;
        this.endLabel = null;
        this.relationshipType = null;
        this.loadIncoming = this.loadOutgoing = true;
        this.relationWeightPropertyName = null;
        this.relationDefaultWeight = 1.0;
        this.nodeWeightPropertyName = null;
        this.nodeDefaultWeight = 1.0;
        this.nodePropertyName = null;
        this.nodeDefaultPropertyValue = 1.0;
        this.executor = null;
        this.concurrency = Pools.DEFAULT_CONCURRENCY;
        this.batchSize = -1;
        this.accumulateWeights = false;
        this.log = NullLog.getInstance();
    }

    /**
     * Setup graph to load any label, any relationship, no property but
     * in multithreaded mode (depends on the actual executor)
     *
     * @param executor executor service
     */
    public GraphSetup(ExecutorService executor) {
        this.startLabel = null;
        this.endLabel = null;
        this.relationshipType = null;
        this.loadIncoming = this.loadOutgoing = true;
        this.relationWeightPropertyName = null;
        this.relationDefaultWeight = 1.0;
        this.nodeWeightPropertyName = null;
        this.nodeDefaultWeight = 1.0;
        this.nodePropertyName = null;
        this.nodeDefaultPropertyValue = 1.0;
        this.executor = executor;
        this.concurrency = Pools.DEFAULT_CONCURRENCY;
        this.batchSize = -1;
        this.accumulateWeights = false;
        log = NullLog.getInstance();
    }

    public boolean loadConcurrent() {
        return executor != null;
    }

    public int concurrency() {
        if (!loadConcurrent()) {
            return 1;
        }
        return concurrency;
    }

    public boolean loadDefaultRelationshipWeight() {
        return relationWeightPropertyName == null;
    }

    public boolean loadDefaultNodeWeight() {
        return nodeWeightPropertyName == null;
    }

    public boolean loadDefaultNodeProperty() {
        return nodePropertyName == null;
    }

    public boolean loadAnyLabel() {
        return startLabel == null;
    }

    public boolean loadAnyRelationshipType() {
        return relationshipType == null;
    }
}
