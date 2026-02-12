package engine.view.core;

// region Imports
import engine.utils.profiling.core.AbstractProfiler;
import engine.utils.profiling.core.MetricType;
// endregion

/**
 * Specialized profiler for render loop timing and FPS metrics.
 *
 * Tracks update/draw/frame durations and computes FPS per reporting period.
 */
public class RendererProfiler extends AbstractProfiler {

    // region Constants

    // FRAME Draw breakdown metrics
    public static final String METRIC_TOTAL_FRAME = "TOTAL_FRAME";
    public static final String METRIC_DRAW_PHASE = "DRAW_PHASE";
    public static final String METRIC_UPDATE_PHASE = "UPDATE_PHASE";

    // Draw breakdown metrics
    public static final String METRIC_DRAW_BACKGROUND = "BACKGROUND";
    public static final String METRIC_TRANSLATE = "TRANSLATE";
    public static final String METRIC_DRAW_STATIC = "STATIC";
    public static final String METRIC_DRAW_DYNAMIC = "DYNAMIC";
    public static final String METRIC_DRAW_HUDS = "HUDS";
    public static final String METRIC_SHOW = "SHOW";

    // Dynamic breakdown metrics
    public static final String METRIC_QUERY_DYNAMIC = "QUERY_DYNAMIC";
    public static final String METRIC_PAINT_DYNAMIC = "PAINT_DYNAMIC";
    // endregion Constants

    // region Fields
    private long framesInPeriod = 0L;
    private volatile long lastFps = 0L;
    // endregion Fields

    // region Constructors
    public RendererProfiler(long reportIntervalNanos) {
        super(reportIntervalNanos);
    }
    // endregion Constructors

    // *** PUBLICS ***

    // region Add
    public void addFrame() {
        this.framesInPeriod++;
    }
    // endregion Add

    // region Get
    public double getAvgDrawMs() {
        return getAvgMs(METRIC_DRAW_PHASE);
    }

    public double getAvgFrameMs() {
        return getAvgMs(METRIC_TOTAL_FRAME);
    }

    public double getAvgUpdateMs() {
        return getAvgMs(METRIC_UPDATE_PHASE);
    }

    // Draw breakdown getters
    public double getAvgDrawBackgroundMs() {
        return getAvgMs(METRIC_DRAW_BACKGROUND);
    }

    public double getAvgDrawStaticMs() {
        return getAvgMs(METRIC_DRAW_STATIC);
    }

    public double getAvgDrawDynamicMs() {
        return getAvgMs(METRIC_DRAW_DYNAMIC);
    }

    public double getAvgDrawHudsMs() {
        return getAvgMs(METRIC_DRAW_HUDS);
    }

    public double getAvgShowMs() {
        return getAvgMs(METRIC_SHOW);
    }

    public double getAvgPaintDynamicMs() {
        return getAvgMs(METRIC_PAINT_DYNAMIC);
    }

    public double getAvgQueryDynamicMs() {
        return getAvgMs(METRIC_QUERY_DYNAMIC);
    }

    public double getAvgTranslateMs() {
        return getAvgMs(METRIC_TRANSLATE);
    }

    public long getLastFps() {
        return this.lastFps;
    }
    // endregion Get

    // *** INTERFACE IMPLEMENTATIONS ***

    // region AbstractProfiler
    @Override
    protected void configureMetrics() {
        addMetric(METRIC_DRAW_PHASE, MetricType.INTERVAL);
        addMetric(METRIC_TOTAL_FRAME, MetricType.INTERVAL);
        addMetric(METRIC_UPDATE_PHASE, MetricType.INTERVAL);

        // Draw breakdown
        addMetric(METRIC_DRAW_BACKGROUND, MetricType.INTERVAL);
        addMetric(METRIC_TRANSLATE, MetricType.INTERVAL);
        addMetric(METRIC_DRAW_STATIC, MetricType.INTERVAL);
        addMetric(METRIC_DRAW_DYNAMIC, MetricType.INTERVAL);
        addMetric(METRIC_DRAW_HUDS, MetricType.INTERVAL);
        addMetric(METRIC_SHOW, MetricType.INTERVAL);

        // Dynamic breakdown
        addMetric(METRIC_QUERY_DYNAMIC, MetricType.INTERVAL);
        addMetric(METRIC_PAINT_DYNAMIC, MetricType.INTERVAL);
    }

    @Override
    protected void customReport() {
        // No console output by default
    }

    @Override
    protected void onPeriodEnd(long elapsedNanos) {
        if (elapsedNanos > 0L) {
            this.lastFps = Math.round(this.framesInPeriod * (1_000_000_000.0 / elapsedNanos));
        } else {
            this.lastFps = 0L;
        }

        this.framesInPeriod = 0L;
    }
    // endregion AbstractProfiler
}
