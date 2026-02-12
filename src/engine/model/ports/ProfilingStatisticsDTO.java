package engine.model.ports;

import java.util.Map;
import engine.utils.profiling.core.MetricDTO;

/**
 * DTO containing performance profiling metrics for the current frame.
 * 
 * Encapsulates metrics captured without exposing the profiler instance.
 */
public class ProfilingStatisticsDTO {

    // region Fields
    public final Map<String, MetricDTO> metrics;
    public final long captureTimeNanos;
    // endregion Fields

    // region Constructors
    public ProfilingStatisticsDTO(Map<String, MetricDTO> metrics) {
        this.metrics = metrics != null ? metrics : Map.of();
        this.captureTimeNanos = System.nanoTime();
    }
    // endregion Constructors

    // *** PUBLICS ***

    // region Get
    public MetricDTO getMetric(String key) {
        return this.metrics.get(key);
    }

    public java.util.Set<String> getMetricKeys() {
        return this.metrics.keySet();
    }
    // endregion Get
}
